package jports.database;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jports.data.ColumnType;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface DatabaseColumn {

	public ColumnType type() default ColumnType.REGULAR;

	public String name() default "";

	public boolean readOnly() default false;
}
