package jports.data;

public class TableColumn implements Column<TableRow> {

	public String name;
	public final Table table;
	public ColumnType columnType;

	protected TableColumn(Table table, String name, ColumnType columnType) {
		this.table = table;
		this.name = name;
		this.columnType = columnType;
	}

	public String getColumnName() {
		return this.name;
	}

	public Class<?> getDataType() {
		return Object.class;
	}

	public ColumnType getColumnType() {
		return this.columnType;
	}

	@Override
	public Object getValue(TableRow row) {
		return row.get(this.name);
	}

	@Override
	public void setValue(TableRow row, Object value) {
		row.set(this.name, value);
	}

}
