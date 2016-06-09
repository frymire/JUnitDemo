// UNCLASSIFIED

package io.github.frymire;

import mockit.Mocked;
import mockit.Injectable;
import mockit.Expectations;
import mockit.StrictExpectations;
import mockit.Verifications;
import mockit.VerificationsInOrder;
import mockit.FullVerifications;
import mockit.FullVerificationsInOrder;

import org.junit.Test;

import static org.junit.Assert.assertEquals;


public class JMockitTest {

  // Define some dependencies.

  public class Adder {
    public Adder() {}
    public Adder(String msg) {}
    public int add(int a, int b) { return a + b; }
    public double add(double a, double b) { return a + b; }
  }  

  public class Talker {
    public String sayHi() { return "The real talker says hi."; }    
  }


  // Mock fields declared at the class level can be reused in any test.
  @Mocked Adder adder1;
  @Mocked Adder adder2;
  
  @Test public void testMultipleInvocations() {

    // Mock the (silly) behavior that add() always returns 10.
    new Expectations() {{      
      adder1.add(anyInt, anyInt);
      result = 10;                   
    }};

    // Verify the (wrong and silly) mocked behavior to show that the 
    // mock is being called, rather than real class instances. You can
    // call the same mocked invocation multiple times, even though it
    // was only defined once. These pass.
    assertEquals(10, adder1.add(1, 1));
    assertEquals(10, adder1.add(1, 1));

  }

  @Test public void testOrderIndependence() {

    // Mock responses to multiple specific add() calls.
    new Expectations() {{

      adder2.add(2, 3);
      result = 5;

      adder2.add(4, 5);
      result = 9;

    }};

    // You can call the mocked methods in any order. These pass.
    assertEquals(9, adder2.add(4, 5));
    assertEquals(5, adder2.add(2, 3));

  }

  @Test public void testStrictExpectations() {

    // Mock specific, ordered invocations and responses to add() calls.
    new StrictExpectations() {{

      adder2.add(2, 3);
      result = 5;

      adder2.add(4, 5);
      result = 9;

    }};

    // This fails on the call to add(4, 5), since it must be parameterized by (2,3).
    assertEquals(9, adder2.add(4, 5));

  }
  
  @Test public void testUncalledExpectations() {

    // If you define expectations that are never called, 
    // JMockit will throw a MissingInvocation error.
    new Expectations() {{      
      adder2.add(1, 1);
      result = 2;                   
    }};

  }
  
  @Test(expected = IllegalArgumentException.class) public void testThownException() {
    
    // Say that we expect an error to be thrown for a specific invocation.
    new Expectations() {{
      adder1.add(1, -1);
      result = new IllegalArgumentException(); 
    }};
    
    // This will pass, because the mock throws the exception.
    adder1.add(1, -1);
    
  }

  @Test public void testBehaviorOnAnyMockedInstance() {

    new Expectations() {{
      adder1.add(1, 1);
      result = 3;
    }};

    // Adder was declared with an @Mocked annotation, so all future Adder instances  
    // are automatically mocked as well. This add() call returns 0, rather than 3,
    // however, since the expectations were specified on adder1. This assert passes, 
    // but an MissingInvocation is thrown, since add() was never called on adder1.
    Adder anotherAdder = new Adder();
    assertEquals(0, anotherAdder.add(1, 1));
    
  }
  
  
  // @Injectable mocks one particular instance, so you can mix with real instances.
  Talker realTalker = new Talker();
  @Injectable Talker fakeTalker;
  //  @Mocked Talker fakeTalker; // Doesn't work.

  @Test public void testWithInjectedMock() {

    // Mock some behavior if someone calls sayHi() on the injectedTalker instance.
    new Expectations() {{
      fakeTalker.sayHi();
      result = "The mocked talker says hi.";
    }};

    assertEquals("The mocked talker says hi.", fakeTalker.sayHi());

    // If you used @Mocked instead of @Injected for the talker test double above, 
    // JMockit would have also mocked the realTalker instance, even though it was 
    // declared without any annotation. As a result, this test would have failed.
    assertEquals("The real talker says hi.", realTalker.sayHi());

  }

  // You can write mocked instances directly in test parameters. The JMockit author   
  // prefers this approach to reduce the risk of interactions among tests.
  @Test public void testInvocationsCountWithMockParameter(@Mocked final Adder localAdder) {

    // Require that add() is called twice.
    new Expectations() {{      
      localAdder.add(1, 1);
      result = 2;
      times = 2;
    }};

    // The test must call add() exactly twice.
    assertEquals(2, localAdder.add(1, 1));
    assertEquals(2, localAdder.add(1, 1)); // Fail without this.
    //    assertEquals(2, localAdder.add(1, 1)); // Fail with this.

  }

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
  

  @Test public void testFutureInstanceExpectationsWithOneRecorder(@Mocked final Adder anyAdder) {
    
    // To set expectations on all future instances of a mocked field, instantiate a 
    // new instance in the Expectations block to record behavior.
    new Expectations() {{
      Adder recorderAdder = new Adder();
      recorderAdder.add(2, 2);
      result = 5;
      recorderAdder.add(1, 1);
      result = 3;      
    }};
    
    // All new instances expect the recorded behavior. 
    
    Adder testAdder1 = new Adder();
    assertEquals(3, testAdder1.add(1, 1));
    assertEquals(5, testAdder1.add(2, 2));
    
    Adder testAdder2 = new Adder();
    assertEquals(3, testAdder2.add(1, 1));
    assertEquals(5, testAdder2.add(2, 2));
    
  }
  
  
  @Test public void testFutureInstanceExpectationsWithMultipeRecorders(
      @Mocked final Adder recorderAdder1,
      @Mocked final Adder recorderAdder2
      ) {
    
    // To set expectations on two different subsets of future instances of a mocked 
    // field, map separate recorder mock fields to new instances in the expectations
    // block, and record their behavior separately.
    new Expectations() {{      
      
      new Adder("Type A");
      result = recorderAdder1;
      recorderAdder1.add(2, 2);
      result = 5;
      
      new Adder("Type B");
      result = recorderAdder2;      
      recorderAdder2.add(2, 2);
      result = 6;      
      
    }};
    
    // Each new type of new instances expects the corresponding recorded behavior. 
    
    Adder testAdderA = new Adder("Type A");
    assertEquals(5, testAdderA.add(2, 2));
    
    Adder testAdderB = new Adder("Type B");
    assertEquals(6, testAdderB.add(2, 2));
    
  }  
  
  
  @Test public void testMockReturnSequence(@Mocked final Adder localAdder) {
    
    // Mock behavior that returns different values for subsequent invocations. 
    new Expectations() {{
      localAdder.add(anyInt, anyInt);
      returns(0, 1, 2);
      result = 3;
    }};
    
    assertEquals(0, localAdder.add(1,2));
    assertEquals(1, localAdder.add(3,4));
    assertEquals(2, localAdder.add(5,6));
    assertEquals(3, localAdder.add(7,8));
    
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

}
