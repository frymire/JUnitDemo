// UNCLASSIFIED

package io.github.frymire;

import mockit.Mocked;
import mockit.Injectable;
import mockit.Expectations;

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

    // Verifying the (wrong and silly) mocked behavior to show that the 
    // mock is being called, rather than real class instances. You can
    // call the same mocked invocation multiple times, even though it
    // was only defined once. These pass.
    assertEquals(10, adder1.add(1, 1));
    assertEquals(10, adder1.add(1, 1));
    
  }
  
  @Test public void testOrderIndependence() {
    
    // Mock multiple responses to specific add() calls.
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
    
    // If you define expectations that are never called, the test will fail.
    new Expectations() {{      
      adder2.add(1, 1);
      result = 2;                   
    }};
        
  }


  // @Injectable mocks one particular instance. Use this if you need to mix 
  // and match real and mocked instances, for some strange reason.
  Talker realTalker = new Talker();
  @Injectable Talker injectedTalker;

  @Test public void testWithInjectedMock() {

    // Mock some behavior if someone calls sayHi() on the injectedTalker instance.
    new Expectations() {{
      injectedTalker.sayHi();
      result = "The mocked talker says hi.";
    }};

    String mockedResult = injectedTalker.sayHi();
    assertEquals("The mocked talker says hi.", mockedResult);

    // If you used @Mocked instead of @Injected for the talker test double above, 
    // JMockit would have also mocked the realTalker instance, even though it was 
    // declared without any annotation. As a result, this test would have failed.
    String realResult = realTalker.sayHi();
    assertEquals("The real talker says hi.", realResult);

  }


  // You can write mocked instances directly in test parameters, if that's more convenient.
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

}
