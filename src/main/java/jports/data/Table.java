package jports.data;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import jports.ShowStopper;
import jports.adapters.Adapter;

public class Table implements ColumnSchema<TableColumn> {

	private final ArrayList<TableColumn> columns = new ArrayList<>();
	private final ArrayList<TableRow> rows = new ArrayList<>();
	private String name;

	public Table setName(String name) {
		this.name = name;
		return this;
	}

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
		rows.parallelStream().forEach(row -> row.set(ordinal, adapter.convert(row.get(ordinal))));
		return this;
	}

	public Table applyAdapter(final String columnName, final Adapter<?> adapter) {
		int ordinal = getColumnOrdinal(columnName);
		if (ordinal < 0)
			throw new ShowStopper("Column not found: " + columnName);
		else
			return applyAdapter(ordinal, adapter);
	}

	public Table applyParser(int ordinal, Adapter<?> adapter) {
		rows.parallelStream().forEach(row -> row.set(ordinal, adapter.parse((String) row.get(ordinal))));
		return this;
	}

	public Table applyParser(final String columnName, final Adapter<?> adapter) {
		int ordinal = getColumnOrdinal(columnName);
		if (ordinal < 0)
			throw new ShowStopper("Column not found: " + columnName);
		else
			return applyParser(ordinal, adapter);
	}

	public TableColumn addColumn(String name, ColumnType columnType) {
		TableColumn column = new TableColumn(this, name, columnType);
		columns.add(column);
		return column;
	}

	@Override
	public TableColumn getColumn(String name) {
		int ordinal = getColumnOrdinal(name);
		if (ordinal < 0)
			throw new ShowStopper("Column " + name + " was not found on this table: " + this);
		else
			return getColumn(ordinal);
	}

	@Override
	public TableColumn getIdentity() {
		return columns
				.stream()
				.filter(c -> c.getColumnType() == ColumnType.IDENTITY)
				.findFirst()
				.orElse(null);
	}

	@Override
	public List<TableColumn> getUniqueColumns() {
		return columns
				.stream()
				.filter(c -> c.getColumnType() == ColumnType.UNIQUE)
				.collect(Collectors.toList());
	}

	@Override
	public List<TableColumn> getCompositeKey() {
		return columns
				.stream()
				.filter(c -> c.getColumnType() == ColumnType.COMPOSITE_KEY)
				.collect(Collectors.toList());
	}

	@Override
	public String getName() {
		return this.name;
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
			throw new ShowStopper("Can't build predicate for filters of type " + filter.getFilterType());

		}
	}

	public Predicate<TableRow> createPredicate(FilterExpression expression) {
		return createPredicate(expression.first);
	}

	public Predicate<TableRow> createPredicate(FilterNode node) {
		return new Predicate<TableRow>() {
			private final FilterNode n = node;
			private final Predicate<TableRow> filter = createPredicate(node.getFilter());
			private final Predicate<TableRow> next = node.getNext() == null
					? null
					: createPredicate(node.getNext());

			@Override
			public boolean test(TableRow row) {
				if (next == null)
					return filter.test(row);
				else
					switch (n.getOperation()) {
					case AND:
						return filter.test(row) && next.test(row);
					case OR:
						return filter.test(row) || next.test(row);
					default:
						throw new ShowStopper("Unknown filter operation " + n.getOperation());
					}
			}
		};
	}

	public Predicate<TableRow> createPredicate(FilterTerm term) {
		return new Predicate<TableRow>() {
			private final Predicate<Object> valuePredicate = term.valuePredicate;

			@Override
			public boolean test(TableRow row) {
				Object val = row.get(term.getName());
				return valuePredicate.test(val);
			}
		};
	}

	public Comparator<TableRow> createComparator(SortNode sort) {

		switch (sort.getDirection()) {
		case ASCENDING:
			return new Comparator<TableRow>() {

				final int ordinal = getColumnOrdinal(sort.getName());

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

				final int ordinal = getColumnOrdinal(sort.getName());

				@SuppressWarnings({ "unchecked", "rawtypes" })
				@Override
				public int compare(TableRow arg0, TableRow arg1) {
					Object a = arg0.get(ordinal);
					Object b = arg1.get(ordinal);
					return a == null
							? 0
							: ((Comparable) b).compareTo(a);
				}
			};
		default:
			throw new ShowStopper("Unknown sort direction: " + sort.getDirection());
		}

	}

	public void loadCsv(InputStream is, Charset charset, String separator, boolean firstRowsHasNames,
			String commentQualifier) throws IOException {
		try (BufferedReader reader = new BufferedReader(new InputStreamReader(is, charset))) {
			String line;
			columns.clear();
			rows.clear();
			boolean hasQualifier = commentQualifier != null && !commentQualifier.isEmpty();
			while ((line = reader.readLine()) != null) {
				if (line.isEmpty() || (hasQualifier && line.startsWith(commentQualifier))) {
					// do nothing
				} else if (firstRowsHasNames) {
					firstRowsHasNames = false;
					String[] colNames = line.split(separator);
					for (int i = 0; i < colNames.length; i++) {
						this.columns.add(new TableColumn(this, colNames[i], ColumnType.REGULAR));
					}
				} else {
					this.addRow((Object[]) line.split(separator));
				}
			}
		}
	}

	public void loadCsv(InputStream is, Charset charset, String separator) throws IOException {
		loadCsv(is, charset, separator, true, null);
	}

	public void loadCsv(File file, Charset charset, String separator) throws IOException {
		try (FileInputStream fis = new FileInputStream(file)) {
			loadCsv(fis, charset, separator);
		}
	}

	public void loadCsv(URL url, Charset charset, String separator) throws IOException {
		try (InputStream uis = url.openStream()) {
			loadCsv(uis, charset, separator);
		}
	}

}
