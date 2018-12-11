package jports.data;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import jports.reflection.Aspect;

/**
 * This is the base class for data annotated objects; You can use the @Column
 * annotation in your classes and extend this DataAspect to handle identities,
 * unique members and composite filters;
 * 
 * @author rportela
 *
 * @param <TClass>
 * @param <TMember>
 */
public abstract class DataAspect<TClass, TMember extends DataAspectMember<TClass>>
		extends Aspect<TClass, TMember>
		implements ColumnSchema<TMember> {

	private TMember identity;
	private final ArrayList<TMember> uniques = new ArrayList<>();
	private final ArrayList<TMember> composite_key = new ArrayList<>();

	/**
	 * Creates a new Data Aspect for a specific data type;
	 * 
	 * @param dataType
	 */
	protected DataAspect(final Class<TClass> dataType) {
		super(dataType);

		TMember id = null;
		for (TMember member : this) {
			switch (member.getColumnType()) {
			case COMPOSITE_KEY:
				composite_key.add(member);
				break;
			case IDENTITY:
				if (id != null)
					throw new RuntimeException("Please use just one identity per class: " +
							dataType +
							" (" +
							member.getColumnName() +
							")");
				else
					id = member;
				break;
			case UNIQUE:
				uniques.add(member);
				break;
			default:
				break;
			}
		}
		this.identity = id;
	}

	/**
	 * Gets the number of columns in this data aspect;
	 */
	@Override
	public int getColumnCount() {
		return this.size();
	}

	/**
	 * Gets the ordinal position of a member by it's column name. Note that this
	 * method is different then calling the indexOf method of the super class that
	 * will look up a member by it's name and not it's column name; If no member had
	 * the given column name, -1 is returned;
	 */
	@Override
	public synchronized int getColumnOrdinal(final String name) {
		for (int i = 0; i < size(); i++)
			if (name.equalsIgnoreCase(get(i).getColumnName()))
				return i;
		return -1;
	}

	/**
	 * Gets all the columns of this Data Aspect;
	 */
	@Override
	public List<TMember> getColumns() {
		return super.getMembers();
	}

	/**
	 * Gets a specific column by it's ordinal position in this schema;
	 */
	@Override
	public TMember getColumn(final int ordinal) {
		return super.get(ordinal);
	}

	/**
	 * Gets a specific column by it's name. Note that this method is different than
	 * calling get(name) on the super class because it will look up the column name
	 * instead of the member name. This method returns null if no column had the
	 * informed name;
	 */
	@Override
	public TMember getColumn(final String name) {
		final int ordinal = getColumnOrdinal(name);
		return ordinal < 0
				? null
				: get(ordinal);
	}

	/**
	 * Gets the identity member of this data aspect or null if none was annotated;
	 */
	@Override
	public TMember getIdentity() {
		return this.identity;
	}

	/**
	 * Sets the identity column by finding a column with a specific name. Note that
	 * this method will look up the column name annotated in the member and not the
	 * member name. However, if you don't provide a specific column name, the member
	 * name will be used as such.
	 * 
	 * @param name
	 */
	public synchronized DataAspect<TClass, TMember> setIdentity(final String name) {
		if (this.identity != null) {
			this.identity.setColumnType(ColumnType.REGULAR);
		}
		this.identity = getColumn(name);
		if (this.identity != null) {
			this.identity.setColumnType(ColumnType.IDENTITY);
		}
		return this;
	}

	/**
	 * Gets a list of unique columns annotated on this data aspect;
	 */
	@Override
	public List<TMember> getUniqueColumns() {
		return this.uniques;
	}

	/**
	 * Sets a specific column as being unique in this data aspect. Note that this
	 * method will look up the column name annotated in the member and not the
	 * member name. However, if you don't provide a specific column name, the member
	 * name will be used as such.
	 * 
	 * @param name
	 */
	public synchronized DataAspect<TClass, TMember> setUnique(final String name) {
		TMember column = getColumn(name);
		if (!uniques.contains(column)) {
			column.setColumnType(ColumnType.UNIQUE);
			uniques.add(column);
		}
		return this;
	}

	/**
	 * Gets a list of members that are part of the composite key;
	 */
	@Override
	public List<TMember> getCompositeKey() {
		return this.composite_key;
	}

	/**
	 * This method sets specific members to be part of a composite key. At least 2
	 * column names must be provided otherwise an exception is thrown. Note that
	 * this method will look up the column name annotated in the member and not the
	 * member name. However, if you don't provide a specific column name, the member
	 * name will be used as such.
	 * 
	 * @param names
	 */
	public synchronized DataAspect<TClass, TMember> setCompositeKey(final String... names) {

		if (names.length < 2)
			throw new RuntimeException("Please add at least 2 columns to a composite key: " + this);

		for (TMember prev : this.composite_key) {
			prev.setColumnType(ColumnType.REGULAR);
		}

		this.composite_key.clear();

		for (int i = 0; i < names.length; i++) {
			TMember column = getColumn(names[i]);
			column.setColumnType(ColumnType.COMPOSITE_KEY);
			this.composite_key.add(column);
		}

		return this;
	}

	/**
	 * This method creates the predicate for the composite key based on the values
	 * extracted from a specific entity;
	 * 
	 * @param entity
	 * @return
	 */
	public synchronized Predicate<TClass> createCompositeKeyFilter(final TClass entity) {
		return new Predicate<TClass>() {
			private final List<Object> ckValues = composite_key
					.stream()
					.map(ck -> ck.getValue(entity))
					.collect(Collectors.toList());

			@Override
			public boolean test(TClass t) {
				for (int i = 0; i < ckValues.size(); i++) {
					TMember ck = composite_key.get(i);
					Object a = ck.getValue(t);
					Object b = ckValues.get(i);
					if (b == null) {
						if (a != null)
							return false;
					} else if (!b.equals(a))
						return false;
				}
				return true;
			}
		};
	}

	/**
	 * This method turns a filter into a predicate by selecting the apropriate
	 * member by their name as instructed on the Filter Terms.
	 * 
	 * @param filter
	 * @return
	 */
	public Predicate<TClass> createFilter(final Filter filter) {
		switch (filter.getFilterType()) {
		case EXPRESSION:
			return createFilter((FilterExpression) filter);
		case NODE:
			return createFilter((FilterNode) filter);
		case TERM:
			return createFilter((FilterTerm) filter);
		default:
			throw new RuntimeException("Unknwon filter type: " + filter.getFilterType() + " on " + filter);

		}
	}

	/**
	 * This method turns a filter into a predicate by selecting the appropriate
	 * member by their name as instructed on the Filter Terms.
	 * 
	 * @param expression
	 * @return
	 */
	public Predicate<TClass> createFilter(final FilterExpression expression) {
		return createFilter(expression.first);
	}

	/**
	 * This method turns a filter into a predicate by selecting the appropriate
	 * member by their name as instructed on the Filter Terms.
	 * 
	 * @param node
	 * @return
	 */
	public synchronized Predicate<TClass> createFilter(final FilterNode node) {
		return new Predicate<TClass>() {

			final Predicate<TClass> filterPredicate = createFilter(node.filter);
			final FilterOperation op = node.operation;
			final Predicate<TClass> next = node.next == null
					? null
					: createFilter(node.next);

			@Override
			public boolean test(TClass arg0) {
				if (next == null)
					return filterPredicate.test(arg0);
				else {
					switch (op) {
					case AND:
						return filterPredicate.test(arg0) && next.test(arg0);
					case OR:
						return filterPredicate.test(arg0) || next.test(arg0);
					default:
						throw new RuntimeException("Unknown filter operation: " + op);

					}
				}
			}
		};
	}

	/**
	 * This method turns a filter into a predicate by selecting the appropriate
	 * member by it's name as instructed on given filter term.
	 * 
	 * @param term
	 * @return
	 */
	public synchronized Predicate<TClass> createFilter(final FilterTerm term) {
		return new Predicate<TClass>() {

			final DataAspectMember<TClass> member = get(term.name);
			final Predicate<Object> valuePredicate = term.createValuePredicate();

			@Override
			public boolean test(TClass arg0) {
				Object value = member.getValue(arg0);
				return valuePredicate.test(value);
			}
		};
	}

	/**
	 * This method creates a comparator based on a specific member selected by it's
	 * member name as indicated by the provided sort node;
	 * 
	 * @param node
	 * @return
	 */
	public synchronized Comparator<TClass> createComparator(final SortNode node) {

		switch (node.direction) {
		case ASCENDING:
			return new Comparator<TClass>() {
				final TMember member = get(node.name);

				@SuppressWarnings({ "unchecked", "rawtypes" })
				@Override
				public int compare(TClass arg0, TClass arg1) {
					Object a = member.getValue(arg0);
					Object b = member.getValue(arg1);
					return ((Comparable) a).compareTo(b);
				}
			};
		case DESCENDING:
			return new Comparator<TClass>() {
				final TMember member = get(node.name);

				@SuppressWarnings({ "unchecked", "rawtypes" })
				@Override
				public int compare(TClass arg0, TClass arg1) {
					Object a = member.getValue(arg0);
					Object b = member.getValue(arg1);
					return -((Comparable) a).compareTo(b);
				}
			};
		default:
			throw new RuntimeException("Unknown sort direction: " + node.direction);
		}

	}

	public TMember[] getColumnArray(String... names) {
		return null;
	}
}
