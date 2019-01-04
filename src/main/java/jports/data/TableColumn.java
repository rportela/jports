package jports.data;

public class TableColumn implements Column {

	private String name;
	private final Table table;
	private ColumnType columnType;

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

	public Table getTable() {
		return this.table;
	}

}
