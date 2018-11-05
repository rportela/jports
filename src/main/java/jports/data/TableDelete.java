package jports.data;

import java.util.List;
import java.util.function.Predicate;

import jports.Incrementer;

public class TableDelete extends Delete {

	private final Table table;

	public TableDelete(Table target) {
		this.table = target;
	}

	@Override
	public int execute() {
		List<TableRow> rows = table.getRows();
		Incrementer inc = new Incrementer();
		FilterExpression filter = getFilter();
		if (filter == null) {
			inc.value = rows.size();
			rows.clear();
		} else {
			Predicate<TableRow> predicate = table.createPredicate(filter);
			for (int i = 0; i < rows.size(); i++) {
				TableRow row = rows.get(i);
				if (predicate.test(row)) {
					rows.remove(i);
					i--;
					inc.increment();
				}
			}
		}
		return inc.value;
	}

}
