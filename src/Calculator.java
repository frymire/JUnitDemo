/**
 * @author mark.e.frymire
 *
 */
public class Calculator {
	
	private int val = 0;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		System.out.println("Hi.");
	}

	public int getValue() {
		return val;
	}

	public void setValue(int i) {
		val = i;		
	}

	public void reset() {
		val = 0;		
	}

	public void add(int i) {
		val += i;		
	}

	public void divideBy(int i) {
		val /= i;		
	}

}
