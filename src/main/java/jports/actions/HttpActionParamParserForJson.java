package jports.actions;

import java.io.InputStreamReader;
import java.nio.charset.Charset;

import javax.servlet.http.HttpServletRequest;

import com.google.gson.Gson;

/**
 * This class can parse JSON from an HTTP request into an expected action
 * parameters;
 * 
 * @author rportela
 *
 */
public class HttpActionParamParserForJson implements HttpActionParamParser {

	private static Gson GSON = new Gson();
	private static Charset UTF8 = Charset.forName("UTF-8");

	/**
	 * Parses the HTTP servlet request as JSON mapped to a specific parameter class;
	 */
	@Override
	public <T> T parseParams(Class<T> paramsClass, HttpServletRequest request) throws Exception {
		return GSON.fromJson(new InputStreamReader(request.getInputStream(), UTF8), paramsClass);
	}

}
