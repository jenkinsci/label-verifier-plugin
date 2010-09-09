package hudson.plugins.label_verifier;

import hudson.ExtensionPoint;
import hudson.FilePath;
import hudson.model.AbstractDescribableImpl;
import hudson.model.Computer;
import hudson.model.Node;
import hudson.model.TaskListener;
import hudson.model.labels.LabelAtom;
import hudson.remoting.Channel;

import java.io.IOException;

/**
 * @author Kohsuke Kawaguchi
 */
public abstract class LabelVerifier extends AbstractDescribableImpl<LabelVerifier> implements ExtensionPoint {
    /**
     * Called before a {@link Computer} becomes online to verify if the label assignment is correct.
     *
     * @param label
     *      Label whose validity you'll check.
     * @param channel
     *      This is the channel object to talk to the slave.
     *      (This is the same object returned by {@link Computer#getChannel()} once
     *      it's connected.
     * @param root
     *      The directory where this slave stores files.
     *      The same as {@link Node#getRootPath()}, except that method returns
     *      null until the slave is connected. So this parameter is passed explicitly instead.
     * @param listener
     *      This is connected to the launch log of the computer.
     *      Since this method is called synchronously from the thread
     *      that launches a computer, if this method performs a time-consuming
     *      operation, this listener should be notified of the progress.
     *      This is also a good listener for reporting problems.
     *
     * @throws IOException
     *      Exceptions will be recorded to the listener, and
     *      the computer will not become online.
     * @throws InterruptedException
     *      Exceptions will be recorded to the listener, and
     *      the computer will not become online.
     */
    public abstract void verify(LabelAtom label, Computer c, Channel channel, FilePath root, TaskListener listener) throws IOException, InterruptedException;

    @Override
    public LabelVerifierDescriptor getDescriptor() {
        return (LabelVerifierDescriptor)super.getDescriptor();
    }
}
