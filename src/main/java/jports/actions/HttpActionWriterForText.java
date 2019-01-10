package jports.actions;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

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
	public void write(ActionExecution<T, String> execution, HttpServletResponse response) throws IOException {

		String result = execution.getResult();
		byte[] bytes = result == null
				? new byte[0]
				: result.getBytes(charset);

		response.setContentType("text/plain; charset=" + charset.displayName());
		response.setContentLength(bytes.length);
		try (ServletOutputStream os = response.getOutputStream()) {
			os.write(bytes);
		}

	}

}
