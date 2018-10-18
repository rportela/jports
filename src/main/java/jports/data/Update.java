package jports.data;

import java.util.LinkedHashMap;
import java.util.Map;

public abstract class Update<Target> extends Filterable<Update<Target>> {

	private Target target;
	private final LinkedHashMap<String, Object> values = new LinkedHashMap<>();

	public Update(Target target) {
		this.target = target;
	}

	public Update<Target> setTarget(Target value) {
		this.target = value;
		return this;
	}

	public Target getTarget() {
		return this.target;
	}

	public Update<Target> set(String name, Object value) {
		this.values.put(name, value);
		return this;
	}

	public Object get(String name) {
		return this.values.get(name);
	}

	public int size() {
		return this.values.size();
	}

	public Update<Target> clear() {
		this.values.clear();
		return this;
	}

	public Map<String, Object> getValues() {
		return this.values;
	}

	@Override
	protected Update<Target> getThis() {
		return this;
	}

	public abstract int execute();

}
