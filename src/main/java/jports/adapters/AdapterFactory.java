package jports.adapters;

import java.lang.reflect.Constructor;
import java.util.HashMap;

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
	private static final HashMap<Class<?>, Class<?>> INSTANCES = new HashMap<Class<?>, Class<?>>();

	/**
	 * Populates the static map with factory built adapters;
	 */
	static {
		INSTANCES.put(Double.class, DoubleAdapter.class);
		INSTANCES.put(Double.TYPE, DoubleAdapter.class);
		INSTANCES.put(String.class, StringAdapter.class);
		INSTANCES.put(Number.class, NumberAdapter.class);
	}

	/**
	 * This method attempts to register an adapter for a non existing data type. If
	 * there's already and adapter for that particular data type and the adapter is
	 * not overridable, an exception is raised;
	 * 
	 * @param adapter
	 * @return
	 */
	public static void register(Adapter<?> adapter) {
		Class<?> dataType = adapter.getDataType();
		INSTANCES.put(dataType, adapter.getClass());
	}

	/**
	 * Finds the expected adapter class from the registered types. Gets it's
	 * constructor and invokes it with no parameters for a new instance; This method
	 * returns null if no adapter was found for that specific data type (Class<T>);
	 * 
	 * @param claz
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static synchronized <T> Adapter<T> getInstance(Class<T> claz) {
		Class<?> adapterClass = INSTANCES.get(claz);
		if (adapterClass == null)
			return null;
		else {
			try {
				Constructor<?> constructor = adapterClass.getConstructor();
				Object newInstance = constructor.newInstance();
				return (Adapter<T>) newInstance;
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
	}
}
