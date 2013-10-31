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
