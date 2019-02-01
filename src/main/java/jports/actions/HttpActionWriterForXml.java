package jports.actions;

import java.io.IOException;
import java.io.OutputStream;

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

	private void writeXml(ActionExecution<T, Document> execution, HttpAction action,
			Transformer transformer) throws IOException {
		try (OutputStream os = action
				.setContentType("application/xml")
				.getResponseStream()) {

			transformer.transform(new DOMSource(execution.getResult()), new StreamResult(os));
		} catch (TransformerException e) {
			GenericLogger.error(getClass(), e);
		}
	}

	/**
	 * Actually writes indented XML to the HTTP response output stream.
	 */
	@Override
	public void write(ActionExecution<T, Document> execution, HttpAction action) throws IOException {
		Transformer transformer;
		try {
			transformer = FACTORY.newTransformer();
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			writeXml(execution, action, transformer);
		} catch (TransformerConfigurationException e) {
			GenericLogger.error(getClass(), e);
		}

	}

}
