
package io.github.frymire;

import org.junit.Test;
import static org.junit.Assert.assertTrue;

public class TestTimeout {
  
  @Test(timeout=500)
  public void testLongMethod() throws InterruptedException { 
    System.out.println("Starting testLongMethod()...");
    Thread.sleep(600);
    assertTrue(true);
  }

}