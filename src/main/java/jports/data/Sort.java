package jports.data;

public class Sort {

	public final SortNode first;
	private SortNode last;

	public Sort(String name, SortDirection direction) {
		this.first = new SortNode(name, direction);
		this.last = this.first;
	}

	public Sort thenOrderBy(String name, SortDirection direction) {
		this.last.next = new SortNode(name, direction);
		this.last = this.last.next;
		return this;
	}
}
