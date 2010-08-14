package hudson.plugins.label_verifier.verifiers;

import hudson.AbortException;
import hudson.Extension;
import hudson.FilePath;
import hudson.model.Computer;
import hudson.model.TaskListener;
import hudson.model.label.LabelAtom;
import hudson.plugins.label_verifier.LabelVerifier;
import hudson.plugins.label_verifier.LabelVerifierDescriptor;
import hudson.remoting.Channel;
import hudson.tasks.Shell;
import org.kohsuke.stapler.DataBoundConstructor;

import java.io.IOException;
import java.util.Collections;

/**
 * Verifies the label by running a shell script.
 * 
 * @author Kohsuke Kawaguchi
 */
public class ShellScriptVerifier extends LabelVerifier {
    public final String script;

    @DataBoundConstructor
    public ShellScriptVerifier(String script) {
        this.script = script;
    }

    @Override
    public void verify(LabelAtom label, Computer c, Channel channel, FilePath root, TaskListener listener) throws IOException, InterruptedException {
        Shell shell = new Shell(this.script);
        FilePath script = shell.createScriptFile(root);
        shell.buildCommandLine(script);

        int r = root.createLauncher(listener).launch().cmds(shell.buildCommandLine(script))
                .envs(Collections.singletonMap("LABEL",label.getName()))
                .stdout(listener).pwd(root).join();
        if (r!=0)
            throw new AbortException("The script failed. Label "+label.getName()+" is refused.");
    }

    @Extension
    public static class DescriptorImpl extends LabelVerifierDescriptor {
        @Override
        public String getDisplayName() {
            return "Verify By Shell Script";
        }
    }
}
