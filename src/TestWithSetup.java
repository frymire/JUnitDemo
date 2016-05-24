// UNCLASSIFIED

import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;

public class TestWithSetup extends TestCase  {
  
   protected double fValue1;
   protected double fValue2;
   
   @Before 
   public void setUp() {
      fValue1 = 2.0;
      fValue2 = 3.0;
   }
  
   @Test
   public void testAdd() {
     
      // Use the countTestCases() method of TestCase to report the number of test cases.
      System.out.println("Test Case #"+ countTestCases());
    
      // Use the getName() method of TestCase to get the name of this test.  
      String name= getName();
      System.out.println("Test Case Name = "+ name);

      // Use the setName() method of TestCase to change the name of this test.
      setName("testNewAdd");
      String newName= getName();
      System.out.println("Updated Test Case Name = "+ newName);
      
   }
   
   // TearDown is used to close the connection or clean up activities.
   public void tearDown(  ) {  }
   
}