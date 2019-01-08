package jports;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

/**
 * This class is specialized in reading and writing settings as a JSON file in
 * the root of your class path; By default, you should add a file named
 * settings.json to the root of your project and setup everything there.
 * 
 * @author rportela
 *
 */
public class Settings {

	private static final String FILE_NAME = "/settings.json";

	private Settings() {
	}

	private static final Map<String, Object> VALUES;

	static {
		InputStream in = Settings.class.getResourceAsStream(FILE_NAME);
		if (in == null) {
			VALUES = new HashMap<>();
		} else {
			InputStreamReader reader = new InputStreamReader(in, StandardCharsets.UTF_8);
			Type typeOfT = new TypeToken<HashMap<String, Object>>() {
			}.getType();
			VALUES = new Gson().fromJson(reader, typeOfT);
		}
	}

	/**
	 * Saves the contents of the settings to a file named settings.json in the root
	 * of the class path loader.
	 */
	public static final synchronized void save() {
		URL resource = Settings.class.getResource(FILE_NAME);
		String fileName;
		if (resource == null) {
			resource = Settings.class.getResource("/");
			fileName = resource.getPath() + FILE_NAME;
		} else {
			fileName = resource.getPath();
		}

		try (FileOutputStream fos = new FileOutputStream(fileName)) {
			String json = new GsonBuilder().setPrettyPrinting().create().toJson(VALUES);
			fos.write(json.getBytes(StandardCharsets.UTF_8));
		} catch (IOException e) {
			Logger.getGlobal().severe(e.toString());
		}
	}

	/**
	 * Gets a setting by name or null if it doesen't exist.
	 * 
	 * @param key
	 * @return
	 */
	public static final Object get(String key) {
		return VALUES.get(key);
	}

	/**
	 * Gets a setting by name or a default value if it doesen't exist.
	 * 
	 * @param key
	 * @param defaultValue
	 * @return
	 */
	public static final Object get(String key, Object defaultValue) {
		Object v = VALUES.get(key);
		return v == null
				? defaultValue
				: v;
	}

	/**
	 * Sets a setting by name.
	 * 
	 * @param key
	 * @param value
	 */
	public static final void put(String key, Object value) {
		VALUES.put(key, value);
	}

	/**
	 * Removes the specific setting by name from the underlying map.
	 * 
	 * @param key
	 */
	public static final void delete(String key) {
		VALUES.remove(key);
	}

}
