package jports;

import jports.reflection.AspectMember;

public class CopyMachineMember<TSource, TDest> {

	private final AspectMember<TSource> source;
	private final AspectMember<TDest> destination;

	public CopyMachineMember(AspectMember<TSource> source, AspectMember<TDest> destination) {
		this.source = source;
		this.destination = destination;
	}

	public void copy(TSource sourceEntity, TDest destinationEntity) {
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
