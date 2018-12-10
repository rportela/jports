package jports;

/**
 * A very simple non thread save integer incrementer to return number of records
 * affected on actions;
 * 
 * @author rportela
 *
 */
public class Incrementer {

	private volatile int value;

	/**
	 * Increments the value wrapped by this incrementer;
	 * 
	 * @return
	 */
	public void increment() {
		value++;
	}

	public void increment(final int inc) {
		this.value += inc;
	}

	/**
	 * Gets the value wrapped by this incrementer;
	 * 
	 * @return
	 */
	public int getValue() {
		return this.value;
	}

	/**
	 * Sets the value wrapped by this incrementer;
	 * 
	 * @param value
	 */
	public void setValue(int value) {
		this.value = value;
	}

	/**
	 * Gets the value wrapped by this incrementer as a string;
	 */
	@Override
	public String toString() {
		return Integer.toString(value);
	}
}
