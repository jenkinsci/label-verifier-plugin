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

import java.util.ArrayList;

/**
 * Implements AND expression for {@link LabelVerifier}.
 * @author Oleg Nenashev <nenashev@synopsys.com>, Synopsys Inc.
 * @since 1.1
 */
public class And extends LabelVerifier {

    private final ArrayList<LabelVerifier> verifiers;
    
    @DataBoundConstructor
    public And(final ArrayList<LabelVerifier> verifiers) {
        this.verifiers = verifiers;
    }

    public ArrayList<LabelVerifier> getVerifiers() {
        return verifiers;
    }
  
    @Override
    public void verify(LabelAtom label, Computer c, Channel channel, FilePath root, TaskListener listener) throws IOException, InterruptedException {
        for (LabelVerifier verifier : verifiers) {
            if (!LogicHelper.verify(verifier, label, c, channel, root, listener)) {
                throw new LabelVerifierException(Messages.logic_shared_evalFailureMessage(getDescriptor().getDisplayName()));
            }
        }
    }

    @Extension
    public static class AndDescriptor extends LabelVerifierDescriptor {

        @Override
        public String getDisplayName() {
            return Messages.logic_and_displayName();
        }

    }

}
