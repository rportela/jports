package jports.adapters;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface AdapterInfo {

	/**
	 * Use this to add a custom adapter to your field or method. If left with
	 * default, an Adapter will be looked up on the Adapter Factory.
	 * 
	 * @return
	 */
	public Class<? extends Adapter<?>> adapter() default VoidAdapter.class;

	/**
	 * A pattern to be used by the adapter.
	 * 
	 * @return
	 */
	public String pattern() default "";
}
