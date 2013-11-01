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

import hudson.FilePath;
import hudson.model.Computer;
import hudson.model.Hudson;
import hudson.model.Label;
import hudson.model.Slave;
import hudson.model.TaskListener;
import hudson.model.labels.LabelAtom;
import hudson.plugins.label_verifier.verifiers.ShellScriptVerifier;
import hudson.remoting.Channel;
import hudson.slaves.DumbSlave;
import org.jvnet.hudson.test.HudsonTestCase;

import java.io.IOException;
import java.util.Arrays;

/**
 * @author Kohsuke Kawaguchi
 */
public class LabelVerifierTest extends HudsonTestCase {
    public void testConfigRoundtrip() throws Exception {
        LabelAtom l = hudson.getLabelAtom("foo");

        ShellScriptVerifier v =  new ShellScriptVerifier("echo bravo");
        LabelAtomPropertyImpl p = new LabelAtomPropertyImpl(Arrays.asList(v));
        l.getProperties().add(p);

        submit(createWebClient().goTo("/label/foo/configure").getFormByName("config"));

        assertEquals(1, l.getProperties().size());

        LabelAtomPropertyImpl pp = l.getProperties().get(LabelAtomPropertyImpl.class);
        assertEquals(1, pp.getVerifiers().size());

        ShellScriptVerifier vv = pp.getVerifiers().get(ShellScriptVerifier.class);
        assertEqualDataBoundBeans(p,pp);
        assertEqualDataBoundBeans(v,vv);
    }

    public void testVeto() throws Exception {
        LabelAtom l = hudson.getLabelAtom("foo");
        final boolean[] veto = new boolean[1];

        LabelVerifier lv = new LabelVerifier() {
            @Override
            public void verify(LabelAtom label, Computer c, Channel channel, FilePath root, TaskListener listener) throws IOException, InterruptedException {
                veto[0] = true;
                throw new IOException("Veto!");
            }
            Object writeReplace() {return new ShellScriptVerifier("echo");}
        };
        l.getProperties().add(new LabelAtomPropertyImpl(Arrays.asList(lv)));

        Slave s = createSlave(l);
        s.toComputer().connect(false).get();
        assertTrue(veto[0]);
        String log = s.toComputer().getLog();
        assertTrue(log,log.contains("Veto!"));

        assertTrue(s.toComputer().isOffline());
    }
}
