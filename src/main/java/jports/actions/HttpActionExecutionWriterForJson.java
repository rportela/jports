package jports.actions;

import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;

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

	protected static final ObjectMapper MAPPER;

	static {
		MAPPER = new ObjectMapper();
		MAPPER.setDateFormat(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ"));
	}

	/**
	 * Actually writes the entire action execution object serialized as JSON to the
	 * HTTP servlet output stream.
	 */
	@Override
	public void write(ActionExecution<T, R> execution, HttpAction action) throws IOException {
		execution.setParams(null);

		OutputStream os = action
				.setContentType("application/json")
				.getResponseStream();

		try {
			MAPPER.writeValue(os, execution);
		} finally {
			os.close();
		}

		action.flushResponse();
	}

}
