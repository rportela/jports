package jports.adapters;

import java.math.BigDecimal;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import jports.ShowStopper;

/**
 * This class has a static map of classes to adapters so it can find their
 * constructor and instantiate adapters based on a given Class<T> data type;
 * 
 * @author rportela
 *
 */
public final class AdapterFactory {

	/**
	 * Private singleton pattern constructor;
	 */
	private AdapterFactory() {
	}

	/**
	 * a static map of adapters;
	 */
	private static final HashMap<Class<?>, Class<? extends Adapter<?>>> INSTANCES = new HashMap<>();

	/**
	 * Populates the static map with factory built adapters;
	 */
	static {
		INSTANCES.put(Boolean.class, BooleanAdapter.class);
		INSTANCES.put(Boolean.TYPE, BooleanAdapter.class);
		INSTANCES.put(Double.class, DoubleAdapter.class);
		INSTANCES.put(Double.TYPE, DoubleAdapter.class);
		INSTANCES.put(Float.class, FloatAdapter.class);
		INSTANCES.put(Float.TYPE, FloatAdapter.class);
		INSTANCES.put(Long.class, LongAdapter.class);
		INSTANCES.put(Long.TYPE, LongAdapter.class);
		INSTANCES.put(Integer.class, IntegerAdapter.class);
		INSTANCES.put(Integer.TYPE, IntegerAdapter.class);

		INSTANCES.put(String.class, StringAdapter.class);
		INSTANCES.put(Number.class, NumberAdapter.class);
		INSTANCES.put(Date.class, DateAdapter.class);

		INSTANCES.put(BigDecimal.class, BigDecimalAdapter.class);
		INSTANCES.put(Time.class, TimeAdapter.class);
		INSTANCES.put(byte[].class, ByteArrayAdapter.class);
	}

	/**
	 * This method attempts to register an adapter for a non existing data type. If
	 * there's already and adapter for that particular data type and the adapter is
	 * not overridable, an exception is raised;
	 * 
	 * @param adapter
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static void register(Adapter<?> adapter) {
		Class<?> dataType = adapter.getDataType();
		INSTANCES.put(dataType, (Class<? extends Adapter<?>>) adapter.getClass());
	}

	/**
	 * Finds the expected adapter class from the registered types. Gets it's
	 * constructor and invokes it with no parameters for a new instance; This method
	 * returns null if no adapter was found for that specific data type (Class<T>);
	 * 
	 * @param claz
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static synchronized <T> Adapter<T> createAdapter(Class<T> claz, String pattern) {
		if (claz.isArray()) {
			return new ArrayAdapter(claz, createAdapter(claz.getComponentType(), pattern));
		} else {
			Class<?> adapterClass = INSTANCES.get(claz);
			if (adapterClass == null)
				return null;
			else {
				try {
					Object instance = pattern == null || pattern.isEmpty()
							? adapterClass.getConstructor().newInstance()
							: adapterClass.getConstructor(String.class).newInstance(pattern);
					return (Adapter<T>) instance;
				} catch (Exception e) {
					throw new ShowStopper(e);
				}
			}
		}
	}

	public static synchronized <T> Adapter<T> createAdapter(Class<T> claz) {
		return createAdapter(claz, null);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static Adapter createAdapter(
			Class claz,
			Class adapterClass,
			final String pattern) {
		try {
			if (claz.isArray()) {
				if (adapterClass == null || adapterClass.equals(VoidAdapter.class)) {
					adapterClass = INSTANCES.get(claz.getComponentType());
				}
				if (adapterClass == null) {
					throw new ShowStopper("Can't find and adapter for " + claz);
				}
				Object instance = pattern == null || pattern.isEmpty()
						? adapterClass.getConstructor().newInstance()
						: adapterClass.getConstructor(String.class).newInstance(pattern);
				return new ArrayAdapter(claz, (Adapter) instance);
			} else if (List.class.isAssignableFrom(claz)) {
				if (claz.isInterface()) {
					claz = ArrayList.class;
				}
				return new ListAdapter<>(claz, null);
			} else {
				if (VoidAdapter.class.equals(adapterClass) || adapterClass == null) {
					adapterClass = INSTANCES.get(claz);
				}
				if (adapterClass == null) {
					throw new ShowStopper("Can't find and adapter for " + claz);
				} else {
					Object instance = pattern == null || pattern.isEmpty()
							? adapterClass.getConstructor().newInstance()
							: adapterClass.getConstructor(String.class).newInstance(pattern);
					return (Adapter) instance;
				}
			}

		} catch (Exception e) {
			throw new ShowStopper(e);
		}
	}
}
