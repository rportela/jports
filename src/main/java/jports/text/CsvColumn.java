package jports.text;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jports.adapters.Adapter;
import jports.adapters.VoidAdapter;

/**
 * Use this annotation to mark public fields on your class that should be read
 * by the CSV parser;
 * 
 * @author rportela
 *
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface CsvColumn {

	/**
	 * The name in case your CSV has column names;
	 * 
	 * @return
	 */
	public String name() default "";

	/**
	 * The pattern of a date, the initializer of a specific parser etc.
	 * 
	 * @return
	 */
	public String pattern() default "";

	/**
	 * Any specific adapter to use when parsing the column. If none is provided, the
	 * default Adapter for the data type will be located;
	 * 
	 * @return
	 */
	public Class<? extends Adapter<?>> adapter() default VoidAdapter.class;

	/**
	 * The field position if your CSV doesen't have column names. If none is
	 * provided, the field order will be used;
	 * 
	 * @return
	 */
	public int position() default -1;

}
