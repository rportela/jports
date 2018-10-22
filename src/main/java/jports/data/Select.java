package jports.data;

import java.util.List;

public abstract class Select<TRow> extends Filterable<Select<TRow>> implements Iterable<TRow> {

	private Sort sort;

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

	public abstract List<TRow> toList();

}
