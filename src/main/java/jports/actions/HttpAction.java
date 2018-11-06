package jports.actions;

import java.io.IOException;
import java.util.Enumeration;
import java.util.LinkedHashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class HttpAction {

	private String name;
	private HttpServletRequest request;
	private HttpServletResponse response;

	public HttpAction setName(String name) {
		this.name = name;
		return this;
	}

	public HttpAction setRequest(HttpServletRequest request) {
		this.request = request;
		return this;
	}

	public HttpAction setResponse(HttpServletResponse response) {
		this.response = response;
		return this;
	}

	private HttpActionParamParser getParser() {
		String content_type = request.getContentType();
		if (isJsonContentType(content_type))
			return new HttpActionParamParserForJson();
		else if (isXmlContentType(content_type))
			return new HttpActionParamParserForXml();
		else if (isMultipart(content_type))
			return new HttpActionParamParserForMultipart();
		else
			return new HttpActionParamParserForParameters();
	}

	@SuppressWarnings("unchecked")
	public <TParams, TResult> ActionExecution<TParams, TResult> execute() {

		
		Action<TParams, TResult> action = null;
		try {
			action = (Action<TParams, TResult>) parser.instantiate(name);
		} catch (Exception e) {
			try {
				response.sendError(404, e.getMessage());
			} catch (IOException ignore) {
				ignore.printStackTrace();
			}
			return null;
		}
		
		HttpActionParamParser parser = getParser();

		ActionExecution<TParams, TResult> execution = new ActionExecution<>();
		execution.headers = new LinkedHashMap<String, Object>();
		Enumeration<String> names = request.getHeaderNames();
		while (names.hasMoreElements()) {
			String name = names.nextElement();
			String value = request.getHeader(name);
			execution.headers.put(name, value);
		}

		try {
			execution.params = parser.parseParams(action.getParamsClass(), request);
			action.execute(execution);
		} catch (Exception e) {
			execution.result_type = ActionResultType.EXCEPTION_RAISED;
			execution.exception = e;
		}

		HttpActionWriter<TParams, TResult> writer = execution.result_type == ActionResultType.SUCCESS ?
				action.getHttpWriter() :
				new HttpActionWriterForJson<>();

		try {
			writer.write(execution, response);
		} catch (Exception ignore) {
			ignore.printStackTrace();
		}

		return execution;

	}

	public static final boolean isJsonContentType(String content_type) {
		if (content_type == null || content_type.isEmpty())
			return false;
		for (int i = 0; i < JSON_CONTENT_TYPES.length; i++)
			if (JSON_CONTENT_TYPES[i].equalsIgnoreCase(content_type))
				return true;
		return false;
	}

	public static final boolean isXmlContentType(String content_type) {
		if (content_type == null || content_type.isEmpty())
			return false;
		for (int i = 0; i < XML_CONTENT_TYPES.length; i++)
			if (XML_CONTENT_TYPES[i].equalsIgnoreCase(content_type))
				return true;
		return false;
	}

	public static final boolean isMultipart(String content_type) {
		return content_type != null && content_type.startsWith("multipart/form-data");
	}

	public static final String[] JSON_CONTENT_TYPES = new String[] {
			"application/json",
			"application/x-javascript",
			"text/javascript",
			"text/x-javascript",
			"text/x-json"
	};

	public static final String[] XML_CONTENT_TYPES = new String[] {
			"text/xml",
			"application/xml"
	};

}
