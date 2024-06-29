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

import hudson.Extension;
import hudson.FilePath;
import hudson.model.Computer;
import hudson.model.TaskListener;
import hudson.model.labels.LabelAtom;
import hudson.plugins.label_verifier.LabelVerifier;
import hudson.plugins.label_verifier.LabelVerifierDescriptor;
import hudson.plugins.label_verifier.Messages;
import hudson.plugins.label_verifier.util.LabelVerifierException;
import hudson.remoting.Channel;
import java.io.IOException;
import java.util.ArrayList;
import org.kohsuke.stapler.DataBoundConstructor;

/**
 * Implements AND expression for {@link LabelVerifier}.
 * @author Oleg Nenashev
 * @since 1.1
 */
public class Or extends LabelVerifier {
    private final ArrayList<LabelVerifier> verifiers;

    @DataBoundConstructor
    public Or(final ArrayList<LabelVerifier> verifiers) {
        this.verifiers = verifiers;
    }

    public ArrayList<LabelVerifier> getVerifiers() {
        return verifiers;
    }

    @Override
    public void verify(LabelAtom label, Computer c, Channel channel, FilePath root, TaskListener listener)
            throws IOException, InterruptedException {
        for (LabelVerifier verifier : verifiers) {
            if (LogicHelper.verify(verifier, label, c, channel, root, listener)) {
                return; // Expression is true
            }
        }

        LabelVerifierException.evaluationError(this);

        // TODO: Provide more info in messages
    }

    @Extension
    public static class OrDescriptor extends LabelVerifierDescriptor {
        @Override
        public String getDisplayName() {
            return Messages.logic_or_displayName();
        }
    }
}
