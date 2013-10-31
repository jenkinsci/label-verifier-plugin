/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package logic;

import hudson.plugins.label_verifier.LabelVerifierTestCase;
import hudson.plugins.label_verifier.logic.AlwaysFalse;
import hudson.plugins.label_verifier.logic.AlwaysTrue;
import hudson.plugins.label_verifier.logic.Not;

/**
 * Contains tests for logic expressions.
 * @author Oleg Nenashev <nenashev@synopsys.com>, Synopsys Inc.
 * @since 1.1
 */
//TODO: add tests for other logic expressions
public class LogicTest extends LabelVerifierTestCase {
     
   /**
    * Checks cases with {@link AlwaysTrue} expression. 
    */
   public void testAlwaysTrue() throws Exception {
       runTest(new AlwaysTrue());
   }
   
   /**
    * Checks cases with {@link AlwaysFalse} expression. 
    */
   public void testAlwaysFalse() throws Exception {
       runTest(new AlwaysFalse(), true, null);
   }
   
   /**
    * Checks typical cases for {@link Not} expression. 
    */
   public void testNot() throws Exception {
       runTest(new Not(new AlwaysTrue()), true, null);
       runTest(new Not(new AlwaysFalse()));
   }
}
