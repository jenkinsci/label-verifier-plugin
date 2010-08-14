package hudson.plugins.label_verifier;

import hudson.Extension;
import hudson.FilePath;
import hudson.model.Computer;
import hudson.model.TaskListener;
import hudson.model.label.LabelAtom;
import hudson.remoting.Channel;
import hudson.slaves.ComputerListener;

import java.io.IOException;
import java.util.Set;

/**
 * Main hook into label verification.
 *
 * @author Kohsuke Kawaguchi
 */
@Extension
public class ComputerListenerImpl extends ComputerListener {
    @Override
    public void preOnline(Computer c, Channel channel, FilePath root, TaskListener listener) throws IOException, InterruptedException {
        Set<LabelAtom> labels = c.getNode().getAssignedLabels();
        for (LabelAtom label : labels) {
            LabelAtomPropertyImpl lap = label.getProperties().get(LabelAtomPropertyImpl.class);
            if (lap!=null)
                lap.verify(label,c,channel,root,listener);
        }
    }
}
