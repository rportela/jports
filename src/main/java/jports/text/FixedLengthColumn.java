package jports.text;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jports.adapters.Adapter;
import jports.adapters.VoidAdapter;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface FixedLengthColumn {

	public int start();

	public int end();

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
}
