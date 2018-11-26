package jports.database.postgres;

import java.sql.Statement;

import jports.database.DatabaseCommand;

public class PostgresCommand extends DatabaseCommand {

	public PostgresCommand(Statement statement) {
		super(statement);
	}

	@Override
	public String getNamePrefix() {
		return "\"";
	}

	@Override
	public String getNameSuffix() {
		return "\"";
	}

	@Override
	public boolean acceptsLimit() {
		return true;
	}

	@Override
	public boolean acceptsOffset() {
		return true;
	}

	@Override
	public DatabaseCommand appendLike(String name, Object value) {
		appendName(name);
		appendSql(" ILIKE ");
		appendValue(value);
		return this;
	}

	@Override
	public DatabaseCommand appendNotLike(String name, Object value) {
		appendName(name);
		appendSql(" NOT ILIKE ");
		appendValue(value);
		return this;
	}

}
