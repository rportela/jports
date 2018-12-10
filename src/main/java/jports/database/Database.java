package jports.database;

import java.io.Closeable;
import java.io.IOException;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.LinkedList;

/**
 * A class that abstracts a database. It contains methods and configurations
 * that should be database specific;
 * 
 * @author rportela
 *
 */
public abstract class Database implements AutoCloseable, Closeable {

	final String jdbcUrl;
	final String username;
	final String password;
	final LinkedList<DatabaseConnection> pool;

	/**
	 * Provides the driver class name as required by JDBC;
	 * 
	 * @return
	 */
	public abstract String getDriverClass();

	/**
	 * Creates a new instance of the database class;
	 * 
	 * @param jdbcUrl
	 * @param username
	 * @param password
	 */
	public Database(String jdbcUrl, String username, String password) {
		this.jdbcUrl = jdbcUrl;
		this.username = username;
		this.password = password;

		this.pool = new LinkedList<>();
	}

	/**
	 * Creates a new instance of the database command;
	 * 
	 * @return
	 */
	public abstract DatabaseCommand createCommand();

	/**
	 * Creates a new database insert command wrapper;
	 * 
	 * @param table
	 * @return
	 */
	public DatabaseInsert insert(String table) {
		return new DatabaseInsert(this, table);
	}

	/**
	 * Creates a new database delete command wrapper;
	 * 
	 * @param table
	 * @return
	 */
	public DatabaseDelete delete(String table) {
		return new DatabaseDelete(this, table);
	}

	/**
	 * Creates a new database update command wrapper;
	 * 
	 * @param table
	 * @return
	 */
	public DatabaseUpdate update(String table) {
		return new DatabaseUpdate(this, table);
	}

	/**
	 * Creates a new database select wrapper that turns result sets into lists of
	 * maps;
	 * 
	 * @param objectName
	 * @return
	 */
	public DatabaseSelectRow select(String objectName) {
		return new DatabaseSelectRow(this, objectName);
	}

	/**
	 * Creates a new database upsert command;
	 * 
	 * @param target
	 * @return
	 */
	public DatabaseUpsert upsert(String target) {
		return new DatabaseUpsert(this, target);
	}

	/**
	 * Gets a pooled wrapped connection that will return itself to the connection
	 * pool when closed;
	 * 
	 * @return
	 * @throws SQLException
	 */
	public synchronized DatabaseConnection getConnection() throws SQLException {
		DatabaseConnection conn;
		synchronized (pool) {
			conn = pool.isEmpty()
					? null
					: pool.removeFirst();
		}

		if (conn == null) {
			conn = new DatabaseConnection(this, DriverManager.getConnection(jdbcUrl, username, password));
		}
		return conn;
	}

	/**
	 * Gets the size of the connection pool;
	 * 
	 * @return
	 */
	public int getPoolSize() {
		return this.pool.size();
	}

	/**
	 * Terminates all connections in the pool upon close;
	 */
	@Override
	public void close() throws IOException {
		try {
			for (DatabaseConnection conn : this.pool)
				try {
					conn.terminate();
				} catch (SQLException e) {
					e.printStackTrace();
				}
		} finally {
			pool.clear();
		}

	}
}
