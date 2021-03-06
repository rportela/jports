package jports.data;

import java.util.List;

/***
 * A column schema. It knows everything about the columns; This interface
 * exposes methods for locating columns, identities, unique columns and
 * composite key;
 * 
 * @author rportela
 *
 */
public interface ColumnSchema<T> {

	/**
	 * The number of columns in this schema;
	 * 
	 * @return
	 */
	public int getColumnCount();

	/**
	 * Gets the ordinal position of a column by it's column name.
	 * 
	 * @param name
	 * @return
	 */
	public int getColumnOrdinal(String name);

	/**
	 * Gets all columns in this schema;
	 * 
	 * @return
	 */
	public List<T> getColumns();

	/**
	 * Gets a column by it's ordinal position in the column list;
	 * 
	 * @param ordinal
	 * @return
	 */
	public T getColumn(int ordinal);

	/*
	 * Gets a column by it's column name
	 */
	public T getColumn(String name);

	/**
	 * Gets the identity column of this schema
	 * 
	 * @return
	 */
	public T getIdentity();

	/**
	 * Gets a list of unique columns in this schema
	 * 
	 * @return
	 */
	public List<T> getUniqueColumns();

	/**
	 * Gets the list of columns that are member of a composite key
	 * 
	 * @return
	 */
	public List<T> getCompositeKey();

	/**
	 * Gets the name of this column schema
	 * 
	 * @return
	 */
	public String getName();

}
