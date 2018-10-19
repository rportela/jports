package jports.data;

public class SortNode {

	public final String name;
	public final SortDirection direction;
	public SortNode next;

	public SortNode(String name, SortDirection direction) {
		this.name = name;
		this.direction = direction;
	}

}
