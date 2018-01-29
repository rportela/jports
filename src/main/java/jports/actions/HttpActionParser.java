package jports.actions;

import javax.servlet.http.HttpServletRequest;

public interface HttpActionParser {

	public <T> T parseParams(Class<T> paramsClass, HttpServletRequest request) throws Exception;
}
