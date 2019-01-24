package jports.actions;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;

import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * This class is responsible for serializing the execution as JSON to the
 * standard HTTP servlet response.
 * 
 * @author rportela
 *
 * @param <T>
 * @param <R>
 */
public class HttpActionWriterForJson<T, R> implements HttpActionWriter<T, R> {

	protected static final ObjectMapper MAPPER;
	static {
		MAPPER = new ObjectMapper();
		MAPPER.setDateFormat(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ"));
	}

	/**
	 * Actually writes the result of the action execution object serialized as JSON
	 * to the HTTP servlet output stream.
	 */
	@Override
	public void write(ActionExecution<T, R> execution, HttpServletResponse response) throws IOException {

		execution.setParams(null);
		String json = MAPPER.writeValueAsString(execution.getResult());
		byte[] bytes = json.getBytes(StandardCharsets.UTF_8);

		response.setContentType("application/json");
		response.setContentLength(bytes.length);

		try (OutputStream os = response.getOutputStream()) {
			os.write(bytes);
		}

		response.flushBuffer();

	}

}
