package jports.actions;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

public interface HttpActionWriter<T, R> {

	public void write(ActionExecution<T, R> execution, HttpServletResponse response) throws IOException;

}
