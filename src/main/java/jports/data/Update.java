package jports.data;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * This is the blueprint of an update command that can change records in a
 * backing store and returns the number of records affected by the update
 * command;
 * 
 * @author rportela
 *
 */
public abstract class Update extends Filterable<Update> {

	private final LinkedHashMap<String, Object> values = new LinkedHashMap<>();

	/**
	 * Sets a specific column to a specific value;
	 * 
	 * @param name
	 * @param value
	 * @return
	 */
	public Update set(String name, Object value) {
		this.values.put(name, value);
		return this;
	}

	/**
	 * Gets the expected value to be set for a column with the given name;
	 * 
	 * @param name
	 * @return
	 */
	public Object get(String name) {
		return this.values.get(name);
	}

	/**
	 * Gets the number of columns that this command contains;
	 * 
	 * @return
	 */
	public int size() {
		return this.values.size();
	}

	/**
	 * Clears the command by removing every entry on the backing map;
	 * 
	 * @return
	 */
	public Update clear() {
		this.values.clear();
		return this;
	}

	/**
	 * Gets the values of the columns that will be updated;
	 * 
	 * @return
	 */
	public Map<String, Object> getValues() {
		return this.values;
	}

	/**
	 * Gets a reference to this instance for method chaining;
	 */
	@Override
	protected Update getThis() {
		return this;
	}

	/**
	 * Executes the command and returns the number of records affected by it;
	 * 
	 * @return
	 * @throws Exception
	 */
	public abstract int execute();

}
