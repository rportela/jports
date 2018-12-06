package jports.database;

import java.sql.Array;
import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.NClob;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLClientInfoException;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.SQLXML;
import java.sql.Savepoint;
import java.sql.Statement;
import java.sql.Struct;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.Executor;

public class DatabaseConnection implements Connection {

	private Database database;
	private Statement statement;
	private Connection connection;
	private Date created_at;
	private Date released_at;

	protected DatabaseConnection(Database database, Connection connection) throws SQLException {
		this.database = database;
		this.connection = connection;
		this.created_at = new Date();
		this.statement = connection.createStatement();
	}

	public Date getCreatetAt() {
		return this.created_at;
	}

	public Date getReleasedAt() {
		return this.released_at;
	}

	public void terminate() throws SQLException {
		System.out.println("terminating ... " + this);
		try {
			this.statement.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		this.connection.close();
	}

	@Override
	protected void finalize() throws Throwable {
		terminate();
	}

	/**
	 * Executes the command;
	 * 
	 * @return
	 * @throws SQLException
	 */
	public boolean execute(String text) throws SQLException {
		return statement.execute(text);
	}

	/**
	 * Executes an INSERT, UPDATE or DELETE and returns the number of records
	 * affected;
	 * 
	 * @return
	 * @throws SQLException
	 */
	public int executeNonQuery(String text) throws SQLException {
		return statement.executeUpdate(text);
	}

	/**
	 * Executes a query and adapts the result set to a specific data type;
	 * 
	 * @param adapter
	 * @return
	 * @throws SQLException
	 */
	public <T> T executeQuery(String text, ResultSetAdapter<T> adapter) throws SQLException {
		ResultSet resultSet = statement.executeQuery(text);
		try {
			return adapter.process(resultSet);
		} finally {
			resultSet.close();
		}
	}

	/**
	 * Executes what possibly is an INSERT statement and returns a map containing
	 * the generated keys;
	 * 
	 * @return
	 * @throws SQLException
	 */
	public Map<String, Object> executeWithGeneratedKeys(String text) throws SQLException {
		statement.execute(text, Statement.RETURN_GENERATED_KEYS);
		ResultSet rs = statement.getGeneratedKeys();
		try {
			if (!rs.next())
				return null;
			ResultSetMetaData meta = rs.getMetaData();
			LinkedHashMap<String, Object> gks = new LinkedHashMap<>();
			int count = meta.getColumnCount();
			for (int i = 1; i <= count; i++) {
				String name = meta.getColumnLabel(i);
				Object value = rs.getObject(i);
				gks.put(name, value);
			}
			return gks;
		} finally {
			rs.close();
		}
	}

	/**
	 * Executes the query and returns the first column of the first row in the
	 * result set;
	 * 
	 * @return
	 * @throws SQLException
	 */
	public Object executeScalar(String text) throws SQLException {
		ResultSet rs = statement.executeQuery(text);
		try {
			return rs.next()
					? rs.getObject(1)
					: null;
		} finally {
			rs.close();
		}
	}

	@Override
	public synchronized void close() throws SQLException {
		this.released_at = new Date();
		this.database.pool.add(this);
	}

	@Override
	public boolean isWrapperFor(Class<?> arg0) throws SQLException {
		return this.connection.isWrapperFor(arg0);
	}

	@Override
	public <T> T unwrap(Class<T> arg0) throws SQLException {
		return this.connection.unwrap(arg0);
	}

	@Override
	public void abort(Executor arg0) throws SQLException {
		this.connection.abort(arg0);

	}

	@Override
	public void clearWarnings() throws SQLException {
		this.connection.clearWarnings();
	}

	@Override
	public void commit() throws SQLException {
		this.connection.commit();

	}

	@Override
	public Array createArrayOf(String arg0, Object[] arg1) throws SQLException {
		return this.connection.createArrayOf(arg0, arg1);
	}

	@Override
	public Blob createBlob() throws SQLException {
		return this.connection.createBlob();
	}

	@Override
	public Clob createClob() throws SQLException {
		return this.connection.createClob();
	}

	@Override
	public NClob createNClob() throws SQLException {
		return this.connection.createNClob();
	}

	@Override
	public SQLXML createSQLXML() throws SQLException {
		return this.connection.createSQLXML();
	}

	@Override
	public Statement createStatement() throws SQLException {
		return this.connection.createStatement();
	}

	@Override
	public Statement createStatement(int arg0, int arg1) throws SQLException {
		return this.connection.createStatement(arg0, arg1);
	}

	@Override
	public Statement createStatement(int arg0, int arg1, int arg2) throws SQLException {
		return this.connection.createStatement(arg0, arg1, arg2);
	}

	@Override
	public Struct createStruct(String arg0, Object[] arg1) throws SQLException {
		return this.connection.createStruct(arg0, arg1);
	}

	@Override
	public boolean getAutoCommit() throws SQLException {
		return this.connection.getAutoCommit();
	}

	@Override
	public String getCatalog() throws SQLException {
		return this.connection.getCatalog();
	}

	@Override
	public Properties getClientInfo() throws SQLException {
		return this.connection.getClientInfo();
	}

	@Override
	public String getClientInfo(String arg0) throws SQLException {
		return this.connection.getClientInfo(arg0);
	}

	@Override
	public int getHoldability() throws SQLException {
		return this.connection.getHoldability();
	}

	@Override
	public DatabaseMetaData getMetaData() throws SQLException {
		return this.connection.getMetaData();
	}

	@Override
	public int getNetworkTimeout() throws SQLException {
		return this.connection.getNetworkTimeout();
	}

	@Override
	public String getSchema() throws SQLException {
		return this.connection.getSchema();
	}

	@Override
	public int getTransactionIsolation() throws SQLException {
		return this.connection.getTransactionIsolation();
	}

	@Override
	public Map<String, Class<?>> getTypeMap() throws SQLException {
		return this.connection.getTypeMap();
	}

	@Override
	public SQLWarning getWarnings() throws SQLException {
		return this.connection.getWarnings();
	}

	@Override
	public boolean isClosed() throws SQLException {
		return this.connection.isClosed();
	}

	@Override
	public boolean isReadOnly() throws SQLException {
		return this.connection.isReadOnly();
	}

	@Override
	public boolean isValid(int arg0) throws SQLException {
		return this.connection.isValid(arg0);
	}

	@Override
	public String nativeSQL(String arg0) throws SQLException {
		return this.connection.nativeSQL(arg0);
	}

	@Override
	public CallableStatement prepareCall(String arg0) throws SQLException {
		return this.connection.prepareCall(arg0);
	}

	@Override
	public CallableStatement prepareCall(String arg0, int arg1, int arg2) throws SQLException {
		return this.connection.prepareCall(arg0, arg1, arg2);
	}

	@Override
	public CallableStatement prepareCall(String arg0, int arg1, int arg2, int arg3) throws SQLException {
		return this.connection.prepareCall(arg0, arg1, arg2, arg3);
	}

	@Override
	public PreparedStatement prepareStatement(String arg0) throws SQLException {
		return this.connection.prepareStatement(arg0);
	}

	@Override
	public PreparedStatement prepareStatement(String arg0, int arg1) throws SQLException {
		return this.connection.prepareStatement(arg0, arg1);
	}

	@Override
	public PreparedStatement prepareStatement(String arg0, int[] arg1) throws SQLException {
		return this.connection.prepareStatement(arg0, arg1);
	}

	@Override
	public PreparedStatement prepareStatement(String arg0, String[] arg1) throws SQLException {
		return this.connection.prepareStatement(arg0, arg1);
	}

	@Override
	public PreparedStatement prepareStatement(String arg0, int arg1, int arg2) throws SQLException {
		return this.connection.prepareStatement(arg0, arg1, arg2);
	}

	@Override
	public PreparedStatement prepareStatement(String arg0, int arg1, int arg2, int arg3) throws SQLException {
		return this.connection.prepareStatement(arg0, arg1, arg2, arg3);
	}

	@Override
	public void releaseSavepoint(Savepoint arg0) throws SQLException {
		this.connection.releaseSavepoint(arg0);
	}

	@Override
	public void rollback() throws SQLException {
		this.connection.rollback();
	}

	@Override
	public void rollback(Savepoint arg0) throws SQLException {
		this.connection.rollback(arg0);

	}

	@Override
	public void setAutoCommit(boolean arg0) throws SQLException {
		this.connection.setAutoCommit(arg0);
	}

	@Override
	public void setCatalog(String arg0) throws SQLException {
		this.connection.setCatalog(arg0);
	}

	@Override
	public void setClientInfo(Properties arg0) throws SQLClientInfoException {
		this.connection.setClientInfo(arg0);
	}

	@Override
	public void setClientInfo(String arg0, String arg1) throws SQLClientInfoException {
		this.connection.setClientInfo(arg0, arg1);
	}

	@Override
	public void setHoldability(int arg0) throws SQLException {
		this.connection.setHoldability(arg0);

	}

	@Override
	public void setNetworkTimeout(Executor arg0, int arg1) throws SQLException {
		this.connection.setNetworkTimeout(arg0, arg1);
	}

	@Override
	public void setReadOnly(boolean arg0) throws SQLException {
		this.connection.setReadOnly(arg0);
	}

	@Override
	public Savepoint setSavepoint() throws SQLException {
		return this.connection.setSavepoint();
	}

	@Override
	public Savepoint setSavepoint(String arg0) throws SQLException {
		return this.connection.setSavepoint(arg0);
	}

	@Override
	public void setSchema(String arg0) throws SQLException {
		this.connection.setSchema(arg0);
	}

	@Override
	public void setTransactionIsolation(int arg0) throws SQLException {
		this.connection.setTransactionIsolation(arg0);

	}

	@Override
	public void setTypeMap(Map<String, Class<?>> arg0) throws SQLException {
		this.connection.setTypeMap(arg0);
	}

}
