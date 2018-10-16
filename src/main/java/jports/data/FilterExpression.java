package jports.data;

public class FilterExpression {

	public final FilterNode first;
	private FilterNode last;

	public FilterExpression(Filter filter) {
		this.first = new FilterNode(filter);
		this.last = this.first;
	}

	public FilterExpression and(Filter filter) {
		this.last.next = new FilterNode(filter);
		this.last.operation = FilterOperation.AND;
		this.last = this.last.next;
		return this;
	}
}
