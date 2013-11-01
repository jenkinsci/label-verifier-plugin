/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package hudson.plugins.label_verifier;

import hudson.FilePath;
import hudson.model.Computer;
import hudson.model.Slave;
import hudson.model.TaskListener;
import hudson.model.labels.LabelAtom;
import hudson.remoting.Channel;
import java.io.IOException;
import java.util.Arrays;
import static junit.framework.Assert.assertEquals;
import org.jvnet.hudson.test.HudsonTestCase;

/**
 * Provides help methods for {@link LabelVerifier} tests.
 * @author Oleg Nenashev <nenashev@synopsys.com>, Synopsys Inc.
 * @since 1.1
 */
public abstract class LabelVerifierTestCase extends HudsonTestCase {
    private static final String LABEL_NAME_PREFIX = "foo";
    private static int LABEL_NAME_CTR = 0;
    
    protected void runTest(LabelVerifier expression) throws Exception {
        runTest(expression, false, null);
    }
    
    private synchronized static int getLabelNameCtr() {
        return ++LABEL_NAME_CTR;
    }
    
    protected void runTest(LabelVerifier expression, boolean expectFail, String expectedFailMessage) 
            throws InterruptedException {
        LabelAtom testLabel = hudson.getLabelAtom(LABEL_NAME_PREFIX+getLabelNameCtr());
        runTest(expression, testLabel, expectFail, expectedFailMessage);
    }
    
    protected void runTest(LabelVerifier expression, LabelAtom testLabel, boolean expectFail, String expectedFailMessage)
            throws InterruptedException {
        
        final TestVerifier testVerifier = new TestVerifier(expression);
        
        // Init node
        try {
            Slave s = createSlave(testLabel);       
            testLabel.getProperties().add(new LabelAtomPropertyImpl(Arrays.asList(testVerifier)));
            s.toComputer().connect(true).get();
        } catch(Exception ex) {
            ex.printStackTrace();
            // do nothing
        }

        // Analyze results   
        if (testVerifier.isExceptionThrown()) {
            Exception ex = testVerifier.getThrownException();
            System.out.print(ex.getMessage());
            ex.printStackTrace();

            if (expectFail) {
                // Check message
                if (expectedFailMessage != null) {
                    assertEquals(expectedFailMessage, ex.getMessage());
                }
            }
        } else if (expectFail) {
            fail("Exception has been expected");
        }
    }

    protected static class TestVerifier extends LabelVerifier {

        private final LabelVerifier wrappedVerifier;
        private IOException thrownException;

        public TestVerifier(LabelVerifier wrappedVerifier) {
            this.wrappedVerifier = wrappedVerifier;
            this.thrownException = null;
        }

        public IOException getThrownException() {
            return thrownException;
        }

        public boolean isExceptionThrown() {
            return thrownException != null;
        }

        @Override
        public void verify(LabelAtom label, Computer c, Channel channel, FilePath root, TaskListener listener) throws IOException, InterruptedException {
            try {
                wrappedVerifier.verify(label, c, channel, root, listener);
            } catch (IOException ex) {
                thrownException = ex;
            }
        }
    };
}
