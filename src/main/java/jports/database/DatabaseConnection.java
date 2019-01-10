package jports.database;

import java.sql.Array;
import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.NClob;
import java.sql.PreparedStatement;
import java.sql.SQLClientInfoException;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.SQLXML;
import java.sql.Savepoint;
import java.sql.Statement;
import java.sql.Struct;
import java.util.LinkedList;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.Executor;

public class DatabaseConnection implements Connection {

	private final Connection connection;
	private final LinkedList<DatabaseConnection> pool;

	protected DatabaseConnection(Connection connection, LinkedList<DatabaseConnection> pool) {
		this.connection = connection;
		this.pool = pool;
	}

	public void terminate() throws SQLException {
		this.connection.close();
	}

	/**
	 * @deprecated The garbage collector invokes object finalizer methods after it
	 *             determines that the object is unreachable but before it reclaims
	 *             the object's storage. Execution of the finalizer provides an
	 *             opportunity to release resources such as open streams, files, and
	 *             network connections that might not otherwise be released
	 *             automatically through the normal action of the garbage collector.
	 */
	@Override
	@Deprecated(
			since = "1.9",
			forRemoval = true)
	protected void finalize() throws Throwable {
		terminate();
	}

	@Override
	public synchronized void close() throws SQLException {
		if (!connection.isClosed())
			pool.push(this);
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
