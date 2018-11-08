package jports.data;

import java.util.ArrayList;
import java.util.stream.Stream;

import jports.Incrementer;

public class TableUpdate extends Update {

	private final Table table;

	public TableUpdate(Table target) {
		this.table = target;
	}

	@Override
	public int execute() {
		final Incrementer inc = new Incrementer();
		final ArrayList<Integer> ordinals = new ArrayList<>(size());
		final ArrayList<Object> values = new ArrayList<>(size());
		final FilterExpression expression = getFilter();

		Stream<TableRow> stream = table.getRows().parallelStream();

		if (expression != null) {
			stream = stream.filter(table.createPredicate(expression));
		}

		getValues().entrySet().stream().forEach(entry -> {
			ordinals.add(table.getColumnOrdinal(entry.getKey()));
			values.add(entry.getValue());
		});

		stream.forEach(
				row -> {
					for (int i = 0; i < ordinals.size(); i++)
						row.set(ordinals.get(i), values.get(i));
					inc.increment();
				});

		return inc.getValue();
	}

}
