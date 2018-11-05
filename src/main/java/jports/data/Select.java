package jports.data;

import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;

public abstract class Select<TRow> extends Filterable<Select<TRow>> implements Iterable<TRow> {

	private Sort sort;
	private int offset;
	private int limit;

	public Sort getSort() {
		return this.sort;
	}

	public Select<TRow> orderBy(String name, SortDirection direction) {
		this.sort = new Sort(name, direction);
		return this;
	}

	public Select<TRow> orderBy(String name) {
		return orderBy(name, SortDirection.ASCENDING);
	}

	public Select<TRow> thenOrderBy(String name, SortDirection direction) {
		this.sort.thenOrderBy(name, direction);
		return this;
	}

	public Select<TRow> thenOrderBy(String name) {
		return thenOrderBy(name, SortDirection.ASCENDING);
	}

	public int getOffset() {
		return this.offset;
	}

	public Select<TRow> setOffset(int offset) {
		this.offset = offset;
		return this;
	}

	public int getLimit() {
		return this.limit;
	}

	public Select<TRow> setLimit(int limit) {
		this.limit = limit;
		return this;
	}

	public Select<TRow> setPage(int pageNumber, int pageSize) {
		this.offset = pageNumber * pageSize;
		this.limit = pageSize;
		return this;
	}

	@Override
	protected Select<TRow> getThis() {
		return this;
	}

	public Iterator<TRow> iterator() {
		return toList().iterator();
	}

	public Stream<TRow> stream() {
		return toList().stream();
	}

	public boolean exists() {
		return first() != null;
	}

	public abstract List<TRow> toList();

	public abstract long count();

	public abstract TRow first();

}
