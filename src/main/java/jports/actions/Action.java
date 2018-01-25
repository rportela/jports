package jports.actions;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * This class represents an action, that is, a specific way of doing things in
 * this framework that should be invoked by regular code or from the web and
 * also should provide specific data about the execution such as start and end
 * time. Also, as a programming paradigm, exceptions and faulty code will be
 * managed and presented as different result types of this action;
 * 
 * @author rportela
 *
 * @param <TParams>
 * @param <TResult>
 */
public class Action<TParams, TResult> {

	/**
	 * Gets the actual type arguments from the generic superclass of the
	 * implementer;
	 * 
	 * @param ordinal
	 * @return
	 */
	private Type[] getActualTypeArguments() {
		Class<?> claz = getClass();
		Type superclass = claz.getGenericSuperclass();
		ParameterizedType parameterized = (ParameterizedType) superclass;
		Type[] typeArguments = parameterized.getActualTypeArguments();
		return typeArguments;
	}

	/**
	 * Locates the generic super class and gets the actual type argument of the
	 * parameters;
	 * 
	 * @return
	 */
	public Type getParamsType() {
		return getActualTypeArguments()[0];
	}

	/**
	 * Gets the class of the parameterized TParams of this action; You can and
	 * should override this method with a specific class for optimization;
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Class<TParams> getParamsClass() {
		Type tp = getParamsType();
		if (tp instanceof ParameterizedType)
			tp = ((ParameterizedType) tp).getRawType();
		return (Class<TParams>) tp;
	}

	/**
	 * Locates the generic super class and gets the actual type argument of the
	 * result ;
	 * 
	 * @return
	 */
	public Type getResultType() {
		return getActualTypeArguments()[1];
	}

	/**
	 * Gets the class of the parameterized TResult of this action; You can and
	 * should override this method with a specific class to optimize;
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Class<TResult> getResultClass() {
		Type tp = getResultType();
		if (tp instanceof ParameterizedType)
			tp = ((ParameterizedType) tp).getRawType();
		return (Class<TResult>) tp;
	}

}
