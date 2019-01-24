package jports.validations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@ValidationImplementation(MinNumberInclusiveValidation.class)
public @interface MinNumberInclusive {

	public double value();

	public String message() default "Value below the inclusive min of %f";
}
