package jports.adapters;

/**
 * This class represents an utility adapter. It enables implements to write
 * values to and read from strings. Also, it is responsible for general data
 * type conversion techniques via the "convert" method;
 * 
 * @author rportela
 *
 * @param <T>
 */
public interface Adapter<T> {

	/**
	 * Parses an input source string to a specific data type;
	 * 
	 * @param source
	 * @return
	 */
	public T parse(String source);

	/**
	 * Formats a specific data type to a String;
	 * 
	 * @param source
	 * @return
	 */
	public String format(T source);

	/**
	 * Converts the object to a specific data type and formats it as a string;
	 * 
	 * @param source
	 * @return
	 */
	@SuppressWarnings("unchecked")
	default String formatObject(Object source) {
		return format((T) source);
	}

	/**
	 * Converts a source object to a specific data type;
	 * 
	 * @param source
	 * @return
	 */
	public T convert(Object source);

	/**
	 * Gets the data type this adapter converts values to;
	 * 
	 * @return
	 */
	public Class<T> getDataType();
}
