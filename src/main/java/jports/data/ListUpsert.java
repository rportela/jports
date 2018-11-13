package jports.data;

import java.util.Map.Entry;

public class ListUpsert<T> extends Upsert {

	private final ListStorage<T> storage;

	public ListUpsert(ListStorage<T> storage) {
		this.storage = storage;
	}

	@Override
	public int execute() {
		Update update = storage.createUpdate();
		for (Entry<String, Object> entry : getValues().entrySet()) {
			if (!containsKey(entry.getKey()))
				update.set(entry.getKey(), entry.getValue());
		}
		for (String key : getKeys())
			update.andWhere(key, get(key));
		int result = update.execute();
		if (result < 1) {
			Insert insert = storage.createInsert();
			for (Entry<String, Object> entry : getValues().entrySet()) {
				insert.add(entry.getKey(), entry.getValue());
			}
			result = insert.execute();
		}
		return result;
	}

}
