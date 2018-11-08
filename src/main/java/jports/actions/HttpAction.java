package jports.actions;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * This class wraps methods to build an actions and an action execution based on
 * HTTP requests and responses; It creates new instances of actions, parses
 * parameters and copies the headers of the requests;
 * 
 * @author rportela
 *
 */
public class HttpAction {

	private String packageName;
	private HttpServletRequest request;
	private HttpServletResponse response;

	/**
	 * Sets the root package name for the action to be invoked;
	 * 
	 * @param packageName
	 * @return
	 */
	public HttpAction setPackage(String packageName) {
		this.packageName = packageName;
		return this;
	}

	/**
	 * Gets the root package name of the action to be invoked;
	 * 
	 * @return
	 */
	public String getPackageName() {
		return this.packageName;
	}

	/**
	 * Sets the HTTP request o be used for parsing the name of the Action and the
	 * Action Parameters depending on the content type;
	 * 
	 * @param request
	 * @return
	 */
	public HttpAction setRequest(HttpServletRequest request) {
		this.request = request;
		return this;
	}

	/**
	 * Gets the HTTP request used for parsing the name of the action and it's
	 * parameters;
	 * 
	 * @return
	 */
	public HttpServletRequest getRequest() {
		return this.request;
	}

	/**
	 * Sets the HTTP response to be used for sending the use case execution
	 * serialization;
	 * 
	 * @param response
	 * @return
	 */
	public HttpAction setResponse(HttpServletResponse response) {
		this.response = response;
		return this;
	}

	/**
	 * Gets the HTTP response used for sending the use case execution.
	 * 
	 * @return
	 */
	public HttpServletResponse getResponse() {
		return this.response;
	}

	/**
	 * Copies the headers of the HTTP request to a new Map of strings and objects;
	 * 
	 * @return
	 */
	public Map<String, Object> copyHeaders() {
		HashMap<String, Object> headers = new HashMap<String, Object>();
		Enumeration<String> names = request.getHeaderNames();
		while (names.hasMoreElements()) {
			String name = names.nextElement();
			String value = request.getHeader(name);
			headers.put(name, value);
		}
		return headers;
	}

	/**
	 * Locates the class name and instantiates a new action to be executed;
	 * 
	 * @return
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws InvocationTargetException
	 * @throws NoSuchMethodException
	 * @throws SecurityException
	 * @throws ClassNotFoundException
	 */
	@SuppressWarnings("unchecked")
	public <TParams, TResult> Action<TParams, TResult> buildAction() throws InstantiationException,
			IllegalAccessException,
			IllegalArgumentException,
			InvocationTargetException,
			NoSuchMethodException,
			SecurityException,
			ClassNotFoundException {
		String className = request.getPathInfo();
		className = className.replace('/', '.');
		className = packageName + className;
		return (Action<TParams, TResult>) Class
				.forName(className)
				.getConstructor()
				.newInstance();

	}

	/**
	 * This method locates a suitable parser based on the HTTP content type of the
	 * request and parses an expected TParams class;
	 * 
	 * @param paramsClass
	 * @return
	 * @throws Exception
	 */
	public <TParams> TParams parseParams(Class<TParams> paramsClass) throws Exception {
		String content_type = request.getContentType();
		HttpActionParamParser parser = null;
		if (isJsonContentType(content_type)) {
			parser = new HttpActionParamParserForJson();
		} else if (isXmlContentType(content_type)) {
			parser = new HttpActionParamParserForXml();
		} else if (isMultipart(content_type)) {
			parser = new HttpActionParamParserForMultipart();
		} else {
			parser = new HttpActionParamParserForParameters();
		}
		return parser.parseParams(paramsClass, request);
	}

	/**
	 * This method builds the Action using the path info on the request, parses the
	 * parameters using the request content type, copies the headers from the
	 * request, processes the execution, serializes the response to the HTTP output
	 * and returns the actual use case execution.
	 * 
	 * @return
	 */
	public <TParams, TResult> ActionExecution<TParams, TResult> execute() {

		try {
			Action<TParams, TResult> action = this.buildAction();
			ActionExecution<TParams, TResult> execution = action.execute(
					parseParams(action.getParamsClass()),
					copyHeaders());
			HttpActionWriter<TParams, TResult> writer = execution.result_type == ActionResultType.SUCCESS ?
					action.getHttpWriter() :
					new HttpActionWriterForJson<>();
			writer.write(execution, response);
			return execution;
		} catch (Exception e) {
			ActionExecution<TParams, TResult> errorExec = new ActionExecution<>();
			errorExec.name = request.getPathInfo();
			errorExec.result_type = ActionResultType.EXCEPTION_RAISED;
			errorExec.exception = e;
			try {
				new HttpActionWriterForJson<TParams, TResult>().write(errorExec, response);
			} catch (IOException ignore) {
				ignore.printStackTrace();
			}
			return errorExec;

		}

	}

	/**
	 * Checks if a given content type is JSON;
	 * 
	 * @param content_type
	 * @return
	 */
	public static final boolean isJsonContentType(String content_type) {
		if (content_type == null || content_type.isEmpty())
			return false;
		for (int i = 0; i < JSON_CONTENT_TYPES.length; i++)
			if (JSON_CONTENT_TYPES[i].equalsIgnoreCase(content_type))
				return true;
		return false;
	}

	/**
	 * Checks if a given content type is XML;
	 * 
	 * @param content_type
	 * @return
	 */
	public static final boolean isXmlContentType(String content_type) {
		if (content_type == null || content_type.isEmpty())
			return false;
		for (int i = 0; i < XML_CONTENT_TYPES.length; i++)
			if (XML_CONTENT_TYPES[i].equalsIgnoreCase(content_type))
				return true;
		return false;
	}

	/**
	 * Checks if a given content type is a multi-part/form-data submission;
	 * 
	 * @param content_type
	 * @return
	 */
	public static final boolean isMultipart(String content_type) {
		return content_type != null && content_type.startsWith("multipart/form-data");
	}

	/**
	 * The default known names of JSON content types;
	 */
	public static final String[] JSON_CONTENT_TYPES = new String[] {
			"application/json",
			"application/x-javascript",
			"text/javascript",
			"text/x-javascript",
			"text/x-json"
	};

	/**
	 * The default known names of XML content types;
	 */
	public static final String[] XML_CONTENT_TYPES = new String[] {
			"text/xml",
			"application/xml"
	};

}
