package jports.actions;

import java.io.IOException;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.xml.XMLConstants;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import jports.GenericLogger;

/**
 * This is an action writer made specifically for XML documents. It will reuse a
 * static Transformer Factory and output indented XML to the HTTP servlet output
 * stream.
 * 
 * @author rportela
 *
 * @param <T>
 */
public class HttpActionWriterForXml<T> implements HttpActionWriter<T, Document> {

	private static final TransformerFactory FACTORY;
	static {
		FACTORY = TransformerFactory.newInstance();
		FACTORY.setAttribute(XMLConstants.ACCESS_EXTERNAL_DTD, "");
		FACTORY.setAttribute(XMLConstants.ACCESS_EXTERNAL_STYLESHEET, "");
	}

	private void writeXml(ActionExecution<T, Document> execution, HttpServletResponse response,
			Transformer transformer) throws IOException {
		try (ServletOutputStream os = response.getOutputStream()) {
			response.setContentType("application/xml");
			transformer.transform(new DOMSource(execution.getResult()), new StreamResult(os));
		} catch (TransformerException e) {
			GenericLogger.error(this, e);
		}
	}

	/**
	 * Actually writes indented XML to the HTTP response output stream.
	 */
	@Override
	public void write(ActionExecution<T, Document> execution, HttpServletResponse response) throws IOException {
		Transformer transformer;
		try {
			transformer = FACTORY.newTransformer();
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			writeXml(execution, response, transformer);
		} catch (TransformerConfigurationException e) {
			GenericLogger.error(this, e);
		}

	}

}
