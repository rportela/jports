package jports.actions;

import java.util.Date;
import java.util.Map;

import jports.validations.ValidationResult;

/**
 * This class holds important information about the execution of an action; It's
 * main purpose is to be used as a transfer object for actions and to be stored
 * and possibly played back in the future;
 * 
 * @author rportela
 *
 * @param <TParams>
 * @param <TResult>
 */
public class ActionExecution<TParams, TResult> implements Runnable {

	/**
	 * The name of the action that was executed;
	 */
	public String name;

	/**
	 * The time the execution started; It's already initialized with current time
	 * but can be overwritten;
	 */
	public Date execution_start = new Date();

	/**
	 * The time the execution ended; It's usually set in the action but can also be
	 * overwritten prior to serialization or storage;
	 */
	public Date execution_end;

	/**
	 * The parameters that the action should use;
	 */
	public TParams params;

	/**
	 * The parameterized result of the action;
	 */
	public TResult result;

	/**
	 * The current identified user to be used for authentication purposes;
	 */
	public Object current_user;

	/**
	 * A map of headers than can be used for validation and for authentication
	 * purposes;
	 */
	public Map<String, Object> headers;

	/**
	 * The result type of the action;
	 */
	public ActionResultType result_type = ActionResultType.NOT_EXECUTED;

	/**
	 * The exception that was raised (if any) for log purposes;
	 */
	public Exception exception;

	/**
	 * The result of the validation of the parameters; If the parameters validated,
	 * this attribute is null;
	 */
	public ValidationResult validation;

	/**
	 * The message that explains why the action execution failed; This can also
	 * contain the message of an exception or a global validation error but should
	 * be null otherwise;
	 */
	public String fail_message;

	/**
	 * This method will run the Use case if it hasn't already been run using the
	 * configures values in this instance;
	 */
	@SuppressWarnings("unchecked")
	public void run() {
		if (result_type == ActionResultType.NOT_EXECUTED) {
			try {
				((Class<? extends Action<TParams, TResult>>) Class
						.forName(name))
								.getConstructor()
								.newInstance()
								.execute(this);

			} catch (Exception e) {
				this.exception = e;
				this.result_type = ActionResultType.EXCEPTION_RAISED;
				this.execution_end = new Date();
			}
		}
	}
}
