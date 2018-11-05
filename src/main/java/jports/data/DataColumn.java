package jports.data;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This is the default annotation of data members. It contains only the column
 * name and the column type. If no name is provided, the member name should be
 * used as column name and if no column type is provided, the default REGULAR
 * will be used;
 * 
 * @author rportela
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.FIELD, ElementType.METHOD })
public @interface DataColumn {

	/**
	 * Use this to set the column name;
	 * 
	 * @return
	 */
	public String name() default "";

	/**
	 * Use this to set the column type;
	 * 
	 * @return
	 */
	public ColumnType type() default ColumnType.REGULAR;
}
