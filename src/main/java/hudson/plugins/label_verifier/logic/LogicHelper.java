/*
 * The MIT License
 *
 * Copyright 2013 Synopsys Inc., Oleg Nenashev
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
package hudson.plugins.label_verifier.logic;

import hudson.FilePath;
import hudson.model.Computer;
import hudson.model.TaskListener;
import hudson.model.labels.LabelAtom;
import hudson.plugins.label_verifier.LabelVerifier;
import hudson.remoting.Channel;
import java.io.IOException;

/**
 * Provides helper methods for logic operations.
 * @author Oleg Nenashev <nenashev@synopsys.com>, Synopsys Inc.
 */
class LogicHelper {
    /**
     * Verifies label and returns result.
     * The method bypasses interrupted exceptions.
     * @param verifier Verifier to be checked
     * @param label Checked label
     * @param c Computer to be checked
     * @param channel Slave communication channel
     * @param root Slave&squot;s Root Directory
     * @param listener Log listener
     * @return true if the label verification passed.
     * @throws InterruptedException 
     */
    public static boolean verify(LabelVerifier verifier, LabelAtom label, Computer c, Channel channel, FilePath root, TaskListener listener) throws InterruptedException {
        try {
            verifier.verify(label, c, channel, root, listener);
        } catch (IOException ex) {
            return false;
        }
        return true;
    }
}
