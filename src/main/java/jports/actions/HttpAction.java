package jports.actions;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.security.Principal;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.GZIPOutputStream;

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
		HashMap<String, Object> headers = new HashMap<>();
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
	public <T, R> Action<T, R> buildAction() throws InstantiationException,
			IllegalAccessException,
			InvocationTargetException,
			NoSuchMethodException,
			ClassNotFoundException {
		String className = request.getPathInfo();
		className = className.replace('/', '.');
		className = packageName + className;
		return (Action<T, R>) Class
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
	public <P> P parseParams(Class<P> paramsClass) throws Exception {
		String contentType = request.getContentType();
		HttpActionParamParser parser = null;
		if (isJsonContentType(contentType)) {
			parser = new HttpActionParamParserForJson();
		} else if (isXmlContentType(contentType)) {
			parser = new HttpActionParamParserForXml();
		} else if (isMultipart(contentType)) {
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
	public <T, R> ActionExecution<T, R> execute() {

		try {
			Action<T, R> action = this.buildAction();

			ActionExecution<T, R> execution = action.execute(
					parseParams(action.getParamsClass()),
					copyHeaders(),
					getRequestUser());

			HttpActionWriter<T, R> writer = execution.getResultType() == ActionResultType.SUCCESS
					? action.getHttpWriter()
					: new HttpActionExecutionWriterForJson<>();

			writer.write(execution, this);

			return execution;
		} catch (Exception e) {
			ActionExecution<T, R> errorExec = new ActionExecution<>();
			errorExec.setName(request.getPathInfo())
					.setResultType(ActionResultType.EXCEPTION_RAISED)
					.setException(e)
					.setFailMessage(e.getMessage());
			try {
				new HttpActionExecutionWriterForJson<T, R>().write(errorExec, this);
			} catch (IOException ignore) {
				Logger.getLogger(getClass().getName()).log(Level.INFO, ignore, null);
			}
			return errorExec;

		}
	}

	public Object getRequestUser() {
		Principal p = request.getUserPrincipal();
		return p == null
				? request.getRemoteUser()
				: p;
	}

	public OutputStream getResponseStream() throws IOException {
		String encoding = request.getHeader("Accept-encoding");

		if (encoding == null || encoding.isEmpty()) {
			return response.getOutputStream();
		} else if (encoding.contains("gzip")) {
			response.setHeader("Content-Encoding", "gzip");
			return new GZIPOutputStream(response.getOutputStream());
		} else if (encoding.contains("deflate")) {
			response.setHeader("Content-Encoding", "deflate");
			return new DeflaterOutputStream(response.getOutputStream());
		} else {
			return response.getOutputStream();
		}
	}

	public HttpAction setResponseHeader(String name, String value) {
		response.setHeader(name, value);
		return this;
	}

	public String getContentType() {
		return response.getContentType();
	}

	public HttpAction setContentType(String contentType) {
		response.setContentType(contentType);
		return this;
	}

	public HttpAction flushResponse() throws IOException {
		response.flushBuffer();
		return this;
	}

	public HttpAction setContentLength(int length) {
		response.setContentLength(length);
		return this;
	}

	public HttpAction sendError(int code) throws IOException {
		response.sendError(code);
		return this;
	}

	public HttpAction sendRedirect(String url) throws IOException {
		response.sendRedirect(url);
		return this;
	}

	/**
	 * Checks if a given content type is JSON;
	 * 
	 * @param contentType
	 * @return
	 */
	public static final boolean isJsonContentType(String contentType) {
		if (contentType == null || contentType.isEmpty())
			return false;
		for (int i = 0; i < JSON_contentTypeS.length; i++)
			if (JSON_contentTypeS[i].equalsIgnoreCase(contentType))
				return true;
		return false;
	}

	/**
	 * Checks if a given content type is XML;
	 * 
	 * @param contentType
	 * @return
	 */
	public static final boolean isXmlContentType(String contentType) {
		if (contentType == null || contentType.isEmpty())
			return false;
		for (int i = 0; i < XML_contentTypeS.length; i++)
			if (XML_contentTypeS[i].equalsIgnoreCase(contentType))
				return true;
		return false;
	}

	/**
	 * Checks if a given content type is a multi-part/form-data submission;
	 * 
	 * @param contentType
	 * @return
	 */
	protected static final boolean isMultipart(String contentType) {
		return contentType != null && contentType.startsWith("multipart/form-data");
	}

	/**
	 * The default known names of JSON content types;
	 */
	protected static final String[] JSON_contentTypeS = new String[] {
			"application/json",
			"application/x-javascript",
			"text/javascript",
			"text/x-javascript",
			"text/x-json"
	};

	/**
	 * The default known names of XML content types;
	 */
	protected static final String[] XML_contentTypeS = new String[] {
			"text/xml",
			"application/xml"
	};

}
