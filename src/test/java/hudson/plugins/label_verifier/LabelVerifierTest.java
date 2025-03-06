/*
 * The MIT License
 *
 * Copyright (c) 2010, Kohsuke Kawaguchi
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package hudson.plugins.label_verifier;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import hudson.FilePath;
import hudson.model.Computer;
import hudson.model.Slave;
import hudson.model.TaskListener;
import hudson.model.labels.LabelAtom;
import hudson.plugins.label_verifier.verifiers.ShellScriptVerifier;
import hudson.remoting.Channel;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.jvnet.hudson.test.JenkinsRule;
import org.jvnet.hudson.test.TestExtension;
import org.jvnet.hudson.test.junit.jupiter.WithJenkins;

/**
 * @author Kohsuke Kawaguchi
 */
@WithJenkins
class LabelVerifierTest {

    private JenkinsRule j;

    @BeforeEach
    void setUp(JenkinsRule rule) {
        j = rule;
    }

    @Test
    void testConfigRoundtrip() throws Exception {
        LabelAtom l = j.jenkins.getLabelAtom("foo");

        ShellScriptVerifier v = new ShellScriptVerifier("echo bravo");
        LabelAtomPropertyImpl p = new LabelAtomPropertyImpl(List.of(v));
        l.getProperties().add(p);

        j.submit(j.createWebClient().goTo("label/foo/configure").getFormByName("config"));

        assertEquals(1, l.getProperties().size());

        LabelAtomPropertyImpl pp = l.getProperties().get(LabelAtomPropertyImpl.class);
        assertEquals(1, pp.getVerifiers().size());

        ShellScriptVerifier vv = pp.getVerifiers().get(ShellScriptVerifier.class);
        j.assertEqualDataBoundBeans(p, pp);
        j.assertEqualDataBoundBeans(v, vv);
    }

    @Test
    void testVeto() throws Exception {
        LabelAtom l = j.jenkins.getLabelAtom("foo");

        AlwaysVetoLabelVerifier lv = new AlwaysVetoLabelVerifier();
        l.getProperties().add(new LabelAtomPropertyImpl(List.of(lv)));
        Slave s = j.createSlave(l);

        try {
            s.toComputer().connect(false).get();
        } catch (ExecutionException ex) {
            // Do nothing
        }

        assertTrue(lv.vetoed, "Label has not been vetoed");
        String log = s.toComputer().getLog();
        assertTrue(log.contains("Veto!"), "Log does not contain the veto message");
        assertTrue(s.toComputer().isOffline(), "Agent should not have been started");
    }

    public static class AlwaysVetoLabelVerifier extends LabelVerifier {

        boolean vetoed;

        @Override
        public void verify(LabelAtom label, Computer c, Channel channel, FilePath root, TaskListener listener)
                throws IOException {
            vetoed = true;
            listener.error("Veto!");
            throw new IOException("Veto!");
        }

        @TestExtension("testVeto")
        public static class DescriptorImpl extends LabelVerifierDescriptor {}
    }
}
