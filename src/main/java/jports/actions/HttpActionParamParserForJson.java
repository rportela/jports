package jports.actions;

import java.io.InputStream;

import javax.servlet.http.HttpServletRequest;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * This class can parse JSON from an HTTP request into an expected action
 * parameters;
 * 
 * @author rportela
 *
 */
public class HttpActionParamParserForJson implements HttpActionParamParser {

	private static final ObjectMapper MAPPER = new ObjectMapper();

	/**
	 * Parses the HTTP servlet request as JSON mapped to a specific parameter class;
	 */
	@Override
	public <T> T parseParams(Class<T> paramsClass, HttpServletRequest request) throws Exception {
		try (InputStream is = request.getInputStream()) {
			return MAPPER.readValue(is, paramsClass);
		}
	}

}
