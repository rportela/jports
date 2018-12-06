package jports.actions;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;

import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class HttpActionWriterForJson<TParams, TResult> implements HttpActionWriter<TParams, TResult> {

	public static final Gson GSON = new GsonBuilder()
			.setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ")
			.create();

	private static final Charset UTF8 = Charset.forName("UTF-8");

	@Override
	public void write(ActionExecution<TParams, TResult> execution, HttpServletResponse response) throws IOException {

		execution.params = null;
		String json = GSON.toJson(execution);
		byte[] bytes = json.getBytes(UTF8);

		response.setContentType("application/json");
		response.setContentLength(bytes.length);
		try (OutputStream os = response.getOutputStream()) {
			os.write(bytes);
		}

		response.flushBuffer();

	}

}
