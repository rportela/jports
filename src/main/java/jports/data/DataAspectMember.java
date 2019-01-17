package jports.data;

import jports.adapters.Adapter;
import jports.adapters.AdapterFactory;
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
 * @param <T>
 */
public class DataAspectMember<T> extends AspectMember<T> implements Column {

	private ColumnType columnType;
	private String columnName;
	private Adapter<?> adapter;
	private boolean readOnly;

	/**
	 * Creates a new instance of the data aspect member.
	 * 
	 * @param accessor
	 * @param columnType
	 * @param columnName
	 * @param adapterClass
	 * @param pattern
	 * @param readOnly
	 */
	public DataAspectMember(
			AspectMemberAccessor<T> accessor,
			ColumnType columnType,
			String columnName,
			Class<?> adapterClass,
			String pattern,
			boolean readOnly) {
		super(accessor);
		this.columnType = columnType == null
				? ColumnType.REGULAR
				: columnType;
		this.columnName = columnName == null || columnName.isEmpty()
				? accessor.getName()
				: columnName;
		this.adapter = AdapterFactory.createAdapter(accessor.getDataType(), adapterClass, pattern);
		this.readOnly = readOnly;
	}

	/**
	 * Creates a new instance of the data aspect member without adapterClass and
	 * pattern
	 * 
	 * @param accessor
	 * @param columnType
	 * @param columnName
	 * 
	 * @author Giovanna Marinelli
	 */
	public DataAspectMember(
			AspectMemberAccessor<T> accessor,
			ColumnType columnType,
			String columnName) {
		this(accessor, columnType, columnName, null, null, false);
	}

	/**
	 * Creates a new instance of the data aspect member class.
	 * 
	 * @param accessor
	 * @param column
	 */
	public DataAspectMember(AspectMemberAccessor<T> accessor, DataColumn column) {
		this(accessor, column.type(), column.name(), column.adapter(), column.format(), column.readOnly());
	}

	/**
	 * Creates a new instance of the data aspect member class using the member name
	 * as column name and the default REGULAR column type;
	 * 
	 * @param accessor
	 */
	public DataAspectMember(AspectMemberAccessor<T> accessor) {
		this(accessor, null, null, null, null, false);
	}

	/**
	 * Gets the column name of this data member;
	 */
	@Override
	public String getColumnName() {
		return this.columnName;
	}

	/**
	 * Gets the column type if this data member;
	 */
	@Override
	public ColumnType getColumnType() {
		return this.columnType;
	}

	/**
	 * Sets the column name of this data member;
	 * 
	 * @param name
	 */
	public void setColumnName(String name) {
		this.columnName = name;
	}

	/**
	 * Sets the column type of this data member;
	 * 
	 * @param type
	 */
	public void setColumnType(ColumnType type) {
		this.columnType = type;
	}

	/**
	 * Gets a string representing this data aspect member;
	 */
	@Override
	public String toString() {
		return String.format("%s %s %s", this.columnName, this.columnType, this.getDataType());
	}

	/**
	 * Uses the adapter to convert the value to the expected target value of the
	 * member accessor;
	 */
	@Override
	public void setValue(T target, Object value) {
		value = this.adapter == null
				? value
				: this.adapter.convert(value);
		super.setValue(target, value);
	}

	public boolean isReadOnly() {
		return this.readOnly;
	}

	public void setReadOnly(boolean value) {
		this.readOnly = value;
	}
}
