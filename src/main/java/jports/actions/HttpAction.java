package jports.actions;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class HttpAction {

	public String actionName;
	public HttpServletRequest request;
	public HttpServletResponse response;
	public HttpActionParser parser;
	public HttpActionWriter writer;
	
	
}
