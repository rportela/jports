package jports.data;

import java.util.function.Predicate;

public class FilterTerm<T> implements Filter<T> {

	public final Column<T> column;
	public final FilterComparison comparison;
	public final Object value;

	public FilterTerm(Column<T> column, FilterComparison comparison, Object value) {
		this.column = column;
		this.comparison = comparison;
		this.value = value;
	}

	public FilterTerm(Column<T> column, Object value) {
		this(column, FilterComparison.EQUAL_TO, value);
	}

	public Predicate<T> toPredicate() {
		switch (this.comparison) {
		case EQUAL_TO:
			return new Predicate<T>() {
				public boolean test(T t) {
					Object val = column.getValue(t);
					return val == null ? value == null : val.equals(t);
				}
			};

		default:
			throw new RuntimeException("Unimplemented toPredicate for filter comparison " + this.comparison);

		}
	}

}
