package jports.data;

import java.util.ArrayList;
import java.util.List;

import jports.data.DataAspect;
import jports.data.DataStorage;
import jports.data.Delete;
import jports.data.Insert;
import jports.data.Select;
import jports.data.Update;

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
	public Select<T> select() {
		return new ListSelect<T>(this);
	}

	@Override
	public DataAspect<T, ?> getAspect() {
		return aspect;
	}

}
