package jports.actions;

import java.io.IOException;
import java.io.OutputStream;

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
	public void write(ActionExecution<T, R> execution, HttpAction action) throws IOException {

		byte[] bytes = getBytes(execution);
		String fileName = getFileName(execution);
		action.setContentType(getContentType(execution));

		if (fileName != null && !fileName.isEmpty()) {
			action.setResponseHeader("Content-Disposition", "attachment;filename=" + fileName);
		}

		if (bytes != null)
			try (OutputStream os = action.getResponseStream()) {
				os.write(bytes);
			}

	}

}
