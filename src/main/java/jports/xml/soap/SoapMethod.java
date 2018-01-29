package jports.xml.soap;

public @interface SoapMethod {
	public String endpoint();

	public String name();

	public String targetNamespace();

}
