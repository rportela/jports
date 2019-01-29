package jports.xml;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;

import jports.ShowStopper;
import jports.adapters.Adapter;

/**
 * An XML adapter that can read from and write to XML text nodes;
 * 
 * @author Rodrigo Portela
 *
 * @param <T>
 */
public class XmlTextAdapter implements XmlAdapter {

	/**
	 * The component adapter to parse and format text content;
	 */
	private final Adapter<?> componentAdapter;

	/**
	 * Creates a new simple XML text adapter with a specific component adapter;
	 * 
	 * @param componentAdapter
	 */
	public XmlTextAdapter(Adapter<?> componentAdapter) {
		if (componentAdapter == null)
			throw new ShowStopper("The component adapter cannot be null");
		this.componentAdapter = componentAdapter;

	}

	/**
	 * Gets the data type of this XML adapter.
	 */
	public Class<?> getDataType() {
		return this.componentAdapter.getDataType();
	}

	@Override
	public Object parse(Element parent) {
		return parent == null
				? null
				: componentAdapter.parse(parent.getTextContent());
	}

	@Override
	public void format(Document document, Element parent, Object xmlValue) {
		if (xmlValue != null) {
			Text textNode = document
					.createTextNode(
							componentAdapter
									.formatObject(xmlValue));
			parent.appendChild(textNode);
		}
	}

}
