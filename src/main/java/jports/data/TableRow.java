package jports.data;

public class TableRow {

	public final Table table;
	public Object[] values;

	public TableRow(Table table, Object... values) {
		this.table = table;
		this.values = values;
	}

	public Object get(int ordinal) {
		return ordinal < 0 || ordinal >= values.length
				? null
				: values[ordinal];
	}

	public Object get(String name) {
		return get(table.getColumnOrdinal(name));
	}

	public void set(int ordinal, Object value) {
		if (ordinal < 0 || ordinal >= table.getColumnCount()) {
			throw new RuntimeException(
					"Invalid column ordinal: " +
							ordinal +
							". Range is [0," +
							table.getColumnCount() + "[");
		} else
			values[ordinal] = value;
	}

	public void set(String name, Object value) {
		set(table.getColumnOrdinal(name), value);
	}

}
