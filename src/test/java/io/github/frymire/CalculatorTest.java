
package io.github.frymire;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import org.junit.rules.ExpectedException;

//import org.hamcrest.Description;
//import org.hamcrest.Matcher;
//import org.hamcrest.StringDescription;

//import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.equalTo;





/**
 * @author mark.e.frymire
 *
 */
public class CalculatorTest {
	
	Calculator c;

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		
		// Arrange...
		c = new Calculator();
		
	}
	

	@Test
	public void returns0uponInit() {
		assertEquals(c.getValue(), 0);
	}
	
	
	@Test
	public void add1returns1() {
		c.add(1);
		assertEquals(c.getValue(), 1);
	}
	
	
	
	@Test
	public void add1ResetReturns0() {
		c.add(1);
		c.reset();
		assertEquals(c.getValue(), 0);
	}
	
	
	@Test
	public void set4Returns4() {
		c.setValue(4);
		assertEquals(c.getValue(), 4);
	}
	
	
	@Test 
	public void set4DivideBy2returns2() {
		c.setValue(4);
		c.divideBy(2);
		assertEquals(c.getValue(), 2);
	}
	
	
	@Test 
	public void set4DivideBy2returns2_Hamcrest() {
		
		// Act ...
		c.setValue(4);
		c.divideBy(2);
		
		// Assert, this time with a Hamcrest matcher for improved readability...
//		assertThat(c.getValue(), is(equalTo(2)) );
		
		// ...or just...
		assertThat(c.getValue(), is(2));
	}
	

	// Here's how to verify that exceptions get thrown when they should be.
	
	@Test(expected = ArithmeticException.class)
	public void set4DivideBy0ThrowsArithmeticException() {
		c.setValue(4);
		c.divideBy(0);
	}
	
	
	@Rule
	public ExpectedException thrown = ExpectedException.none();
	
	
	@Test
	public void set4DivideBy0ThrowsArithmeticException_v2() {
		
		// Arrange...  <-- But this feels more like an assert. Awkward that it goes before "Act".
		thrown.expect(ArithmeticException.class);
		
		// We can also verify the error message.
//		thrown.expectMessage("divide by zero"); // Wrong message, fails the test.
//		thrown.expectMessage("by zero"); // Passes test, since it just looks for a substring of the message.
//		thrown.expectMessage("/ by zero");
		
		// Generally, Hamcrest matchers will make the asset more readable.
		thrown.expectMessage(equalTo("/ by zero"));
		
		// Act...
		c.setValue(4);
		c.divideBy(0);
		
	}
	

} // CalculatorTest
