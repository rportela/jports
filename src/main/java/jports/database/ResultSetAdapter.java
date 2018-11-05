package jports.database;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * This interface represents a generic result set adapter that can handle the
 * contents of a result set. Implementers should not close the result set to
 * allow method chaining and leaving the control to the instantiating class;
 * 
 * @author rportela
 *
 * @param <T>
 */
public interface ResultSetAdapter<T> {

	/**
	 * Processes a result set but does not closes it;
	 * 
	 * @param resultset
	 * @return
	 * @throws SQLException
	 */
	public T process(ResultSet resultset) throws SQLException;
}
