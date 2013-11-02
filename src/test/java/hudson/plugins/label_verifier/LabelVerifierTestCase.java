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
package hudson.plugins.label_verifier;

import hudson.EnvVars;
import hudson.FilePath;
import hudson.model.Computer;
import hudson.model.Slave;
import hudson.model.TaskListener;
import hudson.model.labels.LabelAtom;
import hudson.remoting.Channel;
import java.io.IOException;
import java.util.ArrayList;
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
    
    protected LabelAtom createUniqueLabelAtom() {
        return hudson.getLabelAtom(LABEL_NAME_PREFIX+getLabelNameCtr());
    }
    
    protected void runTest(LabelVerifier expression, boolean expectFail) 
            throws InterruptedException {
        runTest(expression, expectFail, null);
    }
    
    protected void runTest(LabelVerifier expression, boolean expectFail, String expectedFailMessage) 
            throws InterruptedException {
        LabelAtom testLabel = createUniqueLabelAtom();
        runTest(expression, testLabel, expectFail, expectedFailMessage);
    }
    
    protected void runTest(LabelVerifier expression, LabelAtom testLabel, boolean expectFail, String expectedFailMessage)
            throws InterruptedException {
        runTest(expression, null, testLabel, expectFail, expectedFailMessage);
    }

    /**
     * Generic wrapper for testing of {@link LabelVerifier} classes.
     * @param expression Expression to be checked
     * @param nodeName Name of the node to be created (may be null)
     * @param testLabel Label to be added
     * @param expectFail Expect that label verification fails
     * @param expectedFailMessage Expect the following verification message (will be ignored if null)
     * @throws InterruptedException 
     */
    protected void runTest(LabelVerifier expression, String nodeName, LabelAtom testLabel,
            boolean expectFail, String expectedFailMessage)
            throws InterruptedException {
        final TestVerifier testVerifier = new TestVerifier(expression);

        // Init node
        try {
            Slave s = nodeName != null ? createSlave(nodeName, testLabel.getName(), new EnvVars()) : createSlave(testLabel);
            testLabel.getProperties().add(new LabelAtomPropertyImpl(Arrays.asList(testVerifier)));
            s.toComputer().connect(true).get();
        } catch (Exception ex) {
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
    
    protected ArrayList<LabelVerifier> createArray(LabelVerifier ... verifiers) {
        ArrayList<LabelVerifier> list = new ArrayList<LabelVerifier>(verifiers.length);
        list.addAll(Arrays.asList(verifiers));
        return list;
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
