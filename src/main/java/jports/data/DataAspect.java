package jports.data;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import jports.reflection.Aspect;

public abstract class DataAspect<TClass, TMember extends DataAspectMember<TClass>> extends Aspect<TClass, TMember>
		implements ColumnSchema<TMember> {

	private TMember identity;
	private final ArrayList<TMember> uniques = new ArrayList<>();
	private final ArrayList<TMember> composite_key = new ArrayList<>();

	protected DataAspect(Class<TClass> dataType) {
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

	@Override
	public int getColumnCount() {
		return this.size();
	}

	@Override
	public int getColumnOrdinal(String name) {
		for (int i = 0; i < size(); i++)
			if (name.equalsIgnoreCase(get(i).getColumnName()))
				return i;
		return -1;
	}

	@Override
	public List<TMember> getColumns() {
		return super.members;
	}

	@Override
	public TMember getColumn(int ordinal) {
		return super.get(ordinal);
	}

	@Override
	public TMember getColumn(String name) {
		int ordinal = getColumnOrdinal(name);
		return ordinal < 0
				? null
				: get(ordinal);
	}

	@Override
	public TMember getIdentity() {
		return this.identity;
	}

	public void setIdentity(String name) {
		if (this.identity != null) {
			this.identity.setColumnType(ColumnType.REGULAR);
		}
		this.identity = getColumn(name);
		this.identity.setColumnType(ColumnType.IDENTITY);
	}

	@Override
	public List<TMember> getUniqueColumns() {
		return this.uniques;
	}

	public void setUnique(String name) {
		TMember column = getColumn(name);
		if (!uniques.contains(column)) {
			column.setColumnType(ColumnType.UNIQUE);
			uniques.add(column);
		}
	}

	@Override
	public List<TMember> getCompositeKey() {
		return this.composite_key;
	}

	public void setCompositeKey(String... names) {

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
	}

	public Predicate<TClass> createCompositeFilterFor(TClass entity) {
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
}
