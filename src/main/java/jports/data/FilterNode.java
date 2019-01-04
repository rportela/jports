package jports.data;

public class FilterNode implements Filter {

	private final Filter filter;
	private FilterOperation operation;
	private FilterNode next;

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

	/**
	 * @return the operation
	 */
	public FilterOperation getOperation() {
		return operation;
	}

	/**
	 * @param operation
	 *            the operation to set
	 */
	public FilterNode setOperation(FilterOperation operation) {
		this.operation = operation;
		return this;
	}

	/**
	 * @return the next
	 */
	public FilterNode getNext() {
		return next;
	}

	/**
	 * @return the filter
	 */
	public Filter getFilter() {
		return filter;
	}

}
