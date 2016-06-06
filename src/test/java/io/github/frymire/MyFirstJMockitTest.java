// UNCLASSIFIED

package io.github.frymire;

import mockit.Mocked;
import mockit.Injectable;
import org.junit.Test;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class MyFirstJMockitTest
{
  // @Mocked mocks all current and future instances of the class, while
  // @Injectable mocks one particular instance.
  @Mocked Collaborator mock1;
  @Injectable AnotherDependency anotherMock;

  @Test public void myFirstTestMethod() {
    // Any mock field can be used here or in any other test method of the class.
    assertTrue(true);
  }

  @Test public void testMethodWithMockParameter(@Mocked YetAnotherDependency testSpecificMock) {
    fail("Admit it, you suck.");
  }

}