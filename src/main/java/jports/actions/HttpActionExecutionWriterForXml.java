package jports.actions;

import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;

/**
 * This class is responsible for serializing the execution as XML to the
 * standard HTTP servlet response. Different from other action writers, this
 * class serializes the ENTIRE execution object to standardize communications
 * and exception handling on the client.
 * 
 * @author rportela
 *
 * @param <T>
 * @param <R>
 */
public class HttpActionExecutionWriterForXml<T, R> implements HttpActionWriter<T, R> {

	protected static final XmlMapper XML_MAPPER = new XmlMapper();

	/**
	 * Actually writes the entire action execution object serialized as XML to the
	 * HTTP servlet output stream.
	 */
	@Override
	public void write(ActionExecution<T, R> execution, HttpServletResponse response) throws IOException {

		execution.setParams(null);

		response.setContentType("application/xml");

		try (OutputStream os = response.getOutputStream()) {
			XML_MAPPER.writeValue(os, execution);
		}

		response.flushBuffer();

	}

}
