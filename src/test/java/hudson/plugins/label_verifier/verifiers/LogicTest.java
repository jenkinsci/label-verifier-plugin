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

import hudson.plugins.label_verifier.LabelVerifier;
import hudson.plugins.label_verifier.LabelVerifierTestCase;
import hudson.plugins.label_verifier.logic.AlwaysFalse;
import hudson.plugins.label_verifier.logic.AlwaysTrue;
import hudson.plugins.label_verifier.logic.And;
import hudson.plugins.label_verifier.logic.Not;
import hudson.plugins.label_verifier.logic.Or;
import org.junit.jupiter.api.Test;
import org.jvnet.hudson.test.junit.jupiter.WithJenkins;

/**
 * Contains tests for logic expressions.
 * @author Oleg Nenashev
 * @since 1.1
 */
// TODO: add tests for other logic expressions
@WithJenkins
class LogicTest extends LabelVerifierTestCase {
    private static final LabelVerifier TRUE = new AlwaysTrue();
    private static final LabelVerifier FALSE = new AlwaysFalse();

    /**
     * Checks cases with {@link AlwaysTrue} expression.
     */
    @Test
    void testAlwaysTrue() throws Exception {
        runTest(new AlwaysTrue());
    }

    /**
     * Checks cases with {@link AlwaysFalse} expression.
     */
    @Test
    void testAlwaysFalse() throws Exception {
        runTest(new AlwaysFalse(), true, null);
    }

    /**
     * Checks typical cases for {@link Not} expression.
     */
    @Test
    void testNot() throws Exception {
        runTest(new Not(new AlwaysTrue()), true, null);
        runTest(new Not(new AlwaysFalse()));
    }

    /**
     * Checks typical cases for {@link And} expression.
     */
    @Test
    void testAnd() throws Exception {
        runTest(new And(createArray(FALSE, FALSE)), true);
        runTest(new And(createArray(TRUE, FALSE)), true);
        runTest(new And(createArray(FALSE, TRUE)), true);
        runTest(new And(createArray(TRUE, TRUE)), false);

        // And some multiple
        runTest(new And(createArray(FALSE, FALSE, FALSE, FALSE)), true);
        runTest(new And(createArray(TRUE, TRUE, TRUE, FALSE)), true);
        runTest(new And(createArray(TRUE, TRUE, TRUE, TRUE)), false);
    }

    /**
     * Checks typical cases for {@link Or} expression.
     */
    @Test
    void testOr() throws Exception {
        runTest(new Or(createArray(FALSE, FALSE)), true);
        runTest(new Or(createArray(TRUE, FALSE)), false);
        runTest(new Or(createArray(FALSE, TRUE)), false);
        runTest(new Or(createArray(TRUE, TRUE)), false);

        // And some multiple
        runTest(new Or(createArray(FALSE, FALSE, FALSE, FALSE)), true);
        runTest(new Or(createArray(TRUE, FALSE, FALSE, FALSE)), false);
        runTest(new Or(createArray(TRUE, FALSE, TRUE, FALSE)), false);
        runTest(new Or(createArray(TRUE, TRUE, TRUE, FALSE)), false);
        runTest(new Or(createArray(TRUE, TRUE, TRUE, TRUE)), false);
    }
}
