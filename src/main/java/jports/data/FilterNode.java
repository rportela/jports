package jports.data;

public class FilterNode implements Filter {

	public final Filter filter;
	public FilterOperation operation;
	public FilterNode next;

	public FilterNode(FilterTerm term) {
		this.filter = term;
	}

	public FilterNode(FilterExpression expression) {
		this.filter = expression;
	}

	public final void setNext(FilterOperation op, FilterTerm term) {
		this.operation = op;
		this.next = new FilterNode(term);
	}

	public final void setNext(FilterOperation op, FilterExpression expression) {
		this.operation = op;
		this.next = new FilterNode(expression);
	}

	@Override
	public final FilterType getFilterType() {
		return FilterType.NODE;
	}

}
