package jports.data;

import java.util.function.Predicate;
import java.util.regex.Pattern;

public class FilterTerm implements Filter {

	public final String name;
	public final FilterComparison comparison;
	public final Object value;

	public FilterTerm(String name, FilterComparison comparison, Object value) {
		this.name = name;
		this.comparison = comparison;
		this.value = value;
	}

	public FilterTerm(String name, Object value) {
		this(name, FilterComparison.EQUAL_TO, value);
	}

	@Override
	public final FilterType getFilterType() {
		return FilterType.TERM;
	}

	public Predicate<Object> createValuePredicate() {

		return new Predicate<Object>() {
			@SuppressWarnings({ "unchecked", "rawtypes" })
			@Override
			public boolean test(Object o) {
				switch (comparison) {
				case EQUAL_TO:
					return o == null
							? value == null
							: o.equals(value);
				case GRATER_OR_EQUAL:
					return o == null
							? value == null
							: ((Comparable) o).compareTo(value) >= 0;
				case GREATER_THAN:
					return o == null
							? false
							: ((Comparable) o).compareTo(value) > 0;
				case LIKE:
					return o == null
							? value == null
							: ((Pattern) value).matcher((CharSequence) o).matches();

				case LOWER_OR_EQUAL:
					return o == null
							? value == null
							: ((Comparable) o).compareTo(value) <= 0;
				case LOWER_THAN:
					return o == null
							? false
							: ((Comparable) o).compareTo(value) < 0;
				case NOT_EQUAL_TO:
					return o == null
							? value != null
							: !o.equals(value);
				case NOT_LIKE:
					return o == null
							? value != null
							: !((Pattern) value).matcher((CharSequence) o).matches();
				default:
					throw new RuntimeException("Unknown filter comparison: " + comparison);

				}
			}
		};
	}

}
