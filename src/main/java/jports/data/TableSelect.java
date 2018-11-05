package jports.data;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TableSelect extends Select<TableRow> {

	private final Table table;

	public TableSelect(Table table) {
		this.table = table;
	}

	@Override
	public List<TableRow> toList() {

		Stream<TableRow> stream = table.getRows().stream();

		FilterExpression filter = getFilter();
		if (filter != null) {
			stream = stream.filter(table.createPredicate(filter));
		}

		Sort sort = getSort();
		if (sort != null) {
			for (SortNode i = sort.first; i != null; i = i.next) {
				stream = stream.sorted(table.createComparator(i));
			}
		}

		final int offset = getOffset();

		if (offset > 0) {
			stream = stream.filter(new Predicate<TableRow>() {
				int i;

				@Override
				public boolean test(TableRow arg0) {
					return i++ > offset;
				}
			});
		}

		final int limit = getLimit();
		if (limit > 0)
			stream = stream.limit(limit);

		return stream.collect(Collectors.toList());
	}

	@Override
	public long count() {
		FilterExpression filter = getFilter();
		if (filter == null)
			return table.getRowCount();
		else
			return table
					.getRows()
					.stream()
					.filter(table.createPredicate(filter))
					.collect(Collectors.counting())
					.longValue();
	}

	@Override
	public TableRow first() {
		FilterExpression filter = getFilter();
		if (filter == null)
			return table.getRow(0);
		else
			return table
					.getRows()
					.stream()
					.filter(table.createPredicate(filter))
					.findAny()
					.get();
	}

}
