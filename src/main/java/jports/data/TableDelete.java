package jports.data;

import java.util.List;
import java.util.function.Predicate;

import jports.Incrementer;

public class TableDelete extends Delete<Table> {

	public TableDelete(Table target) {
		super(target);
	}

	@Override
	public int execute() {
		Table table = getTarget();
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
