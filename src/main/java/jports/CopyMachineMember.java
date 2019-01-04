package jports;

import jports.reflection.AspectMember;

public class CopyMachineMember<T, D> {

	private final AspectMember<T> source;
	private final AspectMember<D> destination;

	public CopyMachineMember(AspectMember<T> source, AspectMember<D> destination) {
		this.source = source;
		this.destination = destination;
	}

	public void copy(T sourceEntity, D destinationEntity) {
		Object val = source.getValue(sourceEntity);
		destination.setValue(destinationEntity, val);
	}

	public String getSourceName() {
		return this.source.getName();
	}

	public String getDestinationName() {
		return this.destination.getName();
	}
}
