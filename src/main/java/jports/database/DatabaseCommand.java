package jports.database;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

import jports.data.Filter;
import jports.data.FilterExpression;
import jports.data.FilterNode;
import jports.data.FilterOperation;
import jports.data.FilterTerm;
import jports.data.Sort;
import jports.data.SortNode;

/**
 * This class unites useful methods for creating database SQL commands;
 * 
 * @author rportela
 *
 */
public abstract class DatabaseCommand {

	/**
	 * The database that this command belong to;
	 */
	private final Database database;

	/**
	 * Creates a new instance of the database command;
	 * 
	 * @param database
	 */
	public DatabaseCommand(Database database) {
		this.database = database;
	}

	/**
	 * Holds the SQL Builder;
	 */
	private final StringBuilder text = new StringBuilder(512);

	/**
	 * Gets the SQL content;
	 */
	@Override
	public String toString() {
		return text.toString();
	}

	/**
	 * Executes the command against a database connection;
	 * 
	 * @param conn
	 * @return
	 * @throws SQLException
	 */
	public boolean execute(Connection conn) throws SQLException {
		Statement stmt = conn.createStatement();
		try {
			return stmt.execute(toString());
		} finally {
			stmt.close();
		}

	}

	/**
	 * Executes the command using the database passed in the command's constructor;
	 * 
	 * @return
	 * @throws SQLException
	 */
	public boolean execute() throws SQLException {
		Connection conn = database.getConnection();
		try {
			return execute(conn);
		} finally {
			conn.close();
		}
	}

	/**
	 * Executes an INSERT, UPDATE or DELETE and returns the number of records
	 * affected;
	 * 
	 * @param conn
	 * @return
	 * @throws SQLException
	 */
	public int executeNonQuery(Connection conn) throws SQLException {
		Statement stmt = conn.createStatement();
		try {
			return stmt.executeUpdate(toString());
		} finally {
			stmt.close();
		}
	}

	/**
	 * Executes an INSERT, UPDATE or DELETE and returns the number of records
	 * affected;
	 * 
	 * @return
	 * @throws SQLException
	 */
	public int executeNonQuery() throws SQLException {
		Connection conn = database.getConnection();
		try {
			return executeNonQuery(conn);
		} finally {
			conn.close();
		}

	}

	/**
	 * Executes a query and adapts the result set to a specific data type;
	 * 
	 * @param conn
	 * @param adapter
	 * @return
	 * @throws SQLException
	 */
	public <T> T executeQuery(Connection conn, ResultSetAdapter<T> adapter) throws SQLException {
		Statement stmt = conn.createStatement();
		try {
			ResultSet resultSet = stmt.executeQuery(toString());
			try {
				return adapter.process(resultSet);
			} finally {
				resultSet.close();
			}
		} finally {
			stmt.close();
		}
	}

	/**
	 * Executes a query and adapts the result set to a specific data type;
	 * 
	 * @param adapter
	 * @return
	 * @throws SQLException
	 */
	public <T> T executeQuery(ResultSetAdapter<T> adapter) throws SQLException {
		Connection conn = database.getConnection();
		try {
			return executeQuery(conn, adapter);
		} finally {
			conn.close();
		}

	}

	/**
	 * Executes what possibly is an INSERT statement and returns a map containing
	 * the generated keys;
	 * 
	 * @param conn
	 * @return
	 * @throws SQLException
	 */
	public Map<String, Object> executeWithGeneratedKeys(Connection conn) throws SQLException {
		Statement stmt = conn.createStatement();
		try {
			stmt.execute(toString(), Statement.RETURN_GENERATED_KEYS);
			ResultSet rs = stmt.getGeneratedKeys();
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
		} finally {
			stmt.close();
		}
	}

	/**
	 * Executes what possibly is an INSERT statement and returns a map containing
	 * the generated keys;
	 * 
	 * @return
	 * @throws SQLException
	 */
	public Map<String, Object> executeWithGeneratedKeys() throws SQLException {
		Connection conn = database.getConnection();
		try {
			return executeWithGeneratedKeys(conn);
		} finally {
			conn.close();
		}

	}

	/**
	 * Validates a name for invalid chars and throws an exception if the name is not
	 * valid.
	 * 
	 * @param name
	 */
	public void validateNameOrThrowException(String name) {
		if (name.contains("'") || name.contains("--"))
			throw new RuntimeException("Invalid database object name: " + name);
	}

	/**
	 * Gets the database specific name prefix like [ or `;
	 * 
	 * @return
	 */
	public abstract String getNamePrefix();

	/**
	 * Gets the database specific name suffix like ] or �;
	 * 
	 * @return
	 */
	public abstract String getNameSuffix();

