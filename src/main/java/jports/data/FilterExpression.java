package jports.data;

public class FilterExpression implements Filter {

	public final FilterNode first;
	private FilterNode last;

	public FilterExpression(FilterTerm term) {
		this.first = new FilterNode(term);
		this.last = this.first;
	}

	public FilterExpression(FilterExpression expression) {
		this.first = new FilterNode(expression);
		this.last = this.first;
	}

	public FilterExpression(String name, FilterComparison comparison, Object value) {
		this.first = new FilterNode(new FilterTerm(name, comparison, value));
		this.last = this.first;
	}

	public FilterExpression(String name, Object value) {
		this.first = new FilterNode(new FilterTerm(name, value));
		this.last = this.first;
	}

	public FilterExpression and(FilterTerm term) {
		this.last.setNext(FilterOperation.AND, term);
		this.last = this.last.next;
		return this;
	}

	public FilterExpression and(String name, FilterComparison comparison, Object value) {
		return and(new FilterTerm(name, comparison, value));
	}

	public FilterExpression and(String name, Object value) {
		return and(new FilterTerm(name, value));
	}

	public FilterExpression and(FilterExpression expression) {
		this.last.setNext(FilterOperation.AND, expression);
		this.last = this.last.next;
		return this;
	}

	public FilterExpression or(FilterTerm term) {
		this.last.setNext(FilterOperation.OR, term);
		this.last = this.last.next;
		return this;
	}

	public FilterExpression or(String name, FilterComparison comparison, Object value) {
		return or(new FilterTerm(name, comparison, value));
	}

	public FilterExpression or(String name, Object value) {
		return or(new FilterTerm(name, value));
	}

	public FilterExpression or(FilterExpression expression) {
		this.last.setNext(FilterOperation.OR, expression);
		this.last = this.last.next;
		return this;
	}

	@Override
	public final FilterType getFilterType() {
		return FilterType.EXPRESSION;
	}

}
