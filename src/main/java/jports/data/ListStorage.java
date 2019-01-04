package jports.data;

import java.util.ArrayList;
import java.util.List;

/**
 * A list that stores specific types of objects;
 * 
 * @author rportela
 *
 * @param <T>
 */
public class ListStorage<T> extends DataStorage<T> {

	private List<T> list;
	private ListAspect<T> aspect;

	protected ListStorage<T> setList(List<T> newList) {
		this.list = newList;
		return this;
	}

	public ListStorage(Class<T> claz, List<T> list) {
		this.aspect = new ListAspect<>(claz);
		this.list = list;
	}

	public ListStorage(Class<T> claz) {
		this(claz, new ArrayList<>());
	}

	public List<T> all() {
		return this.list;
	}

	@Override
	public Insert createInsert() {
		return new ListInsert<>(this);
	}

	@Override
	public Delete createDelete() {
		return new ListDelete<>(this);
	}

	@Override
	public Update createUpdate() {
		return new ListUpdate<>(this);
	}

	@Override
	public Upsert createUpsert() {
		return new ListUpsert<>(this);
	}

	@Override
	public Select<T> select() {
		return new ListSelect<>(this);
	}

	@Override
	public DataAspect<T, DataAspectMember<T>> getAspect() {
		return aspect;
	}

}
