package jports.database;

import java.lang.reflect.Array;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import jports.ShowStopper;
import jports.data.Filter;
import jports.data.FilterComparison;
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
public class DatabaseCommand {

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
	 * Executes the command;
	 * 
	 * @return
	 * @throws SQLException
	 */
	public boolean execute() throws SQLException {
		try (Connection conn = database.getConnection()) {
			try (Statement statement = conn.createStatement()) {
				return statement.execute(text.toString());
			}
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
		try (Connection conn = database.getConnection()) {
			try (Statement statement = conn.createStatement()) {
				return statement.executeUpdate(text.toString());
			}
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
		try (Connection conn = database.getConnection()) {
			try (Statement statement = conn.createStatement()) {
				try (ResultSet rs = statement.executeQuery(text.toString())) {
					return adapter.process(rs);
				}
			}
		}
	}

	/**
	 * Executes a query and adapts the result set to a specific data type;
	 * 
	 * @param adapter
	 * @return
	 * @throws SQLException
	 */
	public <T> List<T> executeQuery(Class<T> dataType) throws SQLException {
		return executeQuery(new ResultSetToObjectList<>(DatabaseAspect.getInstance(dataType)));
	}

	/**
	 * Executes a query and adapts the result set to a specific data type;
	 * 
	 * @param adapter
	 * @return
	 * @throws SQLException
	 */
	public <T> T executeSingleResult(Class<T> dataType) throws SQLException {
		return executeQuery(new ResultSetToObject<>(DatabaseAspect.getInstance(dataType)));
	}

	/**
	 * Executes what possibly is an INSERT statement and returns the generated key.
	 * 
	 * @return
	 * @throws SQLException
	 */
	public Object executeWithGeneratedKey() throws SQLException {
		try (Connection connection = database.getConnection()) {
			try (Statement statement = connection.createStatement()) {
				statement.execute(text.toString(), Statement.RETURN_GENERATED_KEYS);
				try (ResultSet rs = statement.getGeneratedKeys()) {
					return rs.next()
							? rs.getObject(1)
							: null;
				}
			}
		}
	}

	/**
	 * Executes the query and returns the first column of the first row in the
	 * result set;
	 * 
	 * @return
	 * @throws SQLException
	 */
	public Object executeScalar() throws SQLException {
		Connection conn = database.getConnection();
		try {
			try (Statement statement = conn.createStatement()) {
				try (ResultSet rs = statement.executeQuery(text.toString())) {
					return rs.next()
							? rs.getObject(1)
							: null;
				}
			}
		} finally {
			conn.close();
		}
	}

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
		database.validateNameOrThrowException(name);
		text.append(database.getNamePrefix());
		text.append(name);
		text.append(database.getNameSuffix());
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
			} else {
				prependComma = true;
			}
			appendName(name);
		}
		return this;
	}

	/**
	 * Appends a list of names joined by a comma or an asterisk if the list is
	 * empty;
	 * 
	 * @param names
	 * @return
	 */
	public DatabaseCommand appendNamesOrAsterisk(List<String> names) {
		if (names.isEmpty()) {
			text.append("*");
			return this;
		} else {
			appendName(names.get(0));
			for (int i = 1; i < names.size(); i++) {
				text.append(", ");
				appendName(names.get(i));
			}
			return this;
		}
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
		text.append(value
				? "TRUE"
				: "FALSE");
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
	 * Appends a byte array to the underlying command text;
	 * 
	 * @param value
	 * @return
	 */
	public DatabaseCommand appendByteArray(byte[] value) {
		text.append("0x");
		char[] hexArray = "0123456789ABCDEF".toCharArray();
		char[] hexChars = new char[value.length * 2];
		for (int j = 0; j < value.length; j++) {
			int v = value[j] & 0xFF;
			hexChars[j * 2] = hexArray[v >>> 4];
			hexChars[j * 2 + 1] = hexArray[v & 0x0F];
		}
		text.append(hexChars);
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
		else if (value instanceof Calendar)
			return appendDate(((Calendar) value).getTime());
		else if (value instanceof UUID)
			return appendUUID((UUID) value);
		else if (value instanceof String)
			return appendString((String) value);
		else if (value instanceof byte[])
			return appendByteArray((byte[]) value);
		else if (value instanceof Iterable<?>)
			return appendValueList((Iterable<?>) value);
		else if (value.getClass().isArray())
			return appendValueArray(value);
		else
			throw new ShowStopper("Can't convert " + value + " to SQL.");
	}

	/**
	 * Appends a list of values to the underlying command text;
	 * 
	 * @param value
	 * @return
	 */
	public DatabaseCommand appendValueList(Iterable<?> value) {
		boolean prependComma = false;
		for (Object o : value) {
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
	public DatabaseCommand appendValueArray(Object array) {
		int l = Array.getLength(array);
		appendValue(Array.get(array, 0));
		for (int i = 1; i < l; i++) {
			text.append(", ");
			appendValue(Array.get(array, i));
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
			throw new ShowStopper("Unexpected filter type: " + filter);
		}
	}

	public DatabaseCommand appendEqualTo(String name, Object value) {
		appendName(name);
		if (value == null)
			text.append(" IS NULL");
		else {
			text.append(" = ");
			appendValue(value);
		}
		return this;
	}

	public DatabaseCommand appendNotEqualTo(String name, Object value) {
		appendName(name);
		if (value == null)
			text.append(" IS NOT NULL");
		else {
			text.append(" != ");
			appendValue(value);
		}
		return this;
	}

	public DatabaseCommand appendGreaterOrEqual(String name, Object value) {
		appendName(name);
		text.append(" >= ");
		appendValue(value);
		return this;
	}

	public DatabaseCommand appendGreaterThan(String name, Object value) {
		appendName(name);
		text.append(" > ");
		appendValue(value);
		return this;
	}

	public DatabaseCommand appendLowerOrEqual(String name, Object value) {
		appendName(name);
		text.append(" <= ");
		appendValue(value);
		return this;
	}

	public DatabaseCommand appendLowerThan(String name, Object value) {
		appendName(name);
		text.append(" < ");
		appendValue(value);
		return this;
	}

	public DatabaseCommand appendLike(String name, Object value) {
		appendName(name);
		text.append(" LIKE ");
		appendValue(value);
		return this;
	}

	public DatabaseCommand appendNotLike(String name, Object value) {
		appendName(name);
		text.append(" NOT LIKE ");
		appendValue(value);
		return this;
	}

	public DatabaseCommand appendNotIn(String name, Object value) {
		appendName(name);
		text.append(" NOT IN (");
		appendValue(value);
		text.append(")");
		return this;
	}

	public DatabaseCommand appendIn(String name, Object value) {
		appendName(name);
		text.append(" IN (");
		appendValue(value);
		text.append(")");
		return this;
	}

	public DatabaseCommand appendContains(String name, Object value) {
		return appendSql("CONTAINS(")
				.appendName(name)
				.appendSql(", ")
				.appendValue(value)
				.appendSql(")");
	}

	/**
	 * Appends a filter term to the underlying command text;
	 * 
	 * @param term
	 * @return
	 */
	public DatabaseCommand appendFilterTerm(FilterTerm term) {

		FilterComparison comparison = term.getComparison();
		Object value = term.getValue();
		String name = term.getName();

		switch (comparison) {
		case EQUAL_TO:
			return appendEqualTo(name, value);

		case GREATER_OR_EQUAL:
			return appendGreaterOrEqual(name, value);

		case GREATER_THAN:
			return appendGreaterThan(name, value);

		case LIKE:
			return appendLike(name, value);

		case LOWER_OR_EQUAL:
			return appendLowerOrEqual(name, value);

		case LOWER_THAN:
			return appendLowerThan(name, value);

		case NOT_EQUAL_TO:
			return appendNotEqualTo(name, value);

		case NOT_LIKE:
			return appendNotLike(name, value);

		case IN:
			return appendIn(name, value);

		case NOT_IN:
			return appendNotIn(name, value);

		case CONTAINS:
			return appendContains(name, value);

		default:
			throw new ShowStopper("Unexpected filter comparison: " + comparison);

		}

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
		for (FilterNode node = expression.first; node != null; node = node.getNext()) {
			if (op != null) {
				switch (op) {
				case AND:
					text.append(" AND ");
					break;
				case OR:
					text.append(" OR ");
					break;
				default:
					throw new ShowStopper("Unknown filter operation: " + op);
				}
			}
			appendFilter(node.getFilter());
			op = node.getOperation();
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
		for (SortNode node = sort.first; node != null; node = node.getNext()) {
			if (prependComma)
				text.append(", ");
			else
				prependComma = true;

			appendName(node.getName());
			switch (node.getDirection()) {
			case ASCENDING:
				break;
			case DESCENDING:
				text.append(" DESC");
				break;
			default:
				throw new ShowStopper("Unknown sort direction: " + node.getDirection() + " -> " + sort);
			}
		}
		return this;
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
		if (top > 0 && database.acceptsTop())
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
		if (offset > 0 && database.acceptsOffset())
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
		if (limit > 0 && database.acceptsLimit())
			this.text.append(" LIMIT " + limit);
		return this;
	}

	/**
	 * This method takes a map and appends a name=value sql command text;
	 * 
	 * @param values
	 * @return
	 */
	public DatabaseCommand appendNameValue(Map<String, Object> values) {
		boolean prependComma = false;
		for (Entry<String, Object> entry : values.entrySet()) {
			if (prependComma)
				text.append(", ");
			else
				prependComma = true;

			appendName(entry.getKey());
			text.append('=');
			appendValue(entry.getValue());
		}
		return this;
	}

	/**
	 * This method appends a SELECT statement to the underlying SQL command text;
	 * 
	 * @param objectName
	 * @param columns
	 * @param where
	 * @param orderBy
	 * @param offset
	 * @param limit
	 * @return
	 */
	public DatabaseCommand appendSelect(String objectName, List<String> columns, Filter where, Sort orderBy, int offset,
			int limit) {
		return appendSql("SELECT ")
				.appendTop(offset + limit)
				.appendNamesOrAsterisk(columns)
				.appendSql(" FROM ")
				.appendName(objectName)
				.appendWhere(where)
				.appendOrderBy(orderBy)
				.appendOffset(offset)
				.appendLimit(limit);
	}

	/**
	 * This method appends a DELETE statement to the underlying SQL command text;
	 * 
	 * @param tableName
	 * @param where
	 * @return
	 */
	public DatabaseCommand appendDelete(String tableName, Filter where) {
		return appendSql("DELETE FROM ")
				.appendName(tableName)
				.appendWhere(where);
	}

	/**
	 * This method appends an UPDATE statement to the underlying SQL command text;
	 * 
	 * @param tableName
	 * @param values
	 * @param where
	 * @return
	 */
	public DatabaseCommand appendUpdate(String tableName, Map<String, Object> values, Filter where) {
		return appendSql("UPDATE ")
				.appendName(tableName)
				.appendSql(" SET ")
				.appendNameValue(values)
				.appendWhere(where);

	}

	/**
	 * This method appends an INSERT statement to the underlying SQL command text;
	 * 
	 * @param tableName
	 * @param values
	 * @return
	 */
	public DatabaseCommand appendInsert(String tableName, Map<String, Object> values) {
		return appendSql("INSERT INTO ")
				.appendName(tableName)
				.appendSql(" (")
				.appendNames(values.keySet())
				.appendSql(") VALUES (")
				.appendValueList(values.values())
				.appendSql(")");
	}
}
