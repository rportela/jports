package jports.actions;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Date;
import java.util.Map;

import com.google.gson.GsonBuilder;

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
 * @param <TParams>
 * @param <TResult>
 */
public abstract class Action<TParams, TResult> {

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

	/**
	 * This method is executed after validation, authentication and the main flow.
	 * Override this to add custom logging or anything that should happen after the
	 * desired flow;
	 * 
	 * @param execution
	 */
	protected void postFlow(ActionExecution<TParams, TResult> execution) {
		// override to add new functionality;
	}

	/**
	 * This method uses a validation aspect to look for annotations and to validate
	 * the entire parameters class. Override this method to add custom validations
	 * to your action;
	 * 
	 * @param execution
	 */
	protected void validate(ActionExecution<TParams, TResult> execution) throws Exception {
		Class<TParams> paramsClass = getParamsClass();
		if (execution.params == null) {
			if (Void.class.equals(paramsClass))
				return;

			execution.validation = new ValidationResult(
					"params",
					false,
					"Only void parameters allow nulls. Wrap your primitives or string in a class.");
			execution.result_type = ActionResultType.VALIDATION_FAILED;
			return;

		} else {
			ValidationAspect<TParams> validationAspect = ValidationAspect.getInstance(paramsClass);
			execution.validation = validationAspect.validate("params", execution.params);
			if (!execution.validation.isValid)
				execution.result_type = ActionResultType.VALIDATION_FAILED;
		}
	}

	/**
	 * This method should perform authentication. The default implementations simply
	 * does nothing. Override this method to add custom authentication to your
	 * action;
	 * 
	 * @param execution
	 */
	protected void authenticate(ActionExecution<TParams, TResult> execution) throws Exception {

	}

	/**
	 * This is the action body. Override this method to add an implementation to
	 * your action;
	 * 
	 * @param execution
	 * @throws Exception
	 */
	protected abstract void mainFlow(ActionExecution<TParams, TResult> execution) throws Exception;

	/**
	 * Actually executes the use case using values set in que execution wrapper;
	 * 
	 * @param execution
	 */
	private synchronized final void execute(ActionExecution<TParams, TResult> execution) {
		if (execution.name == null) {
			execution.name = getClass().toString();
		}
		try {
			validate(execution);
			if (execution.result_type == ActionResultType.NOT_EXECUTED) {
				authenticate(execution);
				if (execution.result_type == ActionResultType.NOT_EXECUTED) {
					mainFlow(execution);
				}

			}
		} catch (Exception e) {
			execution.exception = e;
			execution.result_type = ActionResultType.EXCEPTION_RAISED;
		}
		execution.execution_end = new Date();
		postFlow(execution);
	}

	/**
	 * This method creates an execution and runs the validation, authentication,
	 * mainFlow and postFlow of this action;
	 * 
	 * @param params
	 * @param headers
	 * @return
	 */
	public synchronized final ActionExecution<TParams, TResult> execute(TParams params, Map<String, Object> headers) {
		ActionExecution<TParams, TResult> execution = new ActionExecution<TParams, TResult>();
		execution.headers = headers;
		execution.params = params;
		execute(execution);
		return execution;
	}

	/**
	 * Executes the action with the given parameters and no headers;
	 * 
	 * @param params
	 * @return
	 */
	public ActionExecution<TParams, TResult> execute(TParams params) {
		return execute(params, null);
	}

	/**
	 * Gets the default JSON action writer. Override this method to provide binary,
	 * text or html serialization options;
	 * 
	 * @return
	 */
	public HttpActionWriter<TParams, TResult> getHttpWriter() {
		return new HttpActionWriterForJson<>();
	}

	/**
	 * Executes an action with void parameters and write the output to System.out;
	 * 
	 * @param action
	 */
	public static void run(Action<Void, ?> action) {
		ActionExecution<Void, ?> execution = action.execute((Void) null);
		new GsonBuilder()
				.setPrettyPrinting()
				.create()
				.toJson(execution, System.out);
	}

}
