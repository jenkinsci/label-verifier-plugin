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
package hudson.plugins.label_verifier.util;

import hudson.plugins.label_verifier.LabelVerifier;
import hudson.plugins.label_verifier.Messages;
import java.io.IOException;

/**
 * A label verification exception.
 * This class should contain description of validation errors.
 * @author Oleg Nenashev <nenashev@synopsys.com>, Synopsys Inc.
 * @since 1.1
 */
//TODO: Support of logic traces
public class LabelVerifierException extends IOException {

    public LabelVerifierException(String message) {
        super(message);
    }

    public LabelVerifierException(Throwable cause) {
        super(cause);
    }

    public LabelVerifierException(String message, Throwable cause) {
        super(message, cause);
    }
    
    public static void evaluationError (LabelVerifier verifier) 
            throws LabelVerifierException {
        throw new LabelVerifierException
            (Messages.shared_evalFailureMessage(
                verifier.getDescriptor().getShortName()));
    }
}
