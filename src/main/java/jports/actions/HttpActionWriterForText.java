package jports.actions;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * A simple text writer for HTTP servlet responses. This class receives a
 * specific charset to use when encoding the text and sets the appropriate
 * headers on the HTTP response.
 * 
 * @author rportela
 *
 * @param <T>
 */
public class HttpActionWriterForText<T> implements HttpActionWriter<T, String> {

	private final Charset charset;

	/**
	 * Creates a new action writer for text with a specific charset to be used.
	 * 
	 * @param charset
	 */
	public HttpActionWriterForText(Charset charset) {
		this.charset = charset;
	}

	/**
	 * Creates a new action writer for text using the default UTF-8 standard
	 * charset.
	 */
	public HttpActionWriterForText() {
		this(StandardCharsets.UTF_8);
	}

	/**
	 * Gets the charset of this action writer instance.
	 * 
	 * @return
	 */
	public final Charset getCharset() {
		return this.charset;
	}

	/**
	 * Actually sets the headers and writes the text to the HTTP servlet output
	 * stream.
	 */
	@Override
	public void write(ActionExecution<T, String> execution, HttpAction action) throws IOException {

		String result = execution.getResult();
		byte[] bytes = result == null
				? new byte[0]
				: result.getBytes(charset);

		action.setContentType("text/plain; charset=" + charset.displayName());

		try (OutputStream os = action.getResponseStream()) {
			os.write(bytes);
		}

	}

}
