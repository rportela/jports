package jports.database;

import java.util.List;

import jports.data.Select;

public class DatabaseSelectClass<T> extends Select<T> {

	private Database database;
	private DatabaseAspect<T> aspect;

	public DatabaseSelectClass(Database database, DatabaseAspect<T> aspect) {
		this.database = database;
		this.aspect = aspect;
	}

	@Override
	public List<T> toList() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long count() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean exists() {
		// TODO Auto-generated method stub
		return false;
	}

}
