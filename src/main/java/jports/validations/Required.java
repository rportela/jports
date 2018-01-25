package jports.validations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Tells the compiler and the runtime that a specific field is required;
 * 
 * @author rportela
 *
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@ValidationImplementation(RequiredValidation.class)
public @interface Required {

	/**
	 * The message that should be presented when this validation fails;
	 * 
	 * @return
	 */
	public String value() default "Required";
}
