// UNCLASSIFIED

package io.github.frymire;

import mockit.Expectations;

import org.junit.Test;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;


public class JMockitPartialMock {

  // Build a collaborator for demonstration purposes.  
  static class ValueHolder {

    final int value;

    ValueHolder() { value = -1; }
    ValueHolder(int value) { this.value = value; }

    int getValue() { return value; }
    final boolean simpleOperation(int a, String b) { return true; }
    static void doSomething(boolean b, String s) { throw new IllegalStateException(); }

  }  


  @Test public void testPartialMocking() {

    // Define an instance just to record expectations.
    final ValueHolder recorderInstance = new ValueHolder();

    // Partially mock a class by passing its class literal to the  
    // Expectations constructor. Here, only getValue() is mocked.
    new Expectations(ValueHolder.class) {{
      recorderInstance.getValue(); 
      result = 123;
    }};

    // No constructor expectations were recorded, so these are real
    // instances. (Note that we never used an @Mocked annotation.)
    ValueHolder c1 = new ValueHolder();
    ValueHolder c2 = new ValueHolder(150);

    // The mocked value is returned for getValue() calls.
    assertEquals(123, c1.getValue());
    assertEquals(123, c2.getValue());

    // All of the other methods use the actual class.
    assertTrue(c1.simpleOperation(1, "b"));
    assertEquals(45, new ValueHolder(45).value);

  }

  @Test public void testPartiallyMockingASingleInstance() {

    // Define an instance to record expectations.
    final ValueHolder mockedInstance = new ValueHolder(2);

    // Pass the specific instance as a parameters to the Expectations constructor
    // to partially mock it without mocking other instances of the class at all.
    new Expectations(mockedInstance) {{

      mockedInstance.getValue(); 
      result = 123;

      // Static methods can be dynamically mocked too.
      ValueHolder.doSomething(anyBoolean, "test");

    }};

    // The partially-mocked behaves according to recorded expectations.
    assertEquals(123, mockedInstance.getValue());
    ValueHolder.doSomething(true, "test");

    // The partially-mocked instance behaves like a real instance
    // for methods that weren't mocked.
    assertEquals(2, mockedInstance.value);

    // New instances behave like real instances.
    assertEquals(45, new ValueHolder(45).getValue());
    assertEquals(-1, new ValueHolder().getValue());

  }

}
