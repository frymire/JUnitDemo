
package io.github.frymire;

import static org.junit.Assert.assertTrue;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.hamcrest.CoreMatchers.startsWith;

public class TestExceptions {

  // This passes, since the expected exception is thrown.
  @Test(expected = ArithmeticException.class) public void testPrintMessage() {

    // Cause the exception.
    System.out.println(1 / 0);

    // But this never gets executed.
    assertTrue(false);

  }

  //This will fail, since the expected exception wasn't thrown.
  @Test(expected = ArithmeticException.class) 
  public void noExceptionThrownUsingAnnotation() { assertTrue(true); }

  // This fails because an exception other than the expected one was thrown.
  @Test(expected = IllegalArgumentException.class)
  public void throwsDifferentExceptionThanExpected() { System.out.println(1 / 0); }


  // A Rule can be used as an alternative or addition to fixture setup and cleanup methods.
  // For the moment, build an ExpectedException to express that we don't expect any exceptions.
  @Rule public ExpectedException thrown = ExpectedException.none();

  // Extend the ExpectedException rule locally to verify that a specific exception is thrown. 
  @Test public void verifyExceptionTypeAndMessage() {

    // Add the expectations for an exception to be thrown. In addition to specifying the exception
    // type, you can match the whole string of the message, or pass a Hamcrest matcher. 
    thrown.expect(ArithmeticException.class);
    thrown.expectMessage("/ by zero");
    thrown.expectMessage(startsWith("/ by"));

    // This test will pass if we throw this expected exception.
    System.out.println(1 / 0);

  }

  // The test will fail if no exception is thrown, when it is expected.
  @Test public void noExceptionThrownUsingRule() {
    thrown.expect(ArithmeticException.class);
    thrown.reportMissingExceptionWithMessage("No exception of %s was thrown."); 
  }

}
