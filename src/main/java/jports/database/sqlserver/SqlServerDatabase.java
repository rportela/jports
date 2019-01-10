package jports.database.sqlserver;

import java.util.Properties;

import jports.database.Database;

public class SqlServerDatabase extends Database {

	public SqlServerDatabase(String jdbcUrl, Properties properties) throws ClassNotFoundException {
		super(jdbcUrl, properties);
	}

	public SqlServerDatabase(String jdbcUrl) throws ClassNotFoundException {
		super(jdbcUrl);
	}

	@Override
	public String getDriverClass() {
		return "com.microsoft.sqlserver.jdbc.SQLServerDriver";
	}

	@Override
	public String getNamePrefix() {
		return "[";
	}

	@Override
	public String getNameSuffix() {
		return "]";
	}

}
