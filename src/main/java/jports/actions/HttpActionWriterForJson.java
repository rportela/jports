package jports.actions;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

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
		MAPPER.registerModule(
				new JavaTimeModule()
						.addSerializer(LocalDateTime.class, new LocalDateTimeISOSerializer()));
		MAPPER.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
	}

	/**
	 * Actually writes the result of the action execution object serialized as JSON
	 * to the HTTP servlet output stream.
	 */
	@Override
	public void write(ActionExecution<T, R> execution, HttpAction action) throws IOException {

		execution.setParams(null);
		String json = MAPPER.writeValueAsString(execution);
		byte[] bytes = json.getBytes(StandardCharsets.UTF_8);

		action.setContentType("application/json");

		try (OutputStream os = action.getResponseStream()) {
			os.write(bytes);
		}

		action.flushResponse();

	}

}
