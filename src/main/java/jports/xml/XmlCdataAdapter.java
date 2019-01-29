package jports.xml;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.Text;

import jports.adapters.Adapter;

/**
 * An XML adapter that can read from and write to XML CDATA nodes;
 * 
 * @author Rodrigo Portela
 *
 */
public class XmlCdataAdapter implements XmlAdapter {

	private final Adapter<?> componentAdapter;

	/**
	 * Creates a new simple XML text adapter;
	 * 
	 * @param componentAdapter
	 */
	public XmlCdataAdapter(Adapter<?> componentAdapter) {
		this.componentAdapter = componentAdapter;
	}

	/**
	 * Parses
	 */
	@Override
	public Object parse(Element parent) {
		StringBuilder builder = new StringBuilder(1024);
		for (Node child = parent.getFirstChild(); child != null; child = child.getNextSibling()) {
			if (child.getNodeType() == Node.CDATA_SECTION_NODE) {
				builder.append(child.getTextContent());
			}
		}
		return componentAdapter.parse(builder.toString());
	}

	/**
	 * Formats the value as CDATA inside a created Element appended do the parent.
	 */
	@Override
	public void format(Document document, Element parent, Object xmlValue) {
		if (xmlValue != null) {
			Text cdata = document
					.createCDATASection(
							componentAdapter
									.formatObject(xmlValue));
			parent.appendChild(cdata);
		}
	}

	/**
	 * Gets the data type of the underlying component adapter.
	 */
	@Override
	public Class<?> getDataType() {
		return componentAdapter.getDataType();
	}

}
