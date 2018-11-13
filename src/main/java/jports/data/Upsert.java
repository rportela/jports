package jports.data;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public abstract class Upsert {

	private final LinkedHashMap<String, Object> values = new LinkedHashMap<>();
	private final ArrayList<String> keys = new ArrayList<>();

	public Upsert addKeys(String... columns) {
		for (int i = 0; i < columns.length; i++)
			keys.add(columns[i]);
		return this;
	}

	public Upsert addKey(String key) {
		this.keys.add(key);
		return this;
	}

	public Upsert set(String name, Object value) {
		this.values.put(name, value);
		return this;
	}

	public Object get(String name) {
		return this.values.get(name);
	}

	public Map<String, Object> getValues() {
		return this.values;
	}

	public List<String> getKeys() {
		return this.keys;
	}

	public boolean containsKey(String key) {
		return this.keys.contains(key);
	}

	public abstract int execute();

}
