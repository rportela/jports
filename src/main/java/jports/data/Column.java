package jports.data;

/**
 * A super simple column abstraction;
 * 
 * @author rportela
 *
 */
public interface Column {

	/**
	 * The name of the column; If in an aspect this is the name of the member;
	 * 
	 * @return
	 */
	public String getColumnName();

	/**
	 * The data type of the column;
	 * 
	 * @return
	 */
	public Class<?> getDataType();

	/**
	 * The column type. This method has been rewritten to make sure that a column
	 * has only one column type that simplifies developer understanding;
	 * 
	 * @return
	 */
	public ColumnType getColumnType();

}
