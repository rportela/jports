package jports.data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

public class TableSelect extends Select<TableRow> {

	private final Table table;

	public TableSelect(Table table) {
		this.table = table;
	}

	@Override
	public Iterator<TableRow> iterator() {
		return toList().iterator();
	}

	@Override
	public List<TableRow> toList() {

		FilterExpression filter = getFilter();

		Sort sort = getSort();

		if (filter == null && sort == null)
			return table.getRows();

		List<TableRow> list = filter == null
				? new ArrayList<>(table.getRows())
				: table
						.getRows()
						.parallelStream()
						.filter(table.createPredicate(filter))
						.collect(Collectors.toList());

		for (SortNode i = sort.first; i != null; i = i.next) {
			Collections.sort(list, table.createComparator(i));
		}

		return list;

	}

	@Override
	protected Select<TableRow> getThis() {
		return this;
	}

}
