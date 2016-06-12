// UNCLASSIFIED

package io.github.frymire;

import mockit.Tested;
import mockit.Injectable;
import mockit.Expectations;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

public class JMockitTestClassInjection {

  // We want different test class instantiations for each test. @Tested says to   
  // fill in constructor fields with any available @Injectable fields, either at     
  // the class level or as a parameter for each test. 
  @Tested TestMe testInstance;
  @Injectable Adder adder;

  // Instantiate a test instance using the common adder with test-specific flag and name settings.
  @Test public void testInjectionForMary(
      @Injectable("true") boolean flag,
      @Injectable("Mary") String name) {
    
    new Expectations() {{
      adder.add(anyInt, anyInt);
      result = 1;
    }};
    
    assertEquals("Mary", testInstance.getName());
    assertTrue(testInstance.getFlag());
    assertEquals(1, testInstance.add(2, 3));
    
  }

  // Instantiate a test instance again with the common adder, but 
  // with different values for the flag and name parameters.
  @Test public void testInjectionForPeter(
      @Injectable("false") boolean flag,
      @Injectable("Peter") String name) {
    
    new Expectations() {{
      adder.add(anyInt, anyInt);
      result = 1;
    }};
    
    assertEquals("Peter", testInstance.getName());
    assertFalse(testInstance.getFlag());
    assertEquals(1, testInstance.add(2, 3));
    
  }
  
}
