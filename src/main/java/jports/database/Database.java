package jports.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Savepoint;
import java.util.LinkedList;
import java.util.Properties;

import jports.GenericLogger;
import jports.ShowStopper;

/**
 * A class that abstracts a database. It contains methods and configurations
 * that should be database specific; Override the abstract methods of this class
 * do build vendor specific database implementations.
 * 
 * @author rportela
 *
 */
public abstract class Database {

	private final String jdbcUrl;
	private final Properties properties;
	private final LinkedList<DatabaseConnection> pool = new LinkedList<>();
	private Connection transactionConnection;
	private Savepoint transactionSavepoint;

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
	 * @param properties
	 * @throws ClassNotFoundException
	 */
	public Database(String jdbcUrl, Properties properties) throws ClassNotFoundException {
		this.jdbcUrl = jdbcUrl;
		this.properties = properties;
		Class.forName(getDriverClass());
	}

	/**
	 * Creates a new instance of the database class;
	 * 
	 * @param jdbcUrl
	 * @throws ClassNotFoundException
	 */
	public Database(String jdbcUrl) throws ClassNotFoundException {
		this(jdbcUrl, null);
	}

	/**
	 * This method indicates that the underlying database accepts the TOP statement;
	 * 
	 * @return
	 */
	public boolean acceptsTop() {
		return false;
	}

	/**
	 * This method indicates that the underlying database accepts the OFFSET
	 * statement;
	 * 
	 * @return
	 */
	public boolean acceptsOffset() {
		return false;
	}

	/**
	 * This method indicates that the underlying database accepts the LIMIT
	 * statement;
	 * 
	 * @return
	 */
	public boolean acceptsLimit() {
		return false;
	}

	/**
	 * Gets the database specific name prefix like [ or `;
	 * 
	 * @return
	 */
	public abstract String getNamePrefix();

	/**
	 * Gets the database specific name suffix like ] or ´;
	 * 
	 * @return
	 */
	public abstract String getNameSuffix();

	/**
	 * Validates a name for invalid chars and throws an exception if the name is not
	 * valid.
	 * 
	 * @param name
	 */
	public void validateNameOrThrowException(String name) {
		if (name.contains("'") || name.contains("--"))
			throw new ShowStopper("Invalid database object name: " + name);
	}

	/**
	 * Creates a new connection based on the JDBC URL and properties;
	 * 
	 * @return
	 * @throws SQLException
	 */
	protected Connection createConnection() {
		try {
			return this.properties == null
					? DriverManager.getConnection(jdbcUrl)
					: DriverManager.getConnection(jdbcUrl, properties);
		} catch (Exception e) {
			throw new ShowStopper(e);
		}
	}

	public final synchronized Connection getConnection() {

		if (transactionConnection != null)
			return transactionConnection;

		if (pool.isEmpty())
			return createConnection();

		Connection conn = pool.pop();
		try {
			if (!conn.isClosed())
				return conn;
		} catch (SQLException e) {
			// just ignore this exception
		}
		return createConnection();
	}

	/**
	 * Accepts a connection back to the pool. This method should be called from the
	 * database connection, when it is closed.
	 * 
	 * @param conn
	 */
	protected synchronized void pushConnection(DatabaseConnection conn) {
		try {
			if (!conn.isClosed())
				pool.push(conn);
		} catch (SQLException e) {
			// just ignore this exception
		}
	}

	/**
	 * Creates a new instance of the database command;
	 * 
	 * @return
	 */
	public DatabaseCommand createCommand() {
		return new DatabaseCommand(this);
	}

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
	 * Begins a transaction on the current database;
	 * 
	 * @return
	 * @throws SQLException
	 */
	public synchronized Savepoint beginTransaction() throws SQLException {
		if (this.transactionConnection != null)
			throw new ShowStopper("Another transaction is already running: " + transactionSavepoint);

		this.transactionConnection = getConnection();
		this.transactionConnection.setAutoCommit(false);
		this.transactionSavepoint = this.transactionConnection.setSavepoint();
		return this.transactionSavepoint;
	}

	/**
	 * Commits a running transaction or does nothing if none is present;
	 * 
	 * @throws SQLException
	 */
	public synchronized void commit() throws SQLException {
		if (this.transactionConnection != null) {
			this.transactionConnection.commit();
			this.transactionSavepoint = null;
			this.transactionConnection.setAutoCommit(true);
			try {
				this.transactionConnection.close();
			} catch (Exception e) {
				// ignore
			}
			this.transactionConnection = null;
		}
	}

	/**
	 * Rolls back a running transaction;
	 * 
	 * @throws SQLException
	 */
	public synchronized void rollback() throws SQLException {
		if (this.transactionConnection != null) {
			this.transactionConnection.rollback(this.transactionSavepoint);
			this.transactionSavepoint = null;
			this.transactionConnection.setAutoCommit(true);
			try {
				this.transactionConnection.close();
			} catch (Exception e) {
				// ignore
			}
			this.transactionConnection = null;
		}
	}

	public synchronized void clearPool() {
		for (DatabaseConnection conn : this.pool) {
			try {
				conn.close();
			} catch (SQLException e) {
				GenericLogger.info(getClass(), e);
			}
		}
		this.pool.clear();
	}

}
