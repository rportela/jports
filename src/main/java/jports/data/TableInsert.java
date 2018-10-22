package jports.data;

public class TableInsert extends Insert<Table> {

	public TableInsert(Table target) {
		super(target);
	}

	@Override
	public void execute() {
		Table table = getTarget();
		Object[] row = new Object[table.getColumnCount()];
		for (int i = 0; i < row.length; i++) {
			String colName = table.getColumn(i).name;
			row[i] = super.get(colName);
		}
		table.addRow(row);
	}

}
