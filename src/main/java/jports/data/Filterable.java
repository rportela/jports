package jports.data;

public abstract class Filterable<T> {

	protected abstract T getThis();

	private FilterExpression where;

	public FilterExpression getFilter() {
		return this.where;
	}

	public T where(FilterExpression expression) {
		this.where = new FilterExpression(expression);
		return getThis();
	}

	public T where(FilterTerm term) {
		this.where = new FilterExpression(term);
		return getThis();
	}

	public T where(String name, FilterComparison comparison, Object value) {
		return where(new FilterTerm(name, comparison, value));
	}

	public T where(String name, Object value) {
		return where(new FilterTerm(name, value));
	}

	public T andWhere(FilterExpression expression) {
		this.where = this.where == null ? new FilterExpression(expression) : this.where.and(expression);
		return getThis();
	}

	public T andWhere(FilterTerm term) {
		this.where = this.where == null ? new FilterExpression(term) : this.where.and(term);
		return getThis();
	}

	public T andWhere(String name, FilterComparison comparison, Object value) {
		return andWhere(new FilterTerm(name, comparison, value));
	}

	public T andWhere(String name, Object value) {
		return andWhere(new FilterTerm(name, value));
	}

	public T orWhere(FilterExpression expression) {
		this.where = this.where == null ? new FilterExpression(expression) : this.where.or(expression);
		return getThis();
	}

	public T orWhere(FilterTerm term) {
		this.where = this.where == null ? new FilterExpression(term) : this.where.or(term);
		return getThis();
	}

	public T orWhere(String name, FilterComparison comparison, Object value) {
		return orWhere(new FilterTerm(name, comparison, value));
	}

	public T orWhere(String name, Object value) {
		return orWhere(new FilterTerm(name, value));
	}

}
