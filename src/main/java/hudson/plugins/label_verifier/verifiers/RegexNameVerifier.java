package hudson.plugins.label_verifier.verifiers;

import hudson.Extension;
import hudson.FilePath;
import hudson.model.Computer;
import hudson.model.TaskListener;
import hudson.model.labels.LabelAtom;
import hudson.plugins.label_verifier.LabelVerifier;
import hudson.plugins.label_verifier.LabelVerifierDescriptor;
import hudson.remoting.Channel;
import hudson.util.FormValidation;
import java.io.IOException;
import org.kohsuke.stapler.DataBoundConstructor;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
import org.kohsuke.stapler.QueryParameter;

/**
 *
 * @author Oleg Nenashev <nenashev@synopsys.com>, Synopsys Inc.
 */
public class RegexNameVerifier extends LabelVerifier  {
    String regexExpression;

    @DataBoundConstructor
    public RegexNameVerifier(String regexExpression) {
        this.regexExpression = regexExpression;
    }

    public String getRegexExpression() {
        return regexExpression;
    }
      
    @Override
    public void verify(LabelAtom label, Computer c, Channel channel, FilePath root, TaskListener listener) throws IOException, InterruptedException {
        listener.getLogger().println("Validating the label '"+label.getName()+"'");
        try {
            Pattern.compile(regexExpression);
        } catch(PatternSyntaxException ex) {
            listener.error("RegexRestriction for label "+label.getName()+" is invalid. Restriction will be skipped");
            return;
        }
               
        if (!c.getName().matches(regexExpression)) {
            throw new IOException("Label verification failed");
        }
    }
    
    @Extension
    public static class DescriptorImpl extends LabelVerifierDescriptor {
        @Override
        public String getDisplayName() {
            return "Verify By Regular Expression against the computer name";
        }
        
        public FormValidation doCheckRegexExpression(@QueryParameter String regexExpression) {
            try {
                Pattern.compile(regexExpression);
            } catch (PatternSyntaxException exception) {
                return FormValidation.error(exception.getDescription()+"\n. Restriction will be ignored");
            }
            return FormValidation.ok();
        }
    }
    
}
