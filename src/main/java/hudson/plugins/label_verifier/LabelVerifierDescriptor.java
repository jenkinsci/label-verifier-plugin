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

import hudson.DescriptorExtensionList;
import hudson.model.Descriptor;
import java.util.List;
import jenkins.model.Jenkins;

/**
 * @author Kohsuke Kawaguchi
 */
public abstract class LabelVerifierDescriptor extends Descriptor<LabelVerifier> {
    /**
     * Returns all the registered {@link LabelVerifierDescriptor}s.
     */
    public static DescriptorExtensionList<LabelVerifier, LabelVerifierDescriptor> all() {
        return Jenkins.getActiveInstance().getDescriptorList(LabelVerifier.class);
    }

    /**
     * Gets available descriptors.
     * Function is dedicated to be used in logic expressions and other verifiers,
     * which contain nested descriptors.
     * @return all the registered {@link LabelVerifierDescriptor}s.
     * @since 1.1
     */
    public List<LabelVerifierDescriptor> getVerifierDescriptors() {
        return LabelVerifierDescriptor.all();
    }

    /**
     * Returns a short name to be displayed in messages.
     * @return Short name (displayName in default implementation)
     * @since 1.1
     */
    public String getShortName() {
        return getDisplayName();
    }
}
