package jports.xml;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * A class that can read from and write to XML node elements; An extension to an
 * object adapter;
 * 
 * @author Rodrigo Portela
 *
 */
public interface XmlAdapter {

	/**
	 * Parses a parent element, locating attributes, child elements or text nodes
	 * inside it.
	 * 
	 * @param parent
	 * @return
	 */
	public Object parse(Element parent);

	/**
	 * Creates an XML element, text node or attribute and append it to a parent
	 * element. The owner document is passed as an argument to help in creating
	 * child XML nodes.
	 * 
	 * @param document
	 * @param parent
	 * @param xmlValue
	 */
	public void format(Document document, Element parent, Object xmlValue);

	/**
	 * Gets the data type of the object being wrapped by the XML adapter;
	 * 
	 * @return
	 */
	public Class<?> getDataType();
}
