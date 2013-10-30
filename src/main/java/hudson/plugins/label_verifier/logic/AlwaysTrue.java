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
import hudson.remoting.Channel;
import java.io.IOException;
import org.kohsuke.stapler.DataBoundConstructor;

/**
 * Expression, which is always OK.
 * @author Oleg Nenashev <nenashev@synopsys.com>, Synopsys Inc.
 * @since 1.1
 */
public class AlwaysTrue extends LabelVerifier {
    @DataBoundConstructor
    public AlwaysTrue() {
    }
  
    @Override
    public void verify(LabelAtom label, Computer c, Channel channel, FilePath root, TaskListener listener) throws IOException, InterruptedException {
        // Do nothing
    }
     
    @Extension
    public static class AndDescriptor extends LabelVerifierDescriptor {
        @Override
        public String getDisplayName() {
            return Messages.logic_never_displayName();
        }
    }
}
