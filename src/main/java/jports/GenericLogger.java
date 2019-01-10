package jports;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

public class GenericLogger {

	private static final ObjectWriter JSON_WRITER = new ObjectMapper().writerWithDefaultPrettyPrinter();

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

	public static void infoJson(Object source, Object value) {
		try {
			if (value != null) {
				info(source, JSON_WRITER.writeValueAsString(value));
			}
		} catch (JsonProcessingException e) {
			error(source, e);
		}
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
