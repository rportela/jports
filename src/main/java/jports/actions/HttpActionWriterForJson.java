package jports.actions;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class HttpActionWriterForJson<T, R> implements HttpActionWriter<T, R> {

	public static final Gson GSON = new GsonBuilder()
			.setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ")
			.create();

	@Override
	public void write(ActionExecution<T, R> execution, HttpServletResponse response) throws IOException {

		execution.setParams(null);
		String json = GSON.toJson(execution);
		byte[] bytes = json.getBytes(StandardCharsets.UTF_8);

		response.setContentType("application/json");
		response.setContentLength(bytes.length);
		try (OutputStream os = response.getOutputStream()) {
			os.write(bytes);
		}

		response.flushBuffer();

	}

}
