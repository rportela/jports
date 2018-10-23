package jports.text;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface CsvTable {

	public String separator() default ",";

	public String charset() default "";

	public String commentQualifier() default "";

	public int capacity() default 100;

	public boolean firstRowHasNames() default true;
}
