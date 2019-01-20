package jports;

import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

public class GenericLogger {
	private static final Logger GLOBAL = Logger.getGlobal();
	private static final ObjectWriter JSON_WRITER = new ObjectMapper().writerWithDefaultPrettyPrinter();

	private static final boolean shouldJson(Object item) {
		return !(item instanceof Throwable) && (item.getClass().getName().startsWith("java") &&
				!(item instanceof List) &&
				!(item instanceof Map));

	}

	public static final void log(String source, Level level, Object... items) {
		Logger logger = source == null || source.isEmpty()
				? GLOBAL
				: Logger.getLogger(source);

		for (int i = 0; i < items.length; i++) {
			Object item = items[i];
			if (item != null) {
				String itemContent;
				try {
					itemContent = shouldJson(item)
							? JSON_WRITER.writeValueAsString(item)
							: item.toString();
				} catch (JsonProcessingException e) {
					itemContent = e.toString();
				}
				logger.log(level, itemContent);
			}
		}
	}

	public static final void error(String source, Object... items) {
		log(source, Level.SEVERE, items);
	}

	public static final void info(String source, Object... items) {
		log(source, Level.INFO, items);
	}

	public static final void error(Class<?> source, Object... items) {
		log(source.getName(), Level.SEVERE, items);
	}

	public static final void info(Class<?> source, Object... items) {
		log(source.getName(), Level.INFO, items);
	}

}
