package jports.data;

import java.util.LinkedHashMap;
import java.util.Map;

public abstract class Insert<Target> {

	private Target target;
	private final LinkedHashMap<String, Object> values;

	public Insert(Target target) {
		this.target = target;
		this.values = new LinkedHashMap<>();
	}

	public Insert<Target> add(String name, Object value) {
		this.values.put(name, value);
		return this;
	}

	public Object get(String name) {
		return this.values.get(name);
	}

	public Map<String, Object> getValues() {
		return this.values;
	}

	public Target getTarget() {
		return this.target;
	}

	public Insert<Target> setTarget(Target value) {
		this.target = value;
		return this;
	}

	public abstract void execute();
}
