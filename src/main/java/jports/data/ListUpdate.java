package jports.data;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.stream.Stream;

import jports.Incrementer;

public class ListUpdate<T> extends Update {

	private final ListStorage<T> storage;

	public ListUpdate(ListStorage<T> storage) {
		this.storage = storage;
	}

	@Override
	public int execute() {
		final DataAspect<T, ?> aspect = storage.getAspect();
		final List<Integer> indices = new ArrayList<>(super.size());
		final List<Object> values = new ArrayList<>(super.size());
		final Incrementer inc = new Incrementer();
		for (Entry<String, Object> entry : getValues().entrySet()) {
			indices.add(aspect.getColumnOrdinal(entry.getKey()));
			values.add(entry.getValue());
		}
		Stream<T> stream = storage.all().stream();
		FilterExpression filter2 = getFilter();
		if (filter2 != null)
			stream = stream.filter(aspect.createFilter(filter2));

		stream.forEach(e -> {
			for (int i = 0; i < indices.size(); i++) {
				aspect.get(indices.get(i)).setValue(e, values.get(i));
			}
			inc.increment();
		});
		return inc.getValue();
	}

}
