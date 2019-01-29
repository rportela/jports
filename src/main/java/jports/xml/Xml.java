package jports.xml;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jports.adapters.VoidAdapter;

@Target({ ElementType.FIELD, ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface Xml {

	public XmlType type() default XmlType.ELEMENT;

	public String name() default "";

	public Class<?> adapter() default VoidAdapter.class;

	public String pattern() default "";

}
