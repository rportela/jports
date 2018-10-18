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
		case TERM:
			return createPredicate((FilterTerm) filter);
		default:
			throw new RuntimeException("Can't build predicate for filters of type " + filter.getFilterType());

		}
	}

	public Predicate<TableRow> createPredicate(FilterExpression expression) {

	}

	public Predicate<TableRow> createPredicate(FilterTerm term) {
		return new Predicate<TableRow>() {
			private final FilterComparison comparison = term.comparison;
			private final int ordinal = getColumnOrdinal(term.name);
			private final Object value = term.value;

			@SuppressWarnings({ "unchecked", "rawtypes" })
			@Override
			public boolean test(TableRow row) {
				Object colValue = row.get(ordinal);
				switch (comparison) {
				case EQUAL_TO:
					return value == null
							? colValue == null
							: value.equals(colValue);
				case GRATER_OR_EQUAL:
					return colValue == null
							? value == null
							: ((Comparable) colValue).compareTo(value) >= 0;
				case GREATER_THAN:
					return colValue == null
							? false
							: ((Comparable) colValue).compareTo(value) > 0;
				case LIKE:
					break;
				case LOWER_OR_EQUAL:
					return colValue == null
							? value == null
							: ((Comparable) colValue).compareTo(value) <= 0;
				case LOWER_THAN:
					return colValue == null
							? false
							: ((Comparable) colValue).compareTo(value) < 0;
				case NOT_EQUAL_TO:
					return t.value == null
							? colValue != null
							: !value.equals(colValue);
				case NOT_LIKE:
					break;
				default:
					throw new RuntimeException("Unknown filter comparison: " + comparison);

				}
			}
		};
	}

}
