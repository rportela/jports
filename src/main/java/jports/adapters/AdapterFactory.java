package jports.adapters;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.sql.Time;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import jports.ShowStopper;
import jports.reflection.AspectMemberAccessor;

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

		INSTANCES.put(LocalDateTime.class, LocalDateTimeAdapter.class);
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
			return new ArrayAdapter(
					claz,
					createSimpleAdapter(
							null,
							claz.getComponentType(),
							pattern));
		} else {
			return createSimpleAdapter(
					null,
					claz,
					pattern);
		}
	}

	public static <T> Adapter<T> createAdapter(Class<T> claz) {
		return createAdapter(claz, null);
	}

	@SuppressWarnings("rawtypes")
	private static Adapter createSimpleAdapter(Class<?> adapterClass, Class<?> dataType, String pattern) {
		try {
			if (adapterClass == null || adapterClass == VoidAdapter.class) {
				adapterClass = INSTANCES.get(dataType);
				if (adapterClass == null) {
					throw new ShowStopper("Can't find and adapter for " + dataType);
				}
			}
			Object instance =
					pattern == null || pattern.isEmpty() ?
							adapterClass.getConstructor().newInstance() :
							adapterClass.getConstructor(String.class).newInstance(pattern);

			return (Adapter) instance;
		} catch (Exception e) {
			throw new ShowStopper(e);
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static <T> Adapter<T> createAdapter(
			final AspectMemberAccessor<?> accessor,
			final Class<?> adapterClass,
			final String pattern) {

		Class<?> dataType = accessor.getDataType();
		if (dataType.isArray() && !dataType.equals(byte[].class)) {
			return new ArrayAdapter(
					dataType,
					createSimpleAdapter(
							adapterClass,
							dataType.getComponentType(),
							pattern));
		} else if (List.class.isAssignableFrom(dataType)) {
			ParameterizedType type = (ParameterizedType) accessor.getGenericType();
			Type arg = type.getActualTypeArguments()[0];
			return new ListAdapter(
					dataType.isInterface() ? ArrayList.class : dataType,
					createSimpleAdapter(
							adapterClass,
							(Class<?>) arg,
							pattern));
		} else {
			return createSimpleAdapter(
					adapterClass,
					dataType,
					pattern);
		}
	}
}
