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
import org.kohsuke.stapler.DataBoundConstructor;

/**
 * Implements NOT expression for {@link LabelVerifier}.
 * @author Oleg Nenashev <nenashev@synopsys.com>, Synopsys Inc.
 * @since 1.1
 */
public class Not extends LabelVerifier {

    private final LabelVerifier verifier;
    
    @DataBoundConstructor
    public Not(final LabelVerifier verifier) {
        this.verifier = verifier;
    }

    public LabelVerifier getVerifier() {
        return verifier;
    }

    @Override
    public void verify(LabelAtom label, Computer c, Channel channel, FilePath root, TaskListener listener) throws IOException, InterruptedException {   
        final boolean expressionIsOK = LogicHelper.verify(verifier, label, c, channel, root, listener);
        if (expressionIsOK) {
            throw new LabelVerifierException(Messages.logic_shared_evalFailureMessage(getDescriptor().getDisplayName()));
        }    
    }

    @Extension
    public static class NotDescriptor extends LabelVerifierDescriptor {

        @Override
        public String getDisplayName() {
            return Messages.logic_not_displayName();
        }
    }
}
