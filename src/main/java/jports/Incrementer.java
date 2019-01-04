package jports;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * A very simple non thread save integer incrementer to return number of records
 * affected on actions;
 * 
 * @author rportela
 *
 */
public class Incrementer {

	private final AtomicInteger value = new AtomicInteger();

	/**
	 * Increments the value wrapped by this incrementer;
	 * 
	 * @return
	 */
	public int increment() {
		return value.incrementAndGet();
	}

	public int increment(final int inc) {
		return value.addAndGet(inc);
	}

	/**
	 * Gets the value wrapped by this incrementer;
	 * 
	 * @return
	 */
	public int getValue() {
		return value.get();
	}

	/**
	 * Sets the value wrapped by this incrementer;
	 * 
	 * @param value
	 */
	public void setValue(int value) {
		this.value.set(value);
	}

	/**
	 * Gets the value wrapped by this incrementer as a string;
	 */
	@Override
	public String toString() {
		return value.toString();
	}
}
