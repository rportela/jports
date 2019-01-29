package jports.adapters;

import java.lang.reflect.Array;
import java.util.List;

import jports.ShowStopper;

/**
 * A generic array adapter.
 * 
 * @author Rodrigo Portela
 *
 * @param <T>
 */
public class ArrayAdapter<T> implements Adapter<T[]> {

	/**
	 * The array component adapter.
	 */
	private final Adapter<T> componentAdapter;
	/**
	 * The component type of the array;
	 */
	private final Class<T> componentType;

	private final Class<T[]> arrayType;

	/**
	 * Initilizes the array adapter with the given class.
	 * 
	 * @param dataType
	 * @param componentAdapter
	 */
	@SuppressWarnings("unchecked")
	public ArrayAdapter(Class<T[]> dataType, Adapter<T> componentAdapter) {
		this.arrayType = dataType;
		this.componentAdapter = componentAdapter;
		this.componentType = (Class<T>) dataType.getComponentType();
	}

	/**
	 * Initializes the array adapter with the given class;
	 * 
	 * @param dataType
	 */
	@SuppressWarnings("unchecked")
	public ArrayAdapter(Class<T[]> dataType) {
		this(dataType, (Adapter<T>) AdapterFactory.getInstance(dataType.getComponentType()));
	}

	public Class<T> getComponentType() {
		return this.componentType;
	}

	public Adapter<T> getComponentAdapter() {
		return this.componentAdapter;
	}

	/**
	 * Creates a new Array instance with the component type associated with this
	 * adapter;
	 * 
	 * @param size
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public T[] newArray(int size) {
		return (T[]) Array.newInstance(componentAdapter.getDataType(), size);
	}

	/**
	 * Parses the source string as comma separated values.
	 */
	@Override
	public T[] parse(String source) {
		return parse(source, ",");
	}

	/**
	 * Parses the input string with the given regular expression into a specific
	 * array;
	 * 
	 * @param source
	 * @param regex
	 * @return
	 */
	public T[] parse(String source, String regex) {

		// null checks
		if (source == null || source.isEmpty())
			return newArray(0);

		// splits and parses
		String[] items = source.split(regex);
		T[] arr = newArray(items.length);
		for (int i = 0; i < arr.length; i++)
			arr[i] = componentAdapter.parse(items[i]);
		return arr;
	}

	/**
	 * Formats the source array into a string separated by commas.
	 */
	@Override
	public String format(T[] source) {
		return format(source, ",");
	}

	/**
	 * Formats the input array into a string with the provided value delimiter;
	 * 
	 * @param source
	 * @param delimiter
	 * @return
	 */
	public String format(T[] source, CharSequence delimiter) {
		if (source == null || source.length == 0)
			return "";

		String[] values = new String[source.length];
		for (int i = 0; i < values.length; i++)
			values[i] = componentAdapter.format(source[i]);
		return String.join(delimiter, values);
	}

	@SuppressWarnings("unchecked")
	public T[] convertArray(Object source) {
		Class<?> sourceClass = source.getClass();
		Class<?> sourceComponentType = sourceClass.getComponentType();

		// is it an array of the same type?
		if (componentAdapter.getDataType().isAssignableFrom(sourceComponentType))
			return (T[]) source;

		// Adapts and copies the values to the new array
		int length = Array.getLength(source);
		T[] arr = (T[]) Array.newInstance(sourceComponentType, length);
		for (int i = 0; i < length; i++)
			arr[i] = componentAdapter.convert(Array.get(source, i));
		return arr;
	}

	/**
	 * Converts the source list to an array;
	 * 
	 * @param source
	 * @return
	 */
	public T[] convertList(List<?> source) {
		T[] arr = newArray(source.size());
		for (int i = 0; i < arr.length; i++) {
			Object o = source.get(i);
			arr[i] = componentAdapter.convert(o);
		}

		return arr;
	}

	/**
	 * Converts an integer list to an int array;
	 * 
	 * @param list
	 * @return
	 */
	public static final synchronized int[] toIntArray(List<Integer> list) {
		int[] vals = new int[list.size()];
		for (int i = 0; i < vals.length; i++)
			vals[i] = list.get(i);
		return vals;
	}

	/**
	 * Converts a longs list to a longs array.
	 * 
	 * @param list
	 * @return
	 */
	public static final synchronized long[] toLongArray(List<Long> list) {
		long[] vals = new long[list.size()];
		for (int i = 0; i < vals.length; i++)
			vals[i] = list.get(i);
		return vals;
	}

	/**
	 * Converts a doubles list to a doubles array.
	 * 
	 * @param list
	 * @return
	 */
	public static final synchronized double[] toDoubleArray(List<Double> list) {
		double[] vals = new double[list.size()];
		for (int i = 0; i < vals.length; i++)
			vals[i] = list.get(i);
		return vals;
	}

	/**
	 * Converts a bytes list to a bytes array.
	 * 
	 * @param list
	 * @return
	 */
	public static final synchronized byte[] toByteArray(List<Byte> list) {
		byte[] vals = new byte[list.size()];
		for (int i = 0; i < vals.length; i++)
			vals[i] = list.get(i);
		return vals;
	}

	/**
	 * Converts a characters list to a char array.
	 * 
	 * @param list
	 * @return
	 */
	public static final synchronized char[] toCharArray(List<Character> list) {
		char[] vals = new char[list.size()];
		for (int i = 0; i < vals.length; i++)
			vals[i] = list.get(i);
		return vals;
	}

	/**
	 * Converts a boolean list to a boolean array.
	 * 
	 * @param list
	 * @return
	 */
	public static final synchronized boolean[] toBooleanArray(List<Boolean> list) {
		boolean[] vals = new boolean[list.size()];
		for (int i = 0; i < vals.length; i++)
			vals[i] = list.get(i);
		return vals;
	}

	/**
	 * Converts a short list to a short array.
	 * 
	 * @param list
	 * @return
	 */
	public static final synchronized short[] toShortArray(List<Short> list) {
		short[] vals = new short[list.size()];
		for (int i = 0; i < vals.length; i++)
			vals[i] = list.get(i);
		return vals;
	}

	@SuppressWarnings("unchecked")
	@Override
	public T[] convert(Object source) {
		if (source == null) {
			return (T[]) Array.newInstance(componentType, 0);
		}
		Class<?> sourceClass = source.getClass();
		if (sourceClass.isArray())
			return convertArray(source);

		else if (List.class.isAssignableFrom(sourceClass))
			return convertList((List<?>) source);

		else
			throw new ShowStopper("Can't convert " + sourceClass + " to " + getDataType());
	}

	@Override
	public Class<T[]> getDataType() {
		return this.arrayType;
	}
}