package jports.xml;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import jports.adapters.Adapter;

/**
 * An adapter that can read and write to XML attribute nodes.
 * 
 * @author Rodrigo Portela
 *
 * @param <T>
 */
public class XmlAttributeAdapter implements XmlAdapter {

	private final String attributeName;
	private final Adapter<?> componentAdapter;

	public XmlAttributeAdapter(String attributeName, Adapter<?> componentAdapter) {
		this.attributeName = attributeName;
		this.componentAdapter = componentAdapter;
	}

	@Override
	public Object parse(Element parent) {
		return componentAdapter.parse(parent.getAttribute(attributeName));
	}

	@Override
	public void format(Document document, Element parent, Object xmlValue) {
		parent.setAttribute(attributeName, componentAdapter.formatObject(xmlValue));
	}

	@Override
	public Class<?> getDataType() {
		return componentAdapter.getDataType();
	}

}
