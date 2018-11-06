package jports.actions;

import java.io.InputStream;
import java.lang.reflect.Field;
import java.nio.charset.Charset;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;

public class HttpActionParamParserForMultipart implements HttpActionParamParser {

	@Override
	public <T> T parseParams(Class<T> paramsClass, HttpServletRequest request) throws Exception {

		String encoding = request.getCharacterEncoding();

		Charset charset = Charset.forName(
				encoding == null || encoding.isEmpty() ?
						"UTF-8" :
						encoding);

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
						InputStream is = part.getInputStream();
						is.read(bytes);
						is.close();
						String txt = new String(bytes, charset);
						field.set(target, txt);
					}
				}
			} catch (Exception e) {
				throw new RuntimeException(
						String.join(
								" ",
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
