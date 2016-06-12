// UNCLASSIFIED

package io.github.frymire;

import org.junit.Test;
import mockit.Capturing;
import mockit.Expectations;

import static org.junit.Assert.assertEquals;

public class JMockitCapturing {

  // Define an interface for which we'll mock all extending classes and instances.
  public interface ReturnsValue { int getValue(); }  

  // Define a class that implements the interface
  final class ValueOf1 implements ReturnsValue { public int getValue() { return 1; } }

  // We want to test this class, while mocking its private value fields.
  public final class ValueAdder {
    private final ReturnsValue value1 = new ValueOf1();
    private final ReturnsValue value2 = new ReturnsValue() { public int getValue() { return 2; } };
    public int getValueSum() { return value1.getValue() + value2.getValue(); }
  }  


  // Use @Capturing to mock all implementing classes of an interface.
  @Test public void testMockingImplementationClassesFromBaseTypes(@Capturing ReturnsValue anyService) {

    new Expectations() {{
      anyService.getValue(); 
      returns(3, 4); 
    }};

    // Though the value adder instance is real, the internal ReturnsValue instances are  
    // mocked, and behave according to the specified expectations (returning 7 instead of 3).
    assertEquals(7, new ValueAdder().getValueSum());

  }


  @Test public void testWithDifferentBehaviorForFirstNewInstanceAndRemainingNewInstances(
      @Capturing(maxInstances = 1) final ReturnsValue firstValueReturner,
      @Capturing final ReturnsValue remainingValueReturners) {


    // Set expectations for the first new instance, and all subsequent instances.
    new Expectations() {{
      firstValueReturner.getValue(); 
      result = 10;
      remainingValueReturners.getValue();
      result = 20;
    }};

    // Create instances that extend ReturnValue
    ReturnsValue v1 = new ReturnsValue() { public int getValue() { return 1; } };
    ReturnsValue v2 = new ReturnsValue() { public int getValue() { return 2; } };
    ReturnsValue v3 = new ReturnsValue() { public int getValue() { return 3; } };

    // The first is mocked as the firstValueReturner, while 
    // the second two are mocked as remainingValueReturners.
    assertEquals(10, v1.getValue());
    assertEquals(20, v2.getValue());
    assertEquals(20, v3.getValue());
    
  }

}
