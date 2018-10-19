package jports.data;

import java.util.Map.Entry;
import java.util.stream.Stream;

import jports.Incrementer;

public class TableUpdate extends Update<Table> {

	public TableUpdate(Table target) {
		super(target);
	}

	@Override
	public int execute() {
		final Incrementer inc = new Incrementer();
		Stream<TableRow> stream = getTarget().getRows().parallelStream();
		FilterExpression expression = getFilter();
		if (expression != null) {
			stream = stream.filter(getTarget().createPredicate(expression));
		}
		stream.forEach(row -> {
			for (Entry<String, Object> e : this.getValues().entrySet()) {
				String name = e.getKey();
				Object v = e.getValue();
				row.set(name, v);
			}
			inc.increment();
		});
		return inc.value;
	}

}
