package jports.database;

import java.util.List;
import java.util.Map;

import jports.data.Select;

public class DatabaseSelect extends Select<Map<String, Object>> {

	private final Database database;
	private final String objectName;

	public DatabaseSelect(Database database, String objectName) {
		this.database = database;
		this.objectName = objectName;
	}

	@Override
	public List<Map<String, Object>> toList() {
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
