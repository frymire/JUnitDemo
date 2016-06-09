// UNCLASSIFIED

package io.github.frymire;

import java.util.List;
import java.util.ArrayList;

import mockit.FullVerifications;
import mockit.FullVerificationsInOrder;
import mockit.Mocked;
import mockit.StrictExpectations;
import mockit.Verifications;
import mockit.VerificationsInOrder;

import org.junit.Test;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

public class JMockitVerifications {

  @Test public void testVerifications(@Mocked final Adder localAdder) {

    localAdder.add(1, 1);
    
    // Verify that the mocked adder was called as expected. Also, check
    // that it wasn't invoked using unexpected parameters. You can invoke 
    // non-mocked types here, but it's not recommended.
    new Verifications() {{
      localAdder.add(anyInt, 1);
      times = 1;
      localAdder.add(anyDouble, anyDouble);
      times = 0;
    }};    

  }
  
  @Test public void testVerifyNumberOfInvocations(@Mocked final Adder localAdder) {
    
    localAdder.add(1, 2);
    localAdder.add(3, 4);
    localAdder.add(5, 6);
    
    // Verify that the mocked adder was called only once or twice. Fails.
    new Verifications() {{
      localAdder.add(anyInt, anyInt);
      minTimes = 1;
      maxTimes = 2;
    }};    

  }

  @Test public void testVerifyConstructor() {
    
    // Calling the Adder() constructor will cause the following verification 
    // to throw an UnexpectedInvocation exception.
    new Adder().add(1, 1);

    // Verify that no talker instance was ever constructed. This fails.
    new Verifications() {{
      new Adder();
      times = 0;          
    }};

  }
  
  @Test public void testVerifyInOrder(
      @Mocked final Adder localAdder1, 
      @Mocked final Adder localAdder2) {
    
    localAdder1.add(1, 2);
    localAdder2.add(10, 11);
    localAdder1.add(3, 4);
    localAdder2.add(12, 13);
    
    // This will fail if you change the verification order.
    new VerificationsInOrder() {{
      localAdder1.add(1, 2);
      localAdder1.add(3, 4);      
//      localAdder.add(1, 2);
    }};
    
    // Even though the calls to the adders were interleaved, you can check  
    // the verify other calls independently with a separate verifications block.
    // In this case, we don't care about order.
    new Verifications() {{
      localAdder2.add(12, 13);
      localAdder2.add(10, 11);
    }};
    
  }
  
  @Test public void testFullVerification(@Mocked final Adder localAdder) {
    
    localAdder.add(1, 1);
    localAdder.add(2, 2);
    localAdder.add(3, 3);
    
    // Use full verifications to test that no unexpected calls were   
    // made. Order doesn't matter. This fails for add(3,3).
    new FullVerifications() {{
      localAdder.add(2, 2);
      localAdder.add(1, 1);
    }};
    
  }
  
  @Test public void testFullVerificationSimplified(@Mocked final Adder localAdder) {
    
    localAdder.add(1, 1);
    localAdder.add(2, 2);
    localAdder.add(3, 3);
    
    // This passes, because it covers all three cases.
    new FullVerifications() {{
      localAdder.add(anyInt, anyInt);
    }};    
    
  }
  
  @Test public void testFullVerificationInOrder(@Mocked final Adder localAdder) {
    
    localAdder.add(1, 1);
    localAdder.add(2, 2);
    localAdder.add(3, 3);
    
    // For full *ordered* verifications, you can no longer match 
    // a single expectation to all calls. This would fail.
//    new FullVerificationsInOrder() {{
//      localAdder.add(anyInt, anyInt);
//    }};
    
    // Instead, you must list each call, so that order can be checked.
    new FullVerificationsInOrder() {{
      localAdder.add(1, 1);
      localAdder.add(2, 2);
      localAdder.add(3, 3);
    }};
    
  }
  
  @Test public void testFullVerificationOnDifferentInstances(
      @Mocked final Adder localAdder1,
      @Mocked final Adder localAdder2,
      @Mocked final Talker localTalker) {
    
    localAdder1.add(1, 1);
    localAdder1.add(2, 2);
    localAdder2.add(3, 3);
    
    localTalker.sayHi();
    
    // Passing a specific mocked instance to the constructor limits the scope of the
    // verification. This would otherwise fail for the call to localAdder2.
    new FullVerifications(localAdder1) {{
      localAdder1.add(1, 1);
      localAdder1.add(2, 2);
    }};    

    // Pass the class literal to limit the verification to that class.
    new FullVerifications(Talker.class) {{
      localTalker.sayHi();      
    }};

  }
  
  @Test public void testVerifyNoInvocations(
      @Mocked final Adder localAdder1,
      @Mocked final Adder localAdder2) {
    
    localAdder1.add(1, 1);
    
    // Use an empty full verifications block to verify that no calls 
    // were ever made on a specific mocked instance.  
    new FullVerifications(localAdder2) {};
    
  }
 
  @Test public void testParameterMatching(@Mocked final Adder localAdder) {
    
    // Require a method to be called with a particular parameters.
    new StrictExpectations() {{
      new Adder(withSubstring("str"));
      new Adder(withPrefix("Good"));
      new Adder(withSuffix("morning."));
      new Adder(withNotEqual("Not equal."));
      new Adder(withMatch("[Regx]*"));
      new Adder(withNotNull());
      new Adder(withNull());
      new Adder(withArgThat( org.hamcrest.CoreMatchers.isA(String.class) ));
      localAdder.add(withEqual(2.0, 0.1), withEqual(3.0, 0.1));
      result = 5.0;
    }};
        
    new Adder("substring");
    new Adder("Good morning.");
    new Adder("Good morning.");
    new Adder("Equal.");
    new Adder("Regex");
    new Adder("This isn't null.");
    new Adder(null);
    new Adder("A string.");
    assertEquals(5.0, localAdder.add(1.95, 3.05), 0.00001);
    
  }
  
  @Test public void testCapturingArguments(@Mocked final Adder localAdder) {
    
    localAdder.add(1, 1);

    // Capture the method arguments and test something specific about them.
    new Verifications() {{
      int first, second;
      localAdder.add(first = withCapture(), second = withCapture());
      assertTrue(first == second);
    }};
    
  }
  
  @Test public void testCapturingArgumentSequences(@Mocked final Adder localAdder) {
    
    localAdder.add(0, 1);
    localAdder.add(1, 1);
    localAdder.add(2, 1);

    // Capture a sequence method arguments as lists and verify their contents.
    new Verifications() {{
      
      List<Integer> first = new ArrayList<Integer>();
      List<Integer> second = new ArrayList<Integer>();      
      localAdder.add(withCapture(first), withCapture(second));
      
      // Unfortunately, you can't run loops inside a Verifications() object.
      assertTrue(first.get(0) == 0);
      assertTrue(first.get(1) == 1);
      assertTrue(first.get(2) == 2);
      assertTrue(second.get(0) == 1);
      assertTrue(second.get(1) == 1);
      assertTrue(second.get(2) == 1);
      
    }};
    
  }
    
}
