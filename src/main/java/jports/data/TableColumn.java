package jports.data;

import java.util.Arrays;

public class TableColumn implements Column<TableRow> {

	public String name;
	public final int ordinal;
	public final Table table;
	public ColumnType columnType;

	protected TableColumn(Table table, int ordinal, String name) {
		this.table = table;
		this.ordinal = ordinal;
		this.name = name;
	}

	public String getName() {
		return this.name;
	}

	public Class<?> getDataType() {
		return Object.class;
	}

	public ColumnType getColumnType() {
		return this.columnType;
	}

	public Object getValue(TableRow row) {
		return row.values.length >= this.ordinal
				? null
				: row.values[this.ordinal];
	}

	public void setValue(TableRow row, Object value) {
		if (row.values.length <= this.ordinal) {
			row.values = Arrays.copyOf(row.values, table.getColumnCount());
		}
		row.values[this.ordinal] = value;
	}

}
