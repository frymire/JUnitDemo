// UNCLASSIFIED

package io.github.frymire;

import mockit.Tested;
import mockit.Injectable;
import mockit.Expectations;
import mockit.Verifications;

import org.junit.Test;
import org.junit.Before;
import org.junit.After;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

public class JMockitTestClassInjection {

  // We want different test class instantiations for each test. @Tested says to   
  // fill in constructor fields with any available @Injectable fields, either at     
  // the class level or as a parameter for each test. 
  @Tested TestMe testInstance;
  @Injectable Adder adder;
  @Injectable Talker talker;

  // It is convenient to put shared expectations and verifications 
  // in separate classes annotated with @Before and @After.
  @Before public void setAdderExpectations() {    
    new Expectations() {{ adder.add(anyInt, anyInt); result = 1; }};        
  }
  
  @After public void setAdderVerifications() {
    new Verifications() {{ adder.add(2, anyInt); }};
  }
  
  // Alternatively, you can extend Expectations or Verifications directly.
  final class TalkerVerifications extends Verifications { 
    TalkerVerifications() { talker.sayHi(); times = 0; }
  }    
  @After public void setTalkerVerifications() { new TalkerVerifications(); }

  
  // Now, instantiate a test instance using the common adder with test-specific flag and name settings.
  @Test public void testInjectionForMary(@Injectable("true") boolean flag, @Injectable("Mary") String name) {
    assertEquals("Mary", testInstance.getName());
    assertTrue(testInstance.getFlag());
    assertEquals(1, testInstance.add(2, 3));    
  }
  
  // Instantiate a test instance again with the common adder, but with different values for the flag and  
  // name parameters. These tests read cleanly, because common parts of the test have been factored out.
  @Test public void testInjectionForPeter(@Injectable("false") boolean flag, @Injectable("Peter") String name) {    
    assertEquals("Peter", testInstance.getName());
    assertFalse(testInstance.getFlag());
    assertEquals(1, testInstance.add(2, 3));    
  }
  
}
