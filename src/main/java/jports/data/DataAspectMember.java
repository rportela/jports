package jports.data;

import jports.reflection.AspectMember;
import jports.reflection.AspectMemberAccessor;

/**
 * This is the main wrapper of members in a data class. A data class usually
 * have it's members annotated with some descendant of the DataColumn annotation
 * or is constructed by providing specific column type and column name
 * parameters for each member. This class is used to read and write values to
 * data entities.
 * 
 * @author rportela
 *
 * @param <TClass>
 */
public class DataAspectMember<TClass> extends AspectMember<TClass> implements Column {

	private ColumnType column_type;
	private String column_name;

	/**
	 * Creates a new instance of the data aspect member.
	 * 
	 * @param accessor
	 * @param column_type
	 * @param column_name
	 */
	public DataAspectMember(AspectMemberAccessor<TClass> accessor, ColumnType column_type, String column_name) {
		super(accessor);
		this.column_type = column_type == null
				? ColumnType.REGULAR
				: column_type;
		this.column_name = column_name == null || column_name.isEmpty()
				? accessor.getName()
				: column_name;
	}

	/**
	 * Creates a new instance of the data aspect member class.
	 * 
	 * @param accessor
	 * @param column
	 */
	public DataAspectMember(AspectMemberAccessor<TClass> accessor, DataColumn column) {
		this(accessor, column.type(), column.name());
	}

	/**
	 * Creates a new instance of the data aspect member class using the member name
	 * as column name and the default REGULAR column type;
	 * 
	 * @param accessor
	 */
	public DataAspectMember(AspectMemberAccessor<TClass> accessor) {
		this(accessor, null, null);
	}

	/**
	 * Gets the column name of this data member;
	 */
	@Override
	public String getColumnName() {
		return this.column_name;
	}

	/**
	 * Gets the column type if this data member;
	 */
	@Override
	public ColumnType getColumnType() {
		return this.column_type;
	}

	/**
	 * Sets the column name of this data member;
	 * 
	 * @param name
	 */
	public void setColumnName(String name) {
		this.column_name = name;
	}

	/**
	 * Sets the column type of this data member;
	 * 
	 * @param type
	 */
	public void setColumnType(ColumnType type) {
		this.column_type = type;
	}

	/**
	 * Gets a string representing this data aspect member;
	 */
	@Override
	public String toString() {
		return String.format("%s %s %s", this.column_name, this.column_type, this.getDataType());
	}

}
