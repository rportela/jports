package jports.actions;

import java.lang.reflect.InvocationTargetException;

import javax.servlet.http.HttpServletRequest;

public interface HttpActionParser {

	public default Action<?, ?> instantiate(String name)
			throws InstantiationException,
			IllegalAccessException,
			IllegalArgumentException,
			InvocationTargetException,
			NoSuchMethodException,
			SecurityException,
			ClassNotFoundException {
		return (Action<?, ?>) Class.forName(name).getConstructor().newInstance();
	}

	public <T> T parseParams(Class<T> paramsClass, HttpServletRequest request) throws Exception;
}
