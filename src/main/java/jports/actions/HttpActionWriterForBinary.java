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
public abstract class HttpActionWriterForBinary<T, R> implements HttpActionWriter<T, R> {

	public abstract String getContentType(ActionExecution<T, R> execution);

	public abstract String getFileName(ActionExecution<T, R> execution);

	public abstract byte[] getBytes(ActionExecution<T, R> execution);

	/**
	 * Actually writes the byte[] execution result to the response output stream;
	 */
	@Override
	public void write(ActionExecution<T, R> execution, HttpServletResponse response) throws IOException {

		byte[] bytes = getBytes(execution);
		String fileName = getFileName(execution);
		response.setContentType(getContentType(execution));
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
