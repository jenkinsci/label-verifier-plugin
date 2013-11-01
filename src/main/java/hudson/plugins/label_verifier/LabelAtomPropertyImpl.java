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

import hudson.Extension;
import hudson.FilePath;
import hudson.model.Computer;
import hudson.model.Saveable;
import hudson.model.TaskListener;
import hudson.model.labels.LabelAtom;
import hudson.model.labels.LabelAtomProperty;
import hudson.model.labels.LabelAtomPropertyDescriptor;
import hudson.remoting.Channel;
import hudson.util.DescribableList;
import org.kohsuke.stapler.DataBoundConstructor;

import java.io.IOException;
import java.util.List;

/**
 * Contributes {@link LabelAtomProperty} that verifies labels.
 * 
 * @author Kohsuke Kawaguchi
 */
public class LabelAtomPropertyImpl extends LabelAtomProperty {
    // TODO: set a valid parent
    private final DescribableList<LabelVerifier,LabelVerifierDescriptor> verifiers =
            new DescribableList<LabelVerifier,LabelVerifierDescriptor>(Saveable.NOOP);

    @DataBoundConstructor
    public LabelAtomPropertyImpl(List<? extends LabelVerifier> verifiers) throws IOException {
        this.verifiers.replaceBy(verifiers);
    }

    public DescribableList<LabelVerifier, LabelVerifierDescriptor> getVerifiers() {
        return verifiers;
    }

    public void verify(LabelAtom label, Computer c, Channel channel, FilePath root, TaskListener listener) throws IOException, InterruptedException {
        // Print message
        listener.getLogger().println(Messages.shared_validatingLabelMessage(label.getName()));
        
        for (LabelVerifier verifier : verifiers)
            verifier.verify(label,c,channel,root,listener);
    }

    @Extension
    public static class DescriptorImpl extends LabelAtomPropertyDescriptor {
        @Override
        public String getDisplayName() {
            return Messages.LabelAtomPropertyImpl_displayName();
        }

        public List<LabelVerifierDescriptor> getVerifierDescriptors() {
            return LabelVerifierDescriptor.all();
        }
    }
}
