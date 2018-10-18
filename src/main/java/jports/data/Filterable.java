package jports.data;

public abstract class Filterable<T> {

	protected abstract T getThis();

	private FilterExpression filter;

	public FilterExpression getFilter() {
		return this.filter;
	}

	public T where(FilterExpression expression) {
		this.filter = expression;
		return getThis();
	}

	public T where(FilterTerm term) {
		this.filter = new FilterExpression(term);
		return getThis();
	}

	public T where(String name, FilterComparison comparison, Object value) {
		return where(new FilterTerm(name, comparison, value));
	}

	public T where(String name, Object value) {
		return where(new FilterTerm(name, value));
	}

	public T andWhere(FilterExpression expression) {
		this.filter.and(expression);
		return getThis();
	}

	public T andWhere(FilterTerm term) {
		this.filter.and(term);
		return getThis();
	}

	public T andWhere(String name, FilterComparison comparison, Object value) {
		return andWhere(new FilterTerm(name, comparison, value));
	}

	public T andWhere(String name, Object value) {
		return andWhere(new FilterTerm(name, value));
	}

	public T orWhere(FilterExpression expression) {
		this.filter.or(expression);
		return getThis();
	}

	public T orWhere(FilterTerm term) {
		this.filter.or(term);
		return getThis();
	}

	public T orWhere(String name, FilterComparison comparison, Object value) {
		return orWhere(new FilterTerm(name, comparison, value));
	}

	public T orWhere(String name, Object value) {
		return orWhere(new FilterTerm(name, value));
	}

}
