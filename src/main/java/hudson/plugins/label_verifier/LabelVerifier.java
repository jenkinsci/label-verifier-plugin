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
    public abstract void verify(LabelAtom label, Computer c, Channel channel, FilePath root, TaskListener listener)
            throws IOException, InterruptedException;

    @Override
    public LabelVerifierDescriptor getDescriptor() {
        return (LabelVerifierDescriptor) super.getDescriptor();
    }
}
