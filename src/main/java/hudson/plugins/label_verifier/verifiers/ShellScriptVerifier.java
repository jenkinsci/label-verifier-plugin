/*
 * The MIT License
 *
 * Copyright (c) 2010, Kohsuke Kawaguchi
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

import hudson.AbortException;
import hudson.Extension;
import hudson.FilePath;
import hudson.model.Computer;
import hudson.model.TaskListener;
import hudson.model.labels.LabelAtom;
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

        listener.getLogger().println("Validating the label '"+label.getName()+"'");
        int r = root.createLauncher(listener).launch().cmds(shell.buildCommandLine(script))
                .envs(Collections.singletonMap("LABEL",label.getName()))
                .stdout(listener).pwd(root).join();
        if (r!=0)
            throw new AbortException("The script failed. Label '"+label.getName()+"' is refused.");
    }

    @Extension
    public static class DescriptorImpl extends LabelVerifierDescriptor {
        @Override
        public String getDisplayName() {
            return "Verify By Shell Script";
        }
    }
}
