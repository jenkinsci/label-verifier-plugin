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
        for (LabelVerifier verifier : verifiers)
            verifier.verify(label,c,channel,root,listener);
    }

    @Extension
    public static class DescriptorImpl extends LabelAtomPropertyDescriptor {
        @Override
		public String getDisplayName() {
			return "Verify This Label";
		}

        public List<LabelVerifierDescriptor> getVerifierDescriptors() {
            return LabelVerifierDescriptor.all();
        }
    }
}
