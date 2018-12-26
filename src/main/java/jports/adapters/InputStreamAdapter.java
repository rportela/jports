package jports.adapters;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

public class InputStreamAdapter implements Adapter<InputStream> {

	private final Charset charset;

	public InputStreamAdapter() {
		this("UTF-8");
	}

	public InputStreamAdapter(String charsetName) {
		this(Charset.forName(charsetName));
	}

	public InputStreamAdapter(Charset charset) {
		this.charset = charset;
	}

	@Override
	public InputStream parse(String source) {
		return source == null || source.isEmpty()
				? null
				: new ByteArrayInputStream(source.getBytes(charset));
	}

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

	public byte[] toBytes(InputStream source) throws IOException {
		ByteArrayOutputStream bos = new ByteArrayOutputStream(4096);
		byte[] buffer = new byte[4096];
		try {
			for (int r = source.read(buffer); r >= 0; r = source.read(buffer)) {
				bos.write(buffer, 0, r);
			}
			return bos.toByteArray();
		} finally {
			try {
				bos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public Class<InputStream> getDataType() {
		return InputStream.class;
	}

}
