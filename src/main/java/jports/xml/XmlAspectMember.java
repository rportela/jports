package jports.xml;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import jports.ShowStopper;
import jports.adapters.AdapterFactory;
import jports.reflection.AspectMember;
import jports.reflection.AspectMemberAccessor;

/**
 * A representation of xml aspect member.
 * 
 * @author Rodrigo Portela
 *
 */
public class XmlAspectMember<T> extends AspectMember<T> implements XmlAdapter {

	/**
	 * The XML adapter that will be used to read and write to XML nodes.
	 */
	private final XmlAdapter xmlAdapter;

	/**
	 * Creates a new instance of the XML aspect member.
	 * 
	 * @param field
	 * @param annotation
	 */
	public XmlAspectMember(AspectMemberAccessor<T> accessor, Xml annotation) {
		super(accessor);
		this.xmlAdapter = createXmlAdapter(
				accessor,
				annotation.name().isEmpty()
						? accessor.getName()
						: annotation.name(),
				annotation);
	}

	/**
	 * This method decides whether to create an array XML adapter or a list adapter
	 * or a standard XML element adapter using the provided XML name and component
	 * Adapter.
	 * 
	 * @param xmlName
	 * @param componentAdapter
	 * @return
	 */
	private XmlAdapter createXmlAdapterWith(AspectMemberAccessor<T> accessor, String xmlName,
			XmlAdapter componentAdapter) {
		Class<?> dataType = getDataType();
		if (dataType.isArray()) {
			return new XmlArrayAdapter(xmlName, componentAdapter);
		} else if (List.class.isAssignableFrom(dataType)) {
			return dataType.isInterface()
					? new XmlListAdapter(xmlName, ArrayList.class, componentAdapter)
					: new XmlListAdapter(xmlName, dataType, componentAdapter);
		} else {
			return new XmlElementAdapter(xmlName, componentAdapter);
		}
	}

	/**
	 * This method decides which XML adapter to build based on the XML Type present
	 * on the XML annotation.
	 * 
	 * @param xmlName
	 * @param annotation
	 * @return
	 */
	private XmlAdapter createXmlAdapter(AspectMemberAccessor<T> accessor, String xmlName, Xml annotation) {
		switch (annotation.type()) {
		case ATTRIBUTE:
			return new XmlAttributeAdapter(
					xmlName,
					AdapterFactory.createAdapter(
							accessor,
							annotation.adapter(),
							annotation.pattern()));
		case CDATA:
			return createXmlAdapterWith(
					accessor,
					xmlName,
					new XmlCdataAdapter(
							AdapterFactory.createAdapter(
									accessor,
									annotation.adapter(),
									annotation.pattern())));
		case ELEMENT:
		case TEXT:
			return createXmlAdapterWith(
					accessor,
					xmlName,
					new XmlTextAdapter(
							AdapterFactory.createAdapter(
									accessor,
									annotation.adapter(),
									annotation.pattern())));
		default:
			throw new ShowStopper("Unimplemented XML Type: " + annotation.type());
		}
	}

	@Override
	public Object parse(Element parent) {
		return this.xmlAdapter.parse(parent);
	}

	@Override
	public void format(Document document, Element parent, Object xmlValue) {
		this.xmlAdapter.format(document, parent, xmlValue);
	}
}
