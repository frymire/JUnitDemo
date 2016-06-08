// UNCLASSIFIED

package io.github.frymire;

import mockit.Mocked;
import mockit.Injectable;
import mockit.Expectations;
import mockit.Verifications;
import mockit.VerificationsInOrder;

import org.junit.Test;

import static org.junit.Assert.assertEquals;


public class JMockitTest {

  // Define some dependencies.

  public class Adder {
    public int add(int a, int b) { return a + b; }
  }  

  public class Talker {
    public String sayHi() { return "The real talker says hi."; }    
  }


  // Mock fields declared at the class level can be reused in any test.
  // @Mocked mocks all current and future instances of the class.
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

  @Test public void testUncalledExpectations() {

    // If you define expectations that are never called, 
    // JMockit will throw a MissingInvocation error.
    new Expectations() {{      
      adder2.add(1, 1);
      result = 2;                   
    }};

  }


  // @Injectable mocks one particular instance. Use this if you need to mix 
  // and match real and mocked instances, for some strange reason.
  Talker realTalker = new Talker();
  @Injectable Talker fakeTalker;
  //  @Mocked Talker fakeTalker; // Doesn't work.

  @Test public void testWithInjectedMock() {

    // Mock some behavior if someone calls sayHi() on the injectedTalker instance.
    new Expectations() {{
      fakeTalker.sayHi();
      result = "The mocked talker says hi.";
    }};

    String mockedResult = fakeTalker.sayHi();
    assertEquals("The mocked talker says hi.", mockedResult);

    // If you used @Mocked instead of @Injected for the talker test double above, 
    // JMockit would have also mocked the realTalker instance, even though it was 
    // declared without any annotation. As a result, this test would have failed.
    String realResult = realTalker.sayHi();
    assertEquals("The real talker says hi.", realResult);

  }


  // You can write mocked instances directly in test parameters. The JMockit author   
  // prefers this approach to reduce the risk of interactions among tests. The field
  // can't be final, though, which seems odd.
  @Test public void testInvocationsCountWithMockParameter(@Mocked Adder localAdder) {

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

  @Test public void testVerifications(@Mocked Adder localAdder) {

    localAdder.add(1, 1);
    
    // Verify that the mocked adder was called as expected. You 
    // can invoke non-mocked types here, but it's not recommended.
    new Verifications() {{
      localAdder.add(anyInt, 1);
    }};    

  }
  
  @Test public void testVerifyNumberOfInvocations(@Mocked Adder localAdder) {
    
    localAdder.add(1, 2);
    localAdder.add(3, 4);
    localAdder.add(5, 6);
    
    // Verify that the mocked adder was called only twice. Fails.
    new Verifications() {{
      localAdder.add(anyInt, anyInt);
      maxTimes = 2;
    }};    

  }
  
  @Test public void testVerifyConstructor() {
    
    // Calling the Adder() constructor will cause the following verification 
    // to throw an UnexpectedInvocation exception.
    (new Adder()).add(1, 1);

    // Verify that no talker instance was ever constructed.
    new Verifications() {{
      new Adder();
      times = 0;          
    }};

  }
  
  @Test public void testVerifyInOrder(@Mocked Adder localAdder1, @Mocked Adder localAdder2) {
    
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
    // the order of each independently with a separate verifications block.
    new VerificationsInOrder() {{
      localAdder2.add(10, 11);
      localAdder2.add(12, 13);      
    }};
    
  }

}
