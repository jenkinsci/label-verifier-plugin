/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
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
 * Expression, which always fails the evaluation.
 * @author Oleg Nenashev <nenashev@synopsys.com>, Synopsys Inc.
 * @since 1.1
 */
public class AlwaysFalse extends LabelVerifier {
    @DataBoundConstructor
    public AlwaysFalse() {
    }
 
    @Override
    public void verify(LabelAtom label, Computer c, Channel channel, FilePath root, TaskListener listener) throws IOException, InterruptedException {
        LabelVerifierException.evaluationError(this);
    }
    
    
    @Extension
    public static class AndDescriptor extends LabelVerifierDescriptor {
        @Override
        public String getDisplayName() {
            return Messages.logic_always_displayName();
        }
    }
}