	/**
	 * Appends raw SQL to the underlying command text;
	 * 
	 * @param sql
	 * @return
	 */
	public DatabaseCommand appendSql(String sql) {
		this.text.append(sql);
		return this;
	}

	/**
	 * Appends a name to the underlying command text;
	 * 
	 * @param name
	 * @return
	 */
	public DatabaseCommand appendName(String name) {
		validateNameOrThrowException(name);
		text.append(getNamePrefix());
		text.append(name);
		text.append(getNameSuffix());
		return this;
	}

	/**
	 * Appends a list of names to the underlying command text;
	 * 
	 * @param names
	 * @return
	 */
	public DatabaseCommand appendNames(String... names) {
		appendName(names[0]);
		for (int i = 1; i < names.length; i++) {
			text.append(", ");
			appendName(names[i]);
		}
		return this;
	}

	/**
	 * Appends a list of names to the underlying command text;
	 * 
	 * @param names
	 * @return
	 */
	public DatabaseCommand appendNames(Iterable<String> names) {
		boolean prependComma = false;
		for (String name : names) {
			if (prependComma) {
				text.append(", ");
			} else
				prependComma = true;
			appendName(name);
		}
		return this;
	}

	/**
	 * Appends a number to the underlying command text;
	 * 
	 * @param value
	 * @return
	 */
	public DatabaseCommand appendNumber(Number value) {
		text.append(value);
		return this;
	}

	/**
	 * Appends a date to the underlying command text;
	 * 
	 * @param value
	 * @return
	 */
	public DatabaseCommand appendDate(Date value) {
		return appendTimestamp(new Timestamp(value.getTime()));
	}

	/**
	 * Appends a date to the underlying command text;
	 * 
	 * @param value
	 * @return
	 */
	public DatabaseCommand appendDate(java.sql.Date value) {
		text.append("'");
		text.append(value);
		text.append("'");
		return this;
	}

	/**
	 * Appends a TimeStamp to the underlying command text;
	 * 
	 * @param value
	 * @return
	 */
	public DatabaseCommand appendTimestamp(Timestamp value) {
		text.append("'");
		text.append(value);
		text.append("'");
		return this;
	}

	/**
	 * Appends a Time to the underlying command text;
	 * 
	 * @param value
	 * @return
	 */
	public DatabaseCommand appendTime(Time value) {
		text.append("'");
		text.append(value);
		text.append("'");
		return this;
	}

	/**
	 * Appends an UUID to the underlying command text;
	 * 
	 * @param value
	 * @return
	 */
	public DatabaseCommand appendUUID(UUID value) {
		text.append("'");
		text.append(value);
		text.append("'");
		return this;
	}

	/**
	 * Appends a boolean to the underlying command text;
	 * 
	 * @param value
	 * @return
	 */
	public DatabaseCommand appendBoolean(Boolean value) {
		text.append(value ?
				"TRUE" :
				"FALSE");
		return this;
	}

	/**
	 * Appends null to the underlying command text;
	 * 
	 * @return
	 */
	public DatabaseCommand appendNull() {
		text.append("NULL");
		return this;
	}

	/**
	 * Appends a string to the underlying command text;
	 * 
	 * @param value
	 * @return
	 */
	public DatabaseCommand appendString(String value) {
		text.append("'");
		text.append(value.replaceAll("'", "''"));
		text.append("'");
		return this;
	}

	/**
	 * Appends a value to the underlying command text;
	 * 
	 * @param value
	 * @return
	 */
	public DatabaseCommand appendValue(Object value) {
		if (value == null)
			return appendNull();
		else if (value instanceof Number)
			return appendNumber((Number) value);
		else if (value instanceof Date)
			return appendDate((Date) value);
		else if (value instanceof Boolean)
			return appendBoolean((Boolean) value);
		else if (value instanceof java.sql.Date)
			return appendDate((java.sql.Date) value);
		else if (value instanceof Timestamp)
			return appendTimestamp((Timestamp) value);
		else if (value instanceof Time)
			return appendTime((Time) value);
		else if (value instanceof UUID)
			return appendUUID((UUID) value);
		else if (value instanceof String)
			return appendString((String) value);
		else
			throw new RuntimeException("Can't convert " + value + " to SQL.");
	}

	/**
	 * Appends a list of values to the underlying command text;
	 * 
	 * @param values
	 * @return
	 */
	public DatabaseCommand appendValues(Iterable<Object> values) {
		boolean prependComma = false;
		for (Object o : values) {
			if (prependComma)
				text.append(", ");
			else
				prependComma = true;
			appendValue(o);
		}
		return this;
	}

	/**
	 * Appends an array of values to the underlying command text;
	 * 
	 * @param values
	 * @return
	 */
	public DatabaseCommand appendValues(Object... values) {
		appendValue(values[0]);
		for (int i = 1; i < values.length; i++) {
			text.append(", ");
			appendValue(values[i]);
		}
		return this;
	}

