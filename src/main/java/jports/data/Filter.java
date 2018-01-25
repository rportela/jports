package jports.data;

import java.util.function.Predicate;

public interface Filter<T> {

	public Predicate<T> toPredicate();
}
