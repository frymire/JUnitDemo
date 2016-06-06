// UNCLASSIFIED

package io.github.frymire;

import java.util.Arrays;
import java.util.Collection;

import org.junit.Test;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.junit.runner.RunWith;
import static org.junit.Assert.assertTrue;

// Run a test against and array of test values.
@RunWith(Parameterized.class)
public class TestWithParameters {
  
  // Create a variable to store each subsequent value of the parameter set.
  private Integer testValue;

  // Define a constructor that populates the testValue before each test. 
  public TestWithParameters(Integer inputNumber) { this.testValue = inputNumber; }

  // Create an array of test values to be run in sequence.
  @Parameters 
  public static Collection<Object> values() { return Arrays.asList(new Object[] {1, 2, 3}); }
  
  
  // Define the test for each value. The first two will pass, but the last one will fail.
  @Test public void testIntegers() {
    System.out.println("Test value is: " + testValue);
    assertTrue(testValue < 3);
  }
  
}  