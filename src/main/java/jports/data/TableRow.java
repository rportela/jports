package jports.data;

public class TableRow {

	public final Table table;
	public Object[] values;

	public TableRow(Table table, Object... values) {
		this.table = table;
		this.values = values;
	}
	
	

}
