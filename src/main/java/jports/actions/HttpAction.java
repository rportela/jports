package jports.actions;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class HttpAction {

	public String name;
	public HttpServletRequest request;
	public HttpServletResponse response;
	public HttpActionParser parser;
	public HttpActionWriter writer;

	public boolean parse() {

		String content_type = request.getContentType();
		String method = request.getMethod();

		if (isJsonContentType(content_type))
			parser = new HttpActionParserForJson();
		else if (isXmlContentType(content_type))
			parser = new HttpActionParserForXml();
		else if (isMultipart(content_type))
			parser = new HttpActionParserForMultipart();
		else
			parser = new HttpActionParserForParameters();
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
