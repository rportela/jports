package jports.data;

import java.util.List;

/***
 * A column schema. It knows everything about the columns;
 * 
 * @author rportela
 *
 */
public interface ColumnSchema<TColumn> {

	/**
	 * The number of columns in this schema;
	 * 
	 * @return
	 */
	public int getColumnCount();

	public int getColumnOrdinal(String name);

	public List<TColumn> getColumns();

	public TColumn getColumn(int ordinal);

	public TColumn getColumn(String name);

	public TColumn getIdentity();

	public List<TColumn> getUniqueColumns();

	public List<TColumn> getCompositeKey();

	public String getName();

}
