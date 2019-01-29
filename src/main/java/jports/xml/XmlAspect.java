package jports.xml;

import java.util.HashMap;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import jports.reflection.Aspect;
import jports.reflection.AspectMemberAccessor;

/**
 * A generic XML aspect that reads and writes objects to XML elements.
 * 
 * @author Rodrigo Portela
 *
 * @param <T>
 */
public class XmlAspect<T> extends Aspect<T, XmlAspectMember<T>> implements XmlAdapter {

	/**
	 * Gets the XML name of the expected child XML node;
	 */
	private final String xmlName;
	private static final HashMap<Class<?>, XmlAspect<?>> INSTANCES = new HashMap<>();

	/**
	 * Creates a new xml aspect instance;
	 * 
	 * @param dataType
	 */
	private XmlAspect(Class<T> dataType) {
		super(dataType);
		Xml annotation = dataType.getAnnotation(Xml.class);
		this.xmlName = annotation == null || annotation.name().isEmpty()
				? dataType.getSimpleName()
				: annotation.name();
	}

	/**
	 * Gets the single wrapper instance of a data type.
	 * 
	 * @param claz
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> XmlAspect<T> getInstance(Class<T> claz) {
		return (XmlAspect<T>) INSTANCES.putIfAbsent(claz, new XmlAspect<>(claz));
	}

	@Override
	public Object parse(Element parent) {
		if (xmlName.equalsIgnoreCase(parent.getTagName())) {
			T entity = newInstance();
			for (XmlAspectMember<T> member : this) {
				Object value = member.parse(parent);
				member.setValue(entity, value);
			}
			return entity;
		} else {
			return new XmlElementAdapter(xmlName, this).parse(parent);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public void format(Document document, Element parent, Object xmlValue) {
		if (xmlName.equalsIgnoreCase(parent.getTagName())) {
			T entity = (T) xmlValue;
			for (XmlAspectMember<T> member : this) {
				Object value = member.getValue(entity);
				member.format(document, parent, value);
			}
		} else {
			new XmlElementAdapter(xmlName, this).format(document, parent, xmlValue);
		}
	}

	@Override
	protected XmlAspectMember<T> visit(AspectMemberAccessor<T> accessor) {
		Xml annotation = accessor.getAnnotation(Xml.class);
		return annotation != null
				? new XmlAspectMember<>(accessor, annotation)
				: null;
	}

	@SuppressWarnings("unchecked")
	public T parse(Document document) {
		NodeList list = document.getElementsByTagName(xmlName);
		return list.getLength() > 0
				? (T) new XmlElementAdapter(xmlName, this).parse((Element) list.item(0))
				: null;
	}
}
