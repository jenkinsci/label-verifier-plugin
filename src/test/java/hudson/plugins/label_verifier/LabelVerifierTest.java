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
