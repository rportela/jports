package jports.data;

/***
 * A column schema. It knows everything about the columns;
 * 
 * @author rportela
 *
 */
public interface ColumnSchema<TEntity, TColumn>  {

	/**
	 * The number of columns in this schema;
	 * @return
	 */
	public int getColumnCount();
}
