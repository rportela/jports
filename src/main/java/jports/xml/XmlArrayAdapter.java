package jports.xml;

import java.lang.reflect.Array;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 * An XML adapter that can read and write arrays as child elements of a XML
 * node.
 * 
 * @author Rodrigo Portela
 *
 * @param <T>
 */
public class XmlArrayAdapter implements XmlAdapter {

	private final String xmlName;
	private final XmlAdapter componentAdapter;

	public XmlArrayAdapter(String xmlName, XmlAdapter componentAdapter) {
		this.xmlName = xmlName;
		this.componentAdapter = componentAdapter;
	}

	@Override
	public Object parse(Element parent) {
		if (parent == null)
			return null;
		NodeList list = parent.getElementsByTagName(xmlName);
		int length = list.getLength();
		Object array = Array.newInstance(componentAdapter.getDataType(), length);
		for (int i = 0; i < length; i++) {
			Element element = (Element) list.item(i);
			Object item = componentAdapter.parse(element);
			Array.set(array, i, item);
		}
		return array;
	}

	@Override
	public void format(Document document, Element parent, Object xmlValue) {
		if (xmlValue != null) {
			int length = Array.getLength(xmlValue);
			for (int i = 0; i < length; i++) {
				Object item = Array.get(xmlValue, i);
				Element element = document.createElement(xmlName);
				componentAdapter.format(document, element, item);
				parent.appendChild(element);
			}
		}
	}

	@Override
	public Class<?> getDataType() {
		return Array.class;
	}

}
