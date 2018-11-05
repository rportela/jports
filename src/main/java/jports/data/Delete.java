package jports.data;

/**
 * This is a blueprint of a delete command that can remove items from a backing
 * storage;
 * 
 * @author rportela
 *
 * @param <Target>
 */
public abstract class Delete extends Filterable<Delete> {

	/**
	 * Gets the reference to this instance for method chaining;
	 */
	@Override
	protected Delete getThis() {
		return this;
	}

	/**
	 * Actually executes the delete command and returns the number of records
	 * affected;
	 * 
	 * @return
	 */
	public abstract int execute();

}
