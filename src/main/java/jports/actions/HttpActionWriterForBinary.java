package jports.actions;

import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.http.HttpServletResponse;

/**
 * This is an action execution that can write standard byte arrays of a
 * Configurable content type to the output HTTP servlet stream;
 * 
 * @author rportela
 *
 * @param <T>
 */
public class HttpActionWriterForBinary<T> implements HttpActionWriter<T, byte[]> {

	private final String fileName;
	private final String contentType;

	/**
	 * Creates a new action writer for Binary using a specific content type and a
	 * file name indicating that the response should be an attachment or prompt to
	 * save.
	 * 
	 * @param contentType
	 * @param fileName
	 */
	public HttpActionWriterForBinary(String contentType, String fileName) {
		this.contentType = contentType;
		this.fileName = fileName;
	}

	/**
	 * Creates a new action writer for Binary using a specific content type
	 * indicating that the content should be handled inline by the response client.
	 * 
	 * @param contentType
	 */
	public HttpActionWriterForBinary(String contentType) {
		this(contentType, null);
	}

	/**
	 * Actually writes the byte[] execution result to the response output stream;
	 */
	@Override
	public void write(ActionExecution<T, byte[]> execution, HttpServletResponse response) throws IOException {

		byte[] bytes = execution.getResult();

		response.setContentType(this.contentType);
		response.setContentLength(bytes == null
				? 0
				: bytes.length);

		if (fileName != null && !fileName.isEmpty()) {
			response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
		}

		if (bytes != null)
			try (OutputStream os = response.getOutputStream()) {
				os.write(bytes);
			}

	}

}
