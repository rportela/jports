package jports.database;

import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.stream.Collectors;

import jports.data.ColumnType;

public class DatabaseInsertObject<T> {

	private final DatabaseAspect<T> aspect;
	private final Database database;
	private String commandText;
	private ArrayList<DatabaseAspectMember<T>> parameters;

	public DatabaseInsertObject(Database database, DatabaseAspect<T> aspect) {
		this.database = database;
		this.aspect = aspect;
	}

	private void prepare() {

		parameters = new ArrayList<>(aspect.size());
		for (DatabaseAspectMember<T> dam : aspect) {
			if (dam.getColumnType() != ColumnType.IDENTITY)
				parameters.add(dam);
		}

		int paramSize = parameters.size();

		DatabaseCommand command = database
				.createCommand()
				.appendSql("INSERT INTO ")
				.appendName(aspect.getObjectName())
				.appendSql(" (")
				.appendNames(parameters.stream().map(p -> p.getColumnName()).collect(Collectors.toList()))
				.appendSql(") VALUES (?");

		for (int i = 1; i < paramSize; i++)
			command.appendSql(", ?");

		command.appendSql(")");

		this.commandText = command.toString();

	}

}
