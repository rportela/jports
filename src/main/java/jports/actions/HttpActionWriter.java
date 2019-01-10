package jports.actions;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

/**
 * This class is responsible for writing and execution result to the response of
 * an HTTP Servlet. To enhance the possibilities and to provide a standard way
 * of serializing action executions, the entire ActionExecution model is
 * provided;
 * 
 * @author rportela
 *
 * @param <T>
 * @param <R>
 */
public interface HttpActionWriter<T, R> {

	/**
	 * Writes a specific action execution to an output HTTP servlet response;
	 * 
	 * @param execution
	 * @param response
	 * @throws IOException
	 */
	public void write(ActionExecution<T, R> execution, HttpServletResponse response) throws IOException;

}
