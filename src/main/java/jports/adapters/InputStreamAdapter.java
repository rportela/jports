package jports.adapters;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;

/**
 * This class creates input streams out of bytes and Strings; It also contain
 * useful methods for reading and writing streams to strings and to bytes;
 * 
 * @author rportela
 *
 */
public class InputStreamAdapter implements Adapter<InputStream> {

	/**
	 * The character set used to convert streams;
	 */
	private final Charset charset;

	/**
	 * Creates a new instance of the input stream adapter using UTF-8 as the default
	 * character set.
	 */
	public InputStreamAdapter() {
		this("UTF-8");
	}

	/**
	 * Creates a new instance of the input stream adapter with a particular
	 * character set.
	 * 
	 * @param charsetName
	 */
	public InputStreamAdapter(String charsetName) {
		this(Charset.forName(charsetName));
	}

	/**
	 * Creates a new instance of the input stream adapter with a particular
	 * character set.
	 * 
	 * @param charset
	 */
	public InputStreamAdapter(Charset charset) {
		this.charset = charset;
	}

	/**
	 * Gets the bytes of an input string using the instance's character set and
	 * converts them to a byte array input stream;
	 */
	@Override
	public InputStream parse(String source) {
		return source == null || source.isEmpty()
				? null
				: new ByteArrayInputStream(source.getBytes(charset));
	}

	/**
	 * Reads the bytes from the input stream and create a new string using the
	 * instance's character set;
	 */
	@Override
	public String format(InputStream source) {
		try {
			return source == null
					? null
					: new String(toBytes(source), charset);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Converts from different source objects to an input stream;
	 */
	@Override
	public InputStream convert(Object source) {
		if (source == null)
			return null;
		else if (source instanceof InputStream)
			return (InputStream) source;
		else if (source instanceof byte[]) {
			return new ByteArrayInputStream((byte[]) source);
		} else if (source instanceof String)
			return new ByteArrayInputStream(((String) source).getBytes(charset));
		else
			throw new RuntimeException("Can't convert " + source + " to InputStream.");
	}

	/**
	 * Gets the bytes of an input stream;
	 * 
	 * @param source
	 * @return
	 * @throws IOException
	 */
	public byte[] toBytes(InputStream source) throws IOException {
		ByteArrayOutputStream bos = new ByteArrayOutputStream(4096);
		try {
			copy(source, bos, 4096);
			return bos.toByteArray();
		} finally {
			try {
				bos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Creates a new string by reading the bytes from the input stream and using the
	 * instance's character set. This method is the same as calling "format".
	 * 
	 * @param source
	 * @return
	 */
	public String toString(InputStream source) {
		return format(source);
	}

	/**
	 * Copies bytes from the source input stream to the output destination stream
	 * using the default 4096 byte buffer; It returns the total number of bytes
	 * copied from one stream to the other;
	 * 
	 * @param source
	 * @param dest
	 * @return
	 * @throws IOException
	 */
	public int copy(InputStream source, OutputStream dest) throws IOException {
		return copy(source, dest, 4096);
	}

	/**
	 * Copies bytes from the source input stream to the output destination stream
	 * using a custom size byte buffer; It returns the total number of bytes copied
	 * from one stream to the other;
	 * 
	 * @param source
	 * @param dest
	 * @param bufferSize
	 * @return
	 * @throws IOException
	 */
	public int copy(InputStream source, OutputStream dest, int bufferSize) throws IOException {
		int t = 0;
		byte[] buffer = new byte[bufferSize];
		for (int r = source.read(buffer); r >= 0; r = source.read(buffer)) {
			dest.write(buffer, 0, r);
			t += r;
		}
		return t;
	}

	/**
	 * Gets the data type (InputStream) that this Adapter can handle.
	 */
	@Override
	public Class<InputStream> getDataType() {
		return InputStream.class;
	}

}
