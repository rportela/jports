package jports.actions;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Date;
import java.util.Map;

import jports.GenericLogger;
import jports.validations.ValidationAspect;
import jports.validations.ValidationResult;

/**
 * This class represents an action, that is, a specific way of doing things in
 * this framework that should be invoked by regular code or from the web and
 * also should provide specific data about the execution such as start and end
 * time. Also, as a programming paradigm, exceptions and faulty code will be
 * managed and presented as different result types of this action;
 * 
 * @author rportela
 *
 * @param <T>
 * @param <R>
 */
public abstract class Action<T, R> {

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
		return parameterized.getActualTypeArguments();

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
	public Class<T> getParamsClass() {
		Type tp = getParamsType();
		if (tp instanceof ParameterizedType)
			tp = ((ParameterizedType) tp).getRawType();
		return (Class<T>) tp;
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
	public Class<R> getResultClass() {
		Type tp = getResultType();
		if (tp instanceof ParameterizedType)
			tp = ((ParameterizedType) tp).getRawType();
		return (Class<R>) tp;
	}

	/**
	 * This method is executed after validation, authentication and the main flow.
	 * Override this to add custom logging or anything that should happen after the
	 * desired flow;
	 * 
	 * @param execution
	 */
	protected void postFlow(ActionExecution<T, R> execution) {
		// override to add new functionality
	}

	/**
	 * This method uses a validation aspect to look for annotations and to validate
	 * the entire parameters class. Override this method to add custom validations
	 * to your action;
	 * 
	 * @param execution
	 */
	protected void validate(ActionExecution<T, R> execution) {
		Class<T> paramsClass = getParamsClass();
		if (execution.getParams() == null) {
			if (Void.class.equals(paramsClass))
				return;

			execution.setValidation(new ValidationResult(
					"params",
					false,
					"Only void parameters allow nulls. Wrap your primitives or string in a class."))
					.setResultType(ActionResultType.VALIDATION_FAILED);

		} else {
			ValidationAspect<T> validationAspect = ValidationAspect.getInstance(paramsClass);
			ValidationResult validationResult = validationAspect.validate("params", execution.getParams());
			if (!validationResult.isValid()) {
				execution
						.setValidation(validationResult)
						.setResultType(ActionResultType.VALIDATION_FAILED);
			}
		}
	}

	/**
	 * This method should perform authentication. The default implementations simply
	 * does nothing. Override this method to add custom authentication to your
	 * action;
	 * 
	 * @param execution
	 */
	protected void authenticate(ActionExecution<T, R> execution) throws Exception {

	}

	/**
	 * This is the action body. Override this method to add an implementation to
	 * your action;
	 * 
	 * @param execution
	 * @throws Exception
	 */
	protected abstract void mainFlow(ActionExecution<T, R> execution) throws Exception;

	/**
	 * Actually executes the use case using values set in the execution wrapper;
	 * 
	 * @param execution
	 */
	private final synchronized void execute(ActionExecution<T, R> execution) {
		if (execution.getName() == null) {
			execution.setName(getClass().getName());
		}
		try {
			validate(execution);
			if (execution.getResultType() == ActionResultType.NOT_EXECUTED) {
				authenticate(execution);
				if (execution.getResultType() == ActionResultType.NOT_EXECUTED) {
					mainFlow(execution);
				}

			}
		} catch (Exception e) {
			GenericLogger.error(getClass(), e);
			execution
					.setException(e)
					.setFailMessage(e.getMessage())
					.setResultType(ActionResultType.EXCEPTION_RAISED);
		}
		execution.setExecutionEnd(new Date());
		try {
			postFlow(execution);
		} catch (Exception e) {
			GenericLogger.info(getClass(), e);
		}
	}

	/**
	 * This method creates an execution and runs the validation, authentication,
	 * mainFlow and postFlow of this action;
	 * 
	 * @param params
	 * @param headers
	 * @return
	 */
	public final synchronized ActionExecution<T, R> execute(T params, Map<String, Object> headers) {

		ActionExecution<T, R> execution = new ActionExecution<>();
		execution
				.setHeaders(headers)
				.setParams(params);
		execute(execution);
		return execution;
	}

	/**
	 * Executes the action with the given parameters and no headers;
	 * 
	 * @param params
	 * @return
	 */
	public ActionExecution<T, R> execute(T params) {
		return execute(params, null);
	}

	/**
	 * Gets the default JSON action writer. Override this method to provide binary,
	 * text or html serialization options;
	 * 
	 * @return
	 */
	public HttpActionWriter<T, R> getHttpWriter() {
		return new HttpActionExecutionWriterForJson<>();
	}

	/**
	 * Executes an action with void parameters and write the output to System.out;
	 * 
	 * @param action
	 */
	public static void run(Action<Void, ?> action) {
		run(action, (Void) null);
	}

	/**
	 * Executes an action with void parameters and write the output to System.out;
	 * 
	 * @param action
	 */
	public static <T> void run(Action<T, ?> action, T parameters) {

		ActionExecution<T, ?> execution = action.execute(parameters);
		GenericLogger.info(action.getClass(), execution);
	}

}