	/**
	 * Appends a filter to the underlying command text;
	 * 
	 * @param filter
	 * @return
	 */
	public DatabaseCommand appendFilter(Filter filter) {
		switch (filter.getFilterType()) {
		case EXPRESSION:
			return appendFilterExpression((FilterExpression) filter);
		case TERM:
			return appendFilterTerm((FilterTerm) filter);
		default:
			throw new RuntimeException("Unexpected filter type: " + filter);
		}
	}

	/**
	 * Appends a filter term to the underlying command text;
	 * 
	 * @param term
	 * @return
	 */
	public DatabaseCommand appendFilterTerm(FilterTerm term) {

		appendName(term.name);
		switch (term.comparison) {
		case EQUAL_TO:
			if (term.value == null)
				text.append(" IS NULL");
			else {
				text.append(" = ");
				appendValue(term.value);
			}
			break;
		case GRATER_OR_EQUAL:
			text.append(" >= ");
			appendValue(term.value);
			break;
		case GREATER_THAN:
			text.append(" > ");
			appendValue(term.value);
			break;
		case LIKE:
			text.append(" LIKE ");
			appendValue(term.value);
			break;
		case LOWER_OR_EQUAL:
			text.append(" <= ");
			appendValue(term.value);
			break;
		case LOWER_THAN:
			text.append(" < ");
			appendValue(term.value);
			break;
		case NOT_EQUAL_TO:
			if (term.value == null) {
				text.append(" IS NOT NULL");
			} else {
				text.append(" != ");
				appendValue(term.value);
			}
			break;
		case NOT_LIKE:
			text.append(" NOT LIKE ");
			appendValue(term.value);
			break;
		default:
			throw new RuntimeException("Unexpected filter comparison: " + term.comparison);

		}
		return this;
	}

	/**
	 * Appends a filter expression to the underlying command text;
	 * 
	 * @param expression
	 * @return
	 */
	public DatabaseCommand appendFilterExpression(FilterExpression expression) {
		text.append("(");
		FilterOperation op = null;
		for (FilterNode node = expression.first; node != null; node = node.next) {
			if (op != null) {
				switch (op) {
				case AND:
					text.append(" AND ");
					break;
				case OR:
					text.append(" OR ");
					break;
				default:
					throw new RuntimeException("Unknown filter operation: " + op);
				}
			}
			appendFilter(node.filter);
			op = node.operation;
		}
		text.append(")");
		return this;
	}

	/**
	 * Appends a WHERE clause if the given filter is not null. Just returns an
	 * instance to "self" otherwise;
	 * 
	 * @param filter
	 * @return
	 */
	public DatabaseCommand appendWhere(Filter filter) {
		if (filter == null)
			return this;

		text.append(" WHERE ");
		return appendFilter(filter);
	}

	/**
	 * Appends an ORDER BY clause if the given sort expression is not null. It just
	 * returns an instance to "self" otherwise;
	 * 
	 * @param sort
	 * @return
	 */
	public DatabaseCommand appendOrderBy(Sort sort) {
		if (sort == null)
			return this;

		text.append(" ORDER BY ");
		boolean prependComma = false;
		for (SortNode node = sort.first; node != null; node = node.next) {
			if (prependComma)
				text.append(", ");
			else
				prependComma = true;

			appendName(node.name);
			switch (node.direction) {
			case ASCENDING:
				break;
			case DESCENDING:
				text.append(" DESC");
				break;
			default:
				throw new RuntimeException("Unknown sort direction: " + node.direction + " -> " + sort);
			}
		}
		return this;
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
	 * This method was added for compatibility with SQL server TOP statement. The
	 * "acceptsTop" is checked and the statement is added to the underlying command
	 * text;
	 * 
	 * @param top
	 * @return
	 */
	public DatabaseCommand appendTop(int top) {
		if (top > 0 && acceptsTop())
			this.text.append("TOP " + top);
		return this;
	}

	/**
	 * This method was added for compatibility with postgres and mysql OFFSET
	 * statement. The "acceptsOffset" is checked and the OFFSET statement is added
	 * to the underlying command text;
	 * 
	 * @param offset
	 * @return
	 */
	public DatabaseCommand appendOffset(int offset) {
		if (offset > 0 && acceptsOffset())
			this.text.append(" OFFSET " + offset);
		return this;
	}

	/**
	 * This method was added for compatibility with postgres and mysql LIMIT
	 * statement. The "accepltsLimit" is checked and the LIMIT statement is added to
	 * the underlying command text;
	 * 
	 * @param limit
	 * @return
	 */
	public DatabaseCommand appendLimit(int limit) {
		if (limit > 0 && acceptsLimit())
			this.text.append(" LIMIT " + limit);
		return this;
	}

}
