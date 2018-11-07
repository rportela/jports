package jports.validations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Use this annotation to identify field members that should only contain valid
 * email addresses or that their content is empty;
 * 
 * @author rportela
 *
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@ValidationImplementation(EmailValidation.class)
public @interface Email {

	/**
	 * The default error message for invalid email addresses;
	 * 
	 * @return
	 */
	public String value() default "Invalid e-mail address";
}
