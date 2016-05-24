// UNCLASSIFIED

import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

// Create an executable main that runs tests using the JUnitCore facade.
public class TestRunner {

  // Define a method to print the result of a test.
  private static void report(Result result) {
    System.out.println();    
    for (Failure failure : result.getFailures()) { System.out.println(failure.toString()); }
    System.out.println("Test successful? -> " + result.wasSuccessful());
  }
  

  // Run some tests and print the results.
  public static void main(String[] args) {

    // Run individual test classes.
    Result testResult = JUnitCore.runClasses(TestAssertions.class);
//    Result result = JUnitCore.runClasses(TestMessageUtil.class);
    report(testResult);
    
    // Or run a test suite.
    Result suiteResult = JUnitCore.runClasses(AnnotatedSuite.class);
    report(suiteResult);
    
  }
  
}