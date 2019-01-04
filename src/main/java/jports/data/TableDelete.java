package jports.data;

import java.util.ArrayList;
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
			inc.setValue(rows.size());
			rows.clear();
		} else {
			Predicate<TableRow> predicate = table.createPredicate(filter);
			List<Integer> indices = new ArrayList<>(rows.size());
			for (int i = 0; i < rows.size(); i++)
				if (predicate.test(rows.get(i)))
					indices.add(i);

			for (Integer i : indices) {
				rows.remove(i.intValue());
				inc.increment();
			}
		}
		return inc.getValue();
	}

}
