/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package hudson.plugins.label_verifier.logic;

import hudson.FilePath;
import hudson.model.Computer;
import hudson.model.TaskListener;
import hudson.model.labels.LabelAtom;
import hudson.plugins.label_verifier.LabelVerifier;
import hudson.remoting.Channel;
import java.io.IOException;

/**
 * Provides helper methods for logic operations.
 * @author Oleg Nenashev <nenashev@synopsys.com>, Synopsys Inc.
 */
class LogicHelper {
    /**
     * Verifies label and returns result.
     * The method bypasses interrupted exceptions.
     * @param verifier Verifier to be checked
     * @param label Checked label
     * @param c Computer to be checked
     * @param channel Slave communication channel
     * @param root Slave&squot;s Root Directory
     * @param listener Log listener
     * @return true if the label verification passed.
     * @throws InterruptedException 
     */
    public static boolean verify(LabelVerifier verifier, LabelAtom label, Computer c, Channel channel, FilePath root, TaskListener listener) throws InterruptedException {
        try {
            verifier.verify(label, c, channel, root, listener);
        } catch (IOException ex) {
            return false;
        }
        return true;
    }
}
