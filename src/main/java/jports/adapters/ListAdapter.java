package jports.adapters;

import java.lang.reflect.Array;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import jports.ShowStopper;

public class ListAdapter<T> implements Adapter<List<T>> {

	private final Class<? extends List<T>> listClass;
	private final Adapter<T> adapter;
	private String separator = ";";

	public ListAdapter(Class<? extends List<T>> listClass, Adapter<T> adapter) {
		this.listClass = listClass;
		this.adapter = adapter;
	}

	@Override

	public List<T> parse(String source) {
		if (source == null || source.isEmpty())
			return Collections.emptyList();
		else {
			String[] vals = source.split(separator);
			List<T> list = newInstance(vals.length);
			for (int i = 0; i < vals.length; i++) {
				T item = adapter.parse(vals[i]);
				list.add(item);
			}
			return list;
		}
	}

	@Override
	public String format(List<T> source) {
		if (source == null)
			return null;
		else
			return String.join(
					this.separator,
					source
							.stream()
							.map(adapter::format)
							.collect(Collectors.toList()));
	}

	public String getSeparator() {
		return this.separator;
	}

	public ListAdapter<T> setSeparator(String value) {
		this.separator = value;
		return this;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<T> convert(Object source) {
		if (source == null)
			return Collections.emptyList();
		else if (listClass.isInstance(source))
			return ((List<T>) source);
		else if (source.getClass().isArray())
			return fromArray(source);
		else if (source instanceof String)
			return parse((String) source);
		else
			throw new ShowStopper("Can't convert " + source + " to " + listClass);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Class<List<T>> getDataType() {
		return (Class<List<T>>) this.listClass;
	}

	@SuppressWarnings("unchecked")
	public List<T> fromArray(Object array) {
		int length = Array.getLength(array);
		List<T> list = newInstance(length);
		for (int i = 0; i < length; i++)
			list.add((T) Array.get(array, i));
		return list;
	}

	public List<T> newInstance(int size) {
		try {
			return listClass
					.getConstructor(Integer.TYPE)
					.newInstance(size);

		} catch (Exception e) {
			throw new ShowStopper(e);
		}
	}

}
