package hudson.plugins.label_verifier;

import hudson.DescriptorExtensionList;
import hudson.matrix.AxisDescriptor;
import hudson.model.Descriptor;
import hudson.model.Hudson;
import java.util.List;

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
}
