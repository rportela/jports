package jports.data;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

/**
 * A list that stores specific types of objects;
 * 
 * @author rportela
 *
 * @param <T>
 */
public class ListStorage<T> implements Storage<T> {

	private List<T> list;
	private ListAspect<T> aspect;

	public ListStorage(Class<T> claz) {
		this.aspect = new ListAspect<>(claz);
		this.list = new ArrayList<>();
	}

	private boolean doUpdate(T entity) {
		DataAspectMember<T> identity = aspect.getIdentity();
		if (identity != null) {
			Object id = identity.getValue(entity);
			if (id != null) {
				for (int i = 0; i < list.size(); i++) {
					T other = list.get(i);
					Object otherid = identity.getValue(other);
					if (id.equals(otherid)) {
						list.set(i, entity);
						return true;
					}
				}
			}
		}
		for (DataAspectMember<T> uniqueMember : aspect.getUniqueColumns()) {
			Object uniqueValue = uniqueMember.getValue(entity);
			if (uniqueValue != null) {
				for (int i = 0; i < list.size(); i++) {
					T other = list.get(i);
					Object otherid = uniqueMember.getValue(other);
					if (uniqueValue.equals(otherid)) {
						list.set(i, entity);
						return true;
					}
				}
			}
		}

		List<DataAspectMember<T>> compositeKey = aspect.getCompositeKey();
		if (!compositeKey.isEmpty()) {
			Predicate<T> predicate = aspect.createCompositeFilterFor(entity);
			for (int i = 0; i < list.size(); i++) {
				T other = list.get(i);
				if (predicate.test(other)) {
					list.set(i, entity);
					return true;
				}
			}
		}

		return false;
	}

	@Override
	public int save(T entity) {
		if (!doUpdate(entity))
			list.add(entity);
		return 1;

	}

	@Override
	public int insert(T entity) {
		this.list.add(entity);
		return 1;
	}

	@Override
	public int delete(T entity) {
		return this.list.remove(entity)
				? 1
				: 0;
	}

	@Override
	public int update(T entity) {
		return doUpdate(entity)
				? 1
				: 0;
	}

	@Override
	public Select<T> select() {
		// TODO Auto-generated method stub
		return null;
	}

}