package jports.actions;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * This class is responsible for serializing the execution as JSON to the
 * standard HTTP servlet response. Different from other action writers, this
 * class serializes the ENTIRE execution object to standardize communications
 * and exception handling on the client.
 * 
 * @author rportela
 *
 * @param <T>
 * @param <R>
 */
public class HttpActionExecutionWriterForJson<T, R> implements HttpActionWriter<T, R> {

	protected static final ObjectMapper MAPPER = new ObjectMapper();

	/**
	 * Actually writes the entire action execution object serialized as JSON to the
	 * HTTP servlet output stream.
	 */
	@Override
	public void write(ActionExecution<T, R> execution, HttpServletResponse response) throws IOException {

		execution.setParams(null);
		String json = MAPPER.writeValueAsString(execution);
		byte[] bytes = json.getBytes(StandardCharsets.UTF_8);

		response.setContentType("application/json");
		response.setContentLength(bytes.length);

		try (OutputStream os = response.getOutputStream()) {
			os.write(bytes);
		}

		response.flushBuffer();

	}

}