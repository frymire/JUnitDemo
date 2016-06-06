
package io.github.frymire;

import junit.framework.TestCase;

// You can use TestCase directly instead of annotations.
public class TestCaseTest extends TestCase {
  
   protected int value1, value2;
   
   // Assign values
   protected void setUp(){
      value1 = 3;
      value2 = 3;
   }

   // Test method to add two values
   public void testAdd(){
      double result= value1 + value2;
      assertTrue(result == 6);
   }
   
}