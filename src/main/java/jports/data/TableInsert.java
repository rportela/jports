package jports.data;

import java.util.HashMap;
import java.util.Map;

import jports.ShowStopper;

public class TableInsert extends Insert {

	private final Table table;

	public TableInsert(Table target) {
		this.table = target;
	}

	@Override
	public int execute() {
		Object[] row = new Object[table.getColumnCount()];
		for (int i = 0; i < row.length; i++) {
			String colName = table.getColumn(i).getColumnName();
			row[i] = super.get(colName);
		}
		table.addRow(row);
		return 1;
	}

	@Override
	public Map<String, Object> executeWithGeneratedKeys() {
		TableColumn identity = table.getIdentity();
		if (identity == null)
			throw new ShowStopper("The underlying table has no identity column: " + table);
		else {
			execute();
			HashMap<String, Object> map = new HashMap<>();
			map.put(identity.getColumnName(), table.getRowCount());
			return map;
		}
	}

}
