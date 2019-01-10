package jports.database.mysql;

import java.util.Properties;

import jports.database.Database;

public class MySqlDatabase extends Database {

	public MySqlDatabase(String jdbcUrl, Properties properties) throws ClassNotFoundException {
		super(jdbcUrl, properties);
	}

	public MySqlDatabase(String jdbcUrl) throws ClassNotFoundException {
		super(jdbcUrl);
	}

	@Override
	public String getDriverClass() {
		return "com.mysql.jdbc.Driver";
	}

	@Override
	public String getNamePrefix() {
		return "`";
	}

	@Override
	public String getNameSuffix() {
		return "`";
	}

}
