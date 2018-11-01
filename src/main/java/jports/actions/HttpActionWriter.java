package jports.actions;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

public interface HttpActionWriter<TParams, TResult> {

	public void write(ActionExecution<TParams, TResult> execution, HttpServletResponse response) throws IOException;

}
