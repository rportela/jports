package jports.data;

public class FilterNode implements Filter {

	public final Filter filter;
	public FilterOperation operation;
	public FilterNode next;

	public FilterNode(Filter filter) {
		this.filter = filter;
	}

	public FilterNode setOperation(FilterOperation op) {
		this.operation = op;
		return this;
	}

	public FilterNode setNext(Filter filter) {
		this.next = new FilterNode(filter);
		return this;
	}

}
