package jports.data;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ListSelect<T> extends Select<T> {

	private final ListStorage<T> storage;

	public ListSelect(ListStorage<T> storage) {
		this.storage = storage;
	}

	public Stream<T> toStream() {
		final DataAspect<T, ?> aspect = storage.getAspect();
		Stream<T> stream = storage.all().stream();

		final FilterExpression expression = getFilter();
		if (expression != null)
			stream = stream.filter(aspect.createFilter(expression));

		final Sort sort = getSort();
		if (sort != null)
			for (SortNode node = sort.first; node != null; node = node.getNext())
				stream = stream.sorted(aspect.createComparator(node));

		final int offset = this.getOffset();
		if (offset > 0) {
			stream = stream.filter(new Predicate<T>() {
				int i = 0;

				@Override
				public boolean test(T arg0) {
					return i++ > offset;
				}
			});
		}

		final int limit = this.getLimit();
		if (limit > 0) {
			stream = stream.limit(limit);
		}
		return stream;

	}

	@Override
	public List<T> toList() {
		return toStream().collect(Collectors.toList());
	}

	@Override
	public long count() {
		FilterExpression filter = getFilter();
		if (filter == null)
			return storage.all().size();
		else {
			return storage.all()
					.stream()
					.filter(storage.getAspect().createFilter(filter))
					.collect(Collectors.counting())
					.longValue();
		}
	}

	@Override
	public T first() {
		return toStream().findAny().orElse(null);
	}

}
