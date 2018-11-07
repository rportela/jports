package jports.actions;

import javax.servlet.http.HttpServletRequest;

/**
 * This interface exposes a method to be implemented and should parse parameters
 * for a specific parameter Class.
 * 
 * @author rportela
 *
 */
public interface HttpActionParamParser {

	/**
	 * Parses the parameters from an HTTP Servlet request;
	 * 
	 * @param paramsClass
	 * @param request
	 * @return
	 * @throws Exception
	 */
	public <T> T parseParams(Class<T> paramsClass, HttpServletRequest request) throws Exception;
}
