package jports.database;

import jports.data.Select;

public class DatabaseSelect<T> extends Select<T> {

	private Database database;
	private DatabaseAspect<T> aspect;
}
