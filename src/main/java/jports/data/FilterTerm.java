package jports.data;

import java.util.function.Predicate;
import java.util.regex.Pattern;

import jports.ShowStopper;

public class FilterTerm implements Filter {

	private final String name;
	private final FilterComparison comparison;
	private final Object value;

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

	/**
	 * @return the name
	 */
	public final String getName() {
		return name;
	}

	/**
	 * @return the comparison
	 */
	public final FilterComparison getComparison() {
		return comparison;
	}

	/**
	 * @return the value
	 */
	public final Object getValue() {
		return value;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public final Predicate<Object> valuePredicate = o -> {
		Object pValue = getValue();
		switch (getComparison()) {
		case EQUAL_TO:
			return o == null
					? pValue == null
					: o.equals(pValue);
		case GREATER_OR_EQUAL:
			return o != null && ((Comparable) o).compareTo(pValue) >= 0;
		case GREATER_THAN:
			return o != null && ((Comparable) o).compareTo(pValue) > 0;
		case LIKE:
			return o != null && ((Pattern) pValue).matcher((CharSequence) o).matches();
		case LOWER_OR_EQUAL:
			return o != null && ((Comparable) o).compareTo(pValue) <= 0;
		case LOWER_THAN:
			return o != null && ((Comparable) o).compareTo(pValue) < 0;
		case NOT_EQUAL_TO:
			return o == null || !o.equals(pValue);
		case NOT_LIKE:
			return o == null || !((Pattern) pValue).matcher((CharSequence) o).matches();
		default:
			throw new ShowStopper("Unknown filter comparison: " + getComparison());
		}
	};

}
