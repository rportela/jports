package jports.data;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import jports.ShowStopper;

public class ListInsert<T> extends Insert {

	private final ListStorage<T> storage;

	public ListInsert(ListStorage<T> storage) {
		this.storage = storage;
	}

	@Override
	public int execute() {
		DataAspect<T, ?> aspect = storage.getAspect();
		T entity = aspect.newInstance();
		for (Entry<String, Object> entry : this.getValues().entrySet()) {
			aspect.get(entry.getKey()).setValue(entity, entry.getValue());
		}
		storage.all().add(entity);
		return 1;
	}

	@Override
	public Map<String, Object> executeWithGeneratedKeys() {
		DataAspect<T, ?> aspect = storage.getAspect();
		DataAspectMember<T> identity = aspect.getIdentity();
		if (identity == null)
			throw new ShowStopper("This aspect has no identity: " + aspect);

		HashMap<String, Object> map = new HashMap<>();
		map.put(identity.getColumnName(), storage.all().size());
		return map;
	}
}
