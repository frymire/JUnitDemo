
package io.github.frymire;

import org.junit.Test;

import static org.junit.Assert.*;

import org.junit.Ignore;


public class TestAssertions {

  // Define data for the test.
  String expected = new String ("abc");
  String actual = new String ("abc");
  String str3 = null;
  String str4 = "abc";
  String str5 = "abc";
  String[] array1 = {"one", "two", "three"};
  String[] array2 = {"one", "two", "three"};

  // Check that two objects are equal. Put the expected on the left and the actual on the right.
  @Test public void testEquals() { assertEquals(expected, actual); }

  // Check that a condition is true.
  @Test public void testTrue() { assertTrue (0 < 1); }

  // Check that a condition is false.
  @Test public void testFalse() { assertFalse(0 > 1); }

  // Check that a reference isn't null.
  @Test public void testNotNull() { assertNotNull(expected); }

  // Check that two references point to the same object.
  @Test public void testSame() { assertSame(str4, str5); }

  // Check that two references point to the same object.
  @Test public void testNotSame() { assertNotSame(expected, str3); }

  // Check that two arrays have the same contents.
  @Test public void testArrayEquals() { assertArrayEquals(array1, array2); }
  
  // Ignore a test
  @Ignore @Test public void testIgnore() { assertTrue(false); }
  
  // Force a failure
  @Test public void testFailure() { fail("Admit it, you suck."); }

}  
