package jports.data;

import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;

public abstract class Select<T> extends Filterable<Select<T>> implements Iterable<T> {

	private Sort sort;
	private int offset;
	private int limit;

	public Sort getSort() {
		return this.sort;
	}

	public Select<T> orderBy(String name, SortDirection direction) {
		this.sort = new Sort(name, direction);
		return this;
	}

	public Select<T> orderBy(String name) {
		return orderBy(name, SortDirection.ASCENDING);
	}

	public Select<T> thenOrderBy(String name, SortDirection direction) {
		this.sort.thenOrderBy(name, direction);
		return this;
	}

	public Select<T> thenOrderBy(String name) {
		return thenOrderBy(name, SortDirection.ASCENDING);
	}

	public int getOffset() {
		return this.offset;
	}

	public Select<T> setOffset(int offset) {
		this.offset = offset;
		return this;
	}

	public int getLimit() {
		return this.limit;
	}

	public Select<T> setLimit(int limit) {
		this.limit = limit;
		return this;
	}

	public Select<T> setPage(int pageNumber, int pageSize) {
		this.offset = pageNumber * pageSize;
		this.limit = pageSize;
		return this;
	}

	@Override
	protected Select<T> getThis() {
		return this;
	}

	public Iterator<T> iterator() {
		return toList().iterator();
	}

	public Stream<T> stream() {
		return toList().stream();
	}

	public boolean exists() {
		return first() != null;
	}

	public abstract List<T> toList();

	public abstract long count();

	public abstract T first();

}
