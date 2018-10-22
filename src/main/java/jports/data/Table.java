package jports.data;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;

import jports.adapters.Adapter;

public class Table implements ColumnSchema<TableRow, TableColumn> {

	private final ArrayList<TableColumn> columns = new ArrayList<>();
	private final ArrayList<TableRow> rows = new ArrayList<>();

	public int getColumnCount() {
		return columns.size();
	}

	public int getColumnOrdinal(String name) {
		for (int i = 0; i < columns.size(); i++) {
			if (name.equalsIgnoreCase(columns.get(i).getColumnName()))
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

	public TableColumn getColumn(int ordinal) {
		return this.columns.get(ordinal);
	}

	public List<TableRow> getRows() {
		return this.rows;
	}

	public TableRow getRow(int ordinal) {
		return this.rows.get(ordinal);
	}

	public TableRow addRow(Object... values) {
		TableRow row = new TableRow(this, values);
		rows.add(row);
		return row;
	}

	public Table ensureCapacity(int minCapacity) {
		this.rows.ensureCapacity(minCapacity);
		return this;
	}

	public Table applyAdapter(final int ordinal, final Adapter<?> adapter) {
		rows.parallelStream().forEach(row -> {
			row.values[ordinal] = adapter.convert(row.values[ordinal]);
		});
		return this;
	}

	public Table applyAdapter(final String columnName, final Adapter<?> adapter) {
		int ordinal = getColumnOrdinal(columnName);
		if (ordinal < 0)
			throw new RuntimeException("Column not found: " + columnName);
		else
			return applyAdapter(ordinal, adapter);
	}

	public Table applyParser(int ordinal, Adapter<?> adapter) {
		rows.parallelStream().forEach(row -> {
			row.values[ordinal] = adapter.parse((String) row.values[ordinal]);
		});
		return this;
	}

	public Table applyParser(final String columnName, final Adapter<?> adapter) {
		int ordinal = getColumnOrdinal(columnName);
		if (ordinal < 0)
			throw new RuntimeException("Column not found: " + columnName);
		else
			return applyParser(ordinal, adapter);
	}

	public TableColumn addColumn(String name, ColumnType columnType) {
		TableColumn column = new TableColumn(this, name, columnType);
		columns.add(column);
		return column;
	}

	public TableDelete delete() {
		return new TableDelete(this);
	}

	public TableInsert insert() {
		return new TableInsert(this);
	}

	public TableUpdate update() {
		return new TableUpdate(this);
	}

	public TableSelect select() {
		return new TableSelect(this);
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

	public Comparator<TableRow> createComparator(SortNode sort) {

		switch (sort.direction) {
		case ASCENDING:
			return new Comparator<TableRow>() {

				final int ordinal = getColumnOrdinal(sort.name);

				@SuppressWarnings({ "unchecked", "rawtypes" })
				@Override
				public int compare(TableRow arg0, TableRow arg1) {
					Object a = arg0.get(ordinal);
					Object b = arg1.get(ordinal);
					return a == null
							? 0
							: ((Comparable) a).compareTo(b);
				}
			};
		case DESCENDING:
			return new Comparator<TableRow>() {

				final int ordinal = getColumnOrdinal(sort.name);

				@SuppressWarnings({ "unchecked", "rawtypes" })
				@Override
				public int compare(TableRow arg0, TableRow arg1) {
					Object a = arg0.get(ordinal);
					Object b = arg1.get(ordinal);
					return a == null
							? 0
							: -((Comparable) a).compareTo(b);
				}
			};
		default:
			throw new RuntimeException("Unknown sort direction: " + sort.direction);
		}

	}

	public void loadCsv(InputStream is, Charset charset, String separator, boolean firstRowsHasNames,
			String commentQualifier) throws IOException {
		try (BufferedReader reader = new BufferedReader(new InputStreamReader(is, charset))) {
			String line;
			columns.clear();
			rows.clear();
			while ((line = reader.readLine()) != null) {
				if (line.isEmpty())
					continue;
				if (commentQualifier != null && line.startsWith(commentQualifier))
					continue;
				if (firstRowsHasNames) {
					firstRowsHasNames = false;
					String[] colNames = line.split(separator);
					for (int i = 0; i < colNames.length; i++) {
						this.columns.add(new TableColumn(
								this,
								colNames[i],
								ColumnType.REGULAR));
					}
				} else {
					this.addRow((Object[]) line.split(separator));
				}
			}
		}
	}
}
