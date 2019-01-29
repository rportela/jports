package jports.xml;

import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import jports.ShowStopper;

/**
 * An xml adapter that can read and write objects as xml child elements.
 * 
 * @author Rodrigo Portela
 *
 * @param <T>
 */
public class XmlListAdapter implements XmlAdapter {

	private final Class<?> listClass;
	private final String xmlName;
	private final XmlAdapter componentAdapter;

	public XmlListAdapter(String xmlName, Class<?> listClass, XmlAdapter componentAdapter) {
		this.xmlName = xmlName;
		this.listClass = listClass;
		this.componentAdapter = componentAdapter;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public Object parse(Element parent) {
		try {
			NodeList nodes = parent.getElementsByTagName(xmlName);
			int length = nodes.getLength();
			List list = (List) listClass
					.getConstructor(Integer.TYPE)
					.newInstance(length);

			for (int i = 0; i < length; i++) {
				Object item = componentAdapter.parse((Element) nodes.item(i));
				list.add(item);
			}
			return list;

		} catch (Exception e) {
			throw new ShowStopper(e);
		}

	}

	@SuppressWarnings("rawtypes")
	@Override
	public void format(Document document, Element parent, Object xmlValue) {
		if (xmlValue != null) {
			List list = (List) xmlValue;
			for (Object item : list) {
				Element element = document.createElement(xmlName);
				componentAdapter.format(document, element, item);
				parent.appendChild(element);
			}
		}
	}

	@Override
	public Class<?> getDataType() {
		return listClass;
	}

}
