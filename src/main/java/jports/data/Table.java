package jports.data;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class Table implements ColumnSchema<TableRow, TableColumn> {

	private final ArrayList<TableColumn> columns = new ArrayList<>();
	private final ArrayList<TableRow> rows = new ArrayList<>();

	public int getColumnCount() {
		return columns.size();
	}

	public int getColumnOrdinal(String name) {
		for (int i = 0; i < columns.size(); i++) {
			if (name.equalsIgnoreCase(columns.get(i).getName()))
				return i;
		}
		return -1;
	}

	public int getRowCount() {
		return rows.size();
	}

	public List<TableColumn> getColumns() {
		return this.columns;
	}

	public List<TableRow> getRows() {
		return this.rows;
	}

	public TableRow addRow(Object... values) {
		TableRow row = new TableRow(this, values);
		rows.add(row);
		return row;
	}

	public TableColumn addColumn(String name, ColumnType columnType) {
		TableColumn column = new TableColumn(this, name, columnType);
		columns.add(column);
		return column;
	}

	public Predicate<TableRow> createPredicate(Filter filter) {
		switch (filter.getFilterType()) {
		case EXPRESSION:
			return createPredicate((FilterExpression) filter);
		case NODE:
			return createPredicate((FilterNode) filter);
		case TERM:
			return createPredicate((FilterTerm) filter);
		default:
			throw new RuntimeException("Can't build predicate for filters of type " + filter.getFilterType());

		}
	}

	public Predicate<TableRow> createPredicate(FilterExpression expression) {
		return createPredicate(expression.first);
	}

	public Predicate<TableRow> createPredicate(FilterNode node) {
		return new Predicate<TableRow>() {
			private final FilterNode n = node;
			private final Predicate<TableRow> filter = createPredicate(node.filter);
			private final Predicate<TableRow> next = node.next == null
					? null
					: createPredicate(node.next);

			@Override
			public boolean test(TableRow row) {
				if (next == null)
					return filter.test(row);
				else
					switch (n.operation) {
					case AND:
						return filter.test(row) && next.test(row);
					case OR:
						return filter.test(row) || next.test(row);
					default:
						throw new RuntimeException("Unknown filter operation " + n.operation);
					}
			}
		};
	}

	public Predicate<TableRow> createPredicate(FilterTerm term) {
		return new Predicate<TableRow>() {
			private final String name = term.name;
			private final Predicate<Object> valuePredicate = term.createValuePredicate();

			@Override
			public boolean test(TableRow row) {
				Object val = row.get(name);
				return valuePredicate.test(val);
			}
		};
	}

}
