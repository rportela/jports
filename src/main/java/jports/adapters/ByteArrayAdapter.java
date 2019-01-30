package jports.adapters;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.nio.ByteBuffer;

import jports.ShowStopper;

/**
 * The standard byte array adapter that reads and writes strings in the HEX
 * format;
 * 
 * @author Rodrigo Portela
 *
 */
public class ByteArrayAdapter implements Adapter<byte[]> {

	private static final char[] HEX_ARRAY = "0123456789ABCDEF".toCharArray();

	private static final byte valueOf(char c) {
		for (byte i = 0; i < HEX_ARRAY.length; i++)
			if (c == HEX_ARRAY[i])
				return i;
		throw new ShowStopper(c + " is not a hex character.");
	}

	/**
	 * HEX formats the source array into a string;
	 */
	@Override
	public String format(byte[] source) {
		if (source == null)
			return "";

		char[] hexChars = new char[source.length * 2];
		for (int j = 0; j < source.length; j++) {
			int v = source[j] & 0xFF;
			hexChars[j * 2] = HEX_ARRAY[v >>> 4];
			hexChars[j * 2 + 1] = HEX_ARRAY[v & 0x0F];
		}
		return new String(hexChars);
	}

	/**
	 * Parses the string source as a HEX formatted byte array;
	 */
	@Override
	public byte[] parse(String source) {
		if (source == null || source.isEmpty())
			return new byte[0];

		byte[] output = new byte[source.length() / 2];
		for (int i = 0; i < output.length; i++) {
			char hi = Character.toUpperCase(source.charAt((i * 2)));
			char lo = Character.toUpperCase(source.charAt((i * 2) + 1));
			output[i] = (byte) (valueOf(hi) * 16 + valueOf(lo));
		}
		return output;
	}

	/**
	 * Converts an input stream to a byte array;
	 * 
	 * @param is
	 * @return
	 */
	public byte[] convertInputStream(InputStream is) {
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			for (int i = is.read(); i >= 0; i = is.read())
				baos.write(i);
			return baos.toByteArray();
		} catch (Exception e) {
			throw new ShowStopper(e);
		}
	}

	@Override
	public byte[] convert(Object source) {
		if (source == null) {
			return (byte[]) Array.newInstance(byte[].class, 0);
		} else if (source instanceof byte[]) {
			return (byte[]) source;
		} else {
			Class<?> sourceClass = source.getClass();
			if (InputStream.class.isAssignableFrom(sourceClass))
				return convertInputStream((InputStream) source);
			else if (ByteBuffer.class.isAssignableFrom(sourceClass))
				return ((ByteBuffer) source).array();
			else
				throw new ShowStopper("Can't convert " + source + " to byte[]");
		}
	}

	@Override
	public Class<byte[]> getDataType() {
		return byte[].class;
	}

}
