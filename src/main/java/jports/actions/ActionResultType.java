package jports.actions;

/**
 * This enumeration divides waters on the user interface by telling beforehand
 * what happened;
 * 
 * @author rportela
 *
 */
public enum ActionResultType {

	/**
	 * The action has not yet been executed;
	 */
	NOT_EXECUTED,
	/**
	 * The authentication of the current user failed; More information should be
	 * provided.
	 */
	AUTHENTICATION_FAILED,
	/**
	 * The validation of the parameters failed; ore information should be provided.
	 */
	VALIDATION_FAILED,
	/**
	 * An exception was raised during the execution; More information should be
	 * provided.
	 */
	EXCEPTION_RAISED,
	/**
	 * All is great;
	 */
	SUCCESS,
	/**
	 * The action validated and authenticated but failed the execution. More
	 * information should be provided.
	 */
	FAILED

}
