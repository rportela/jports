package jports.adapters;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation hints the factory that a specific adapter can be extended.
 * That is, if someone registers another adapter of that data type, the registry
 * should be overwritten. Otherwise, the registry should raise an exception;
 * 
 * @author rportela
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface ExtensibleAdapter {

}
