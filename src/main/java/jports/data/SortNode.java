package jports.data;

public class SortNode {

	private final String name;
	private final SortDirection direction;
	private SortNode next;

	public SortNode(String name, SortDirection direction) {
		this.name = name;
		this.direction = direction;
	}

	/**
	 * @return the next
	 */
	public final SortNode getNext() {
		return next;
	}

	/**
	 * @param next
	 *            the next to set
	 */
	public final SortNode setNext(String name, SortDirection direction) {
		this.next = new SortNode(name, direction);
		return this;
	}

	/**
	 * @return the name
	 */
	public final String getName() {
		return name;
	}

	/**
	 * @return the direction
	 */
	public final SortDirection getDirection() {
		return direction;
	}

}
