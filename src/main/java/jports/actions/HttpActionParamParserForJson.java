package jports.actions;

import java.io.InputStream;
import java.time.LocalDateTime;

import javax.servlet.http.HttpServletRequest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

/**
 * This class can parse JSON from an HTTP request into an expected action
 * parameters;
 * 
 * @author rportela
 *
 */
public class HttpActionParamParserForJson implements HttpActionParamParser {

	protected static final ObjectMapper MAPPER;

	static {

		MAPPER = new ObjectMapper();
		MAPPER.registerModule(
				new JavaTimeModule()
						.addDeserializer(LocalDateTime.class, new LocalDateTimeEpochDeserializer()));
		MAPPER.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
	}

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
