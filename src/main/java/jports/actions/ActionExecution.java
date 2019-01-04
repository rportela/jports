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
 * @param <T>
 * @param <R>
 */
public class ActionExecution<T, R> {

	private String name;
	private Date executionStart = new Date();
	private Date executionEnd;
	private T params;
	private R result;
	private Object currentUser;
	private Map<String, Object> headers;
	private ActionResultType resultType = ActionResultType.NOT_EXECUTED;
	private Exception exception;
	private ValidationResult validation;
	private String failMessage;

	/**
	 * The name of the action that was executed;
	 * 
	 * @return the name
	 */
	public final String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public ActionExecution<T, R> setName(String name) {
		this.name = name;
		return this;
	}

	/**
	 * The time the execution started; It's already initialized with current time
	 * but can be overwritten;
	 * 
	 * @return the executionStart
	 */
	public final Date getExecutionStart() {
		return executionStart;
	}

	/**
	 * @param executionStart
	 *            the executionStart to set
	 */
	public ActionExecution<T, R> setExecutionStart(Date executionStart) {
		this.executionStart = executionStart;
		return this;
	}

	/**
	 * The time the execution ended; It's usually set in the action but can also be
	 * overwritten prior to serialization or storage;
	 * 
	 * @return the executionEnd
	 */
	public final Date getExecutionEnd() {
		return executionEnd;
	}

	/**
	 * @param executionEnd
	 *            the executionEnd to set
	 */
	public ActionExecution<T, R> setExecutionEnd(Date executionEnd) {
		this.executionEnd = executionEnd;
		return this;
	}

	/**
	 * The parameters that the action should use;
	 * 
	 * @return the params
	 */
	public final T getParams() {
		return params;
	}

	/**
	 * @param params
	 *            the params to set
	 */
	public ActionExecution<T, R> setParams(T params) {
		this.params = params;
		return this;
	}

	/**
	 * The parameterized result of the action;
	 * 
	 * @return the result
	 */
	public final R getResult() {
		return result;
	}

	/**
	 * 
	 * @param result
	 *            the result to set
	 */
	public ActionExecution<T, R> setResult(R result) {
		this.result = result;
		return this;
	}

	/**
	 * The current identified user to be used for authentication purposes;
	 * 
	 * @return the currentUser
	 */
	public final Object getCurrentUser() {
		return currentUser;
	}

	/**
	 * @param currentUser
	 *            the currentUser to set
	 */
	public ActionExecution<T, R> setCurrentUser(Object currentUser) {
		this.currentUser = currentUser;
		return this;
	}

	/**
	 * A map of headers than can be used for validation and for authentication
	 * purposes;
	 * 
	 * @return the headers
	 */
	public final Map<String, Object> getHeaders() {
		return headers;
	}

	/**
	 * @param headers
	 *            the headers to set
	 */
	public ActionExecution<T, R> setHeaders(Map<String, Object> headers) {
		this.headers = headers;
		return this;
	}

	/**
	 * The result type of the action;
	 * 
	 * @return the resultType
	 */
	public final ActionResultType getResultType() {
		return resultType;
	}

	/**
	 * @param resultType
	 *            the resultType to set
	 */
	public ActionExecution<T, R> setResultType(ActionResultType resultType) {
		this.resultType = resultType;
		return this;
	}

	/**
	 * The exception that was raised (if any) for log purposes;
	 * 
	 * @return the exception
	 */
	public final Exception getException() {
		return exception;
	}

	/**
	 * @param exception
	 *            the exception to set
	 */
	public ActionExecution<T, R> setException(Exception exception) {
		this.exception = exception;
		return this;
	}

	/**
	 * The result of the validation of the parameters; If the parameters validated,
	 * this attribute is null;
	 * 
	 * @return the validation
	 */
	public final ValidationResult getValidation() {
		return validation;
	}

	/**
	 * @param validation
	 *            the validation to set
	 */
	public ActionExecution<T, R> setValidation(ValidationResult validation) {
		this.validation = validation;
		return this;
	}

	/**
	 * The message that explains why the action execution failed; This can also
	 * contain the message of an exception or a global validation error but should
	 * be null otherwise;
	 * 
	 * @return the failMessage
	 */
	public final String getFailMessage() {
		return failMessage;
	}

	/**
	 * @param failMessage
	 *            the failMessage to set
	 */
	public ActionExecution<T, R> setFailMessage(String failMessage) {
		this.failMessage = failMessage;
		return this;
	}

}
