package hudson.plugins.label_verifier;

import hudson.DescriptorExtensionList;
import hudson.matrix.AxisDescriptor;
import hudson.model.Descriptor;
import hudson.model.Hudson;

/**
 * @author Kohsuke Kawaguchi
 */
public abstract class LabelVerifierDescriptor extends Descriptor<LabelVerifier> {
    /**
     * Returns all the registered {@link LabelVerifierDescriptor}s.
     */
    public static DescriptorExtensionList<LabelVerifier,LabelVerifierDescriptor> all() {
        return Hudson.getInstance().getDescriptorList(LabelVerifier.class);
    }
}
