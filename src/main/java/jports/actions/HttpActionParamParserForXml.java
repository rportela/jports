package jports.actions;

import java.io.InputStream;

import javax.servlet.http.HttpServletRequest;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;

public class HttpActionParamParserForXml implements HttpActionParamParser {

	private static final XmlMapper XML_MAPPER = new XmlMapper();

	@Override
	public <T> T parseParams(Class<T> paramsClass, HttpServletRequest request) throws Exception {
		try (InputStream is = request.getInputStream()) {
			return XML_MAPPER.readValue(is, paramsClass);
		}

	}

}
