package jports.validations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@ValidationImplementation(EmailValidation.class)
public @interface Email {

	public String value() default "Invalid e-mail address";
}
