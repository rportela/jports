package jports.data;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class ListDelete<T> extends Delete {

	private final ListStorage<T> storage;

	public ListDelete(ListStorage<T> storage) {
		this.storage = storage;
	}

	@Override
	public int execute() {
		List<T> list = storage.all();
		int prevSize = list.size();
		FilterExpression filter2 = getFilter();
		if (filter2 == null) {
			list.clear();
			return prevSize;
		} else {
			List<T> newList = list.stream().filter(new Predicate<T>() {
				final Predicate<T> delete = storage.getAspect().createFilter(filter2);

				@Override
				public boolean test(T arg0) {
					return !delete.test(arg0);
				}
			})
					.collect(Collectors.toList());
			storage.setList(newList);
			return prevSize - newList.size();
		}
	}
}
