package jports.actions;

import java.io.InputStreamReader;
import java.nio.charset.Charset;

import javax.servlet.http.HttpServletRequest;

import com.google.gson.Gson;

public class HttpActionParamParserForJson implements HttpActionParamParser {

	private static Gson GSON = new Gson();
	private static Charset UTF8 = Charset.forName("UTF-8");

	@Override
	public <T> T parseParams(Class<T> paramsClass, HttpServletRequest request) throws Exception {
		return GSON.fromJson(new InputStreamReader(request.getInputStream(), UTF8), paramsClass);
	}

}
