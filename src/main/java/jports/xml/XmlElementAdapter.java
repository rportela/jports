package jports.xml;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class XmlElementAdapter implements XmlAdapter {

	private final String xmlName;
	private final XmlAdapter xmlAdapter;

	public XmlElementAdapter(String xmlName, XmlAdapter xmlAdapter) {
		this.xmlName = xmlName;
		this.xmlAdapter = xmlAdapter;
	}

	public Object parse(Element parent) {

		for (Node child = parent.getFirstChild(); child != null; child = child.getNextSibling()) {
			if (child.getNodeType() == Node.ELEMENT_NODE &&
					xmlName.equalsIgnoreCase(child.getNodeName())) {
				return this.xmlAdapter.parse((Element) child);
			}
		}

		return null;
	}

	public void format(Document document, Element parent, Object xmlValue) {
		if (xmlValue != null) {
			Element element = document.createElement(this.xmlName);
			this.xmlAdapter.format(document, element, xmlValue);
			parent.appendChild(element);
		}
	}

	public Class<?> getDataType() {
		return this.xmlAdapter.getDataType();
	}

}
