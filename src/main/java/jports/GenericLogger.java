package jports;

import java.util.logging.Level;
import java.util.logging.Logger;

public class GenericLogger {

	private GenericLogger() {
	}

	public static Logger getLogger(Object source) {
		return source == null
				? Logger.getGlobal()
				: Logger.getLogger(source.getClass().getName());
	}

	public static void info(Object source, Throwable t) {
		info(source, t.getMessage(), t);
	}

	public static void info(Object source, String message, Throwable t) {
		getLogger(source).log(Level.INFO, message, t);
	}

	public static void info(Object source, String message) {
		getLogger(source).log(Level.INFO, message);
	}

	public static void error(Object source, Throwable t) {
		error(source, t.getMessage(), t);
	}

	public static void error(Object source, String message, Throwable t) {
		getLogger(source).log(Level.SEVERE, message, t);
	}

	public static void warn(Object source, Throwable t) {
		warn(source, t.getMessage(), t);
	}

	public static void warn(Object source, String message, Throwable t) {
		getLogger(source).log(Level.WARNING, message, t);
	}

	public static void warn(Object source, String message) {
		getLogger(source).log(Level.WARNING, message);
	}

}
