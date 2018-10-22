package jports.database;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.apache.tomcat.jdbc.pool.PoolProperties;

/**
 * A class that abstracts a database. It contains methods and configurations
 * that should be database specific;
 * 
 * @author rportela
 *
 */
public abstract class Database {

	/**
	 * The JDBC data source available to this Database class;
	 */
	public final DataSource data_source;

	/**
	 * Provides the driver class name as required by JDBC;
	 * 
	 * @return
	 */
	public abstract String getDriverClass();

	/**
	 * Creates the default connection pool properties object; Override this to add
	 * custom or specific settings;
	 * 
	 * @return
	 */
	protected PoolProperties createPoolProperties() {
		PoolProperties p = new PoolProperties();
		p.setJmxEnabled(true);
		p.setTestWhileIdle(false);
		p.setTestOnBorrow(true);
		p.setValidationQuery("SELECT 1");
		p.setTestOnReturn(false);
		p.setValidationInterval(30000);
		p.setTimeBetweenEvictionRunsMillis(30000);
		p.setMaxActive(100);
		p.setInitialSize(10);
		p.setMaxWait(10000);
		p.setRemoveAbandonedTimeout(60);
		p.setMinEvictableIdleTimeMillis(30000);
		p.setMinIdle(10);
		p.setLogAbandoned(true);
		p.setRemoveAbandoned(true);
		p.setJdbcInterceptors("org.apache.tomcat.jdbc.pool.interceptor.ConnectionState;"
				+ "org.apache.tomcat.jdbc.pool.interceptor.StatementFinalizer");
		return p;
	}

	/**
	 * Creates a new instance of the database class;
	 * 
	 * @param jdbcUrl
	 * @param username
	 * @param password
	 */
	public Database(String jdbcUrl, String username, String password) {
		PoolProperties p = createPoolProperties();
		p.setUrl(jdbcUrl);
		p.setDriverClassName(getDriverClass());
		p.setUsername(username);
		p.setPassword(password);
		this.data_source = new org.apache.tomcat.jdbc.pool.DataSource();
		((org.apache.tomcat.jdbc.pool.DataSource) this.data_source).setPoolProperties(p);
	}

	/**
	 * Pops a pooled connection from the underlying data source;
	 * 
	 * @return
	 * @throws SQLException
	 */
	public Connection getConnection() throws SQLException {
		return this.data_source.getConnection();
	}

	/**
	 * Creates a new instance of the database command;
	 * 
	 * @return
	 */
	public abstract DatabaseCommand createCommand();

	public DatabaseInsert insert(String table) {
		return new DatabaseInsert(this, table);
	}

	public DatabaseDelete delete(String table) {
		return new DatabaseDelete(this, table);
	}
}
