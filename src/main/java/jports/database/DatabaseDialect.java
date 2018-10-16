package jports.database;

public abstract class DatabaseDialect {

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
	 * This class validates names for building SQL statements; Override this method
	 * to add custom or database specific name validation rules;
	 * 
	 * @param name
	 */
	public void validateNameOrRaiseException(String name) {
		if (name.contains("'") || name.contains("--"))
			throw new RuntimeException("Names cannot contain invalid chars: " + name);
	}

	/**
	 * Creates the default SQL insert command text using ? as parameter holder.
	 * Override this method to create custom or database specific insert command
	 * syntax;
	 * 
	 * @param table
	 * @param columns
	 * @return
	 */
	public String createInsertCommandText(String table, Iterable<String> columns) {

		validateNameOrRaiseException(table);

		StringBuilder sqlBuilder = new StringBuilder(512);
		StringBuilder valueBuilder = new StringBuilder(255);
		String namePrefix = getNamePrefix();
		String nameSuffix = getNameSuffix();
		boolean prependComma = false;

		sqlBuilder.append("INSERT INTO ");
		sqlBuilder.append(namePrefix);
		sqlBuilder.append(table);
		sqlBuilder.append(nameSuffix);
		sqlBuilder.append(" (");

		valueBuilder.append(") VALUES (");

		for (String column : columns) {

			validateNameOrRaiseException(column);

			if (prependComma) {
				sqlBuilder.append(", ");
				valueBuilder.append(", ");
			} else {
				prependComma = true;
			}

			sqlBuilder.append(namePrefix);
			sqlBuilder.append(column);
			sqlBuilder.append(nameSuffix);
			valueBuilder.append("?");
		}
		valueBuilder.append(")");
		sqlBuilder.append(valueBuilder.toString());
		return sqlBuilder.toString();
	}

}
