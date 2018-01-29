package jports.data;

/**
 * A column abstraction that can read and write values to a row;
 * 
 * @author rportela
 *
 * @param <TRow>
 */
public interface Column<TRow> {

	/**
	 * The name of the column; If in an aspect this is the name of the member;
	 * 
	 * @return
	 */
	public String getName();

	/**
	 * The data type of the column;
	 * 
	 * @return
	 */
	public Class<?> getDataType();

	/**
	 * The column type. This method has been rewritten to make sure that a column
	 * has only one column type;
	 * 
	 * @return
	 */
	public ColumnType getColumnType();

	/**
	 * Gets the value of this column from a specific row;
	 * 
	 * @param row
	 * @return
	 */
	public Object getValue(TRow row);

	/**
	 * Sets the value of this column to a specific row;
	 * 
	 * @param row
	 * @param value
	 */
	public void setValue(TRow row, Object value);
}
