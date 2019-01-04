package jports.actions;

import java.io.InputStream;
import java.lang.reflect.Field;
import java.nio.charset.Charset;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;

import jports.ShowStopper;

/**
 * This class wraps the parser for enctype multipart/form-data, that may include
 * file parts to be processed by an action;
 * 
 * @author rportela
 *
 */
public class HttpActionParamParserForMultipart implements HttpActionParamParser {

	/**
	 * Parses the HTTP request as enctype multipart/form-data into expected Action
	 * parameters base on the params Class;
	 */
	@Override
	public <T> T parseParams(Class<T> paramsClass, HttpServletRequest request) throws Exception {

		String encoding = request.getCharacterEncoding();

		Charset charset = Charset.forName(
				encoding == null || encoding.isEmpty()
						? "UTF-8"
						: encoding);

		T target = paramsClass.getConstructor().newInstance();
		Field[] fields = paramsClass.getFields();
		for (int i = 0; i < fields.length; i++) {
			Field field = fields[i];
			try {
				Part part = request.getPart(field.getName());
				if (part != null) {
					if (Part.class.isAssignableFrom(field.getType())) {
						field.set(target, part);
					} else {
						byte[] bytes = new byte[(int) part.getSize()];
						int r = -1;
						try (InputStream is = part.getInputStream()) {
							r = is.read(bytes);
						}
						if (r > 0) {
							field.set(target, new String(bytes, 0, r, charset));
						}
					}
				}
			} catch (Exception e) {
				throw new ShowStopper(
						String.join(
								"Unable to set value to field",
								paramsClass.getSimpleName() + "." + field.getName(),
								"(" + field.getType() + ")",
								":",
								e.getMessage()));
			}
		}
		return target;
	}

}
