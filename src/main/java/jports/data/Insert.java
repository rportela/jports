package jports.data;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * This is a blueprint for an abstract insert command that can either do a
 * simple execution or return the generated keys from the data store;
 * 
 * @author rportela
 *
 */
public abstract class Insert {

	private final LinkedHashMap<String, Object> values = new LinkedHashMap<>();

	/**
	 * Adds a specific name value pair to the insert command;
	 * 
	 * @param name
	 * @param value
	 * @return
	 */
	public Insert add(String name, Object value) {
		this.values.put(name, value);
		return this;
	}

	/**
	 * Gets the expected value of a specific column in the insert command;
	 * 
	 * @param name
	 * @return
	 */
	public Object get(String name) {
		return this.values.get(name);
	}

	/**
	 * Gets a map of the column names and values in the insert command;
	 * 
	 * @return
	 */
	public Map<String, Object> getValues() {
		return this.values;
	}

	/**
	 * Actually executes the command and returns the number of records affected;
	 * 
	 * @return
	 * @throws Exception
	 */
	public abstract int execute();

	/**
	 * Executes the insert command and returns the generated key from the data
	 * store;
	 * 
	 * @return
	 * @throws Exception
	 */
	public abstract Object executeWithGeneratedKey();
}
