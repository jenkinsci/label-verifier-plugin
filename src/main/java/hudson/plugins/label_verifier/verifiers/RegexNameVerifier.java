/*
 * The MIT License
 *
 * Copyright 2013 Synopsys Inc., Oleg Nenashev
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
 * Verifies the computer name by a regular expression.
 * @author Oleg Nenashev <nenashev@synopsys.com>, Synopsys Inc.
 * @since 1.1
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
            return "Verify computer name by a regular expression";
        }
        
        public FormValidation doCheckRegexExpression(@QueryParameter String regexExpression) {
            try {
                Pattern.compile(regexExpression);
            } catch (PatternSyntaxException exception) {
                return FormValidation.error(exception.getDescription()+"\nRestriction will be ignored");
            }
            return FormValidation.ok();
        }
    }
    
}
