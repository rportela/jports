package jports;

import java.util.ArrayList;

import jports.reflection.AspectMember;
import jports.reflection.DefaultAspect;

public class CopyMachine<TSource, TDest> {

	private final DefaultAspect<TSource> source;
	private final DefaultAspect<TDest> destination;
	private final ArrayList<CopyMachineMember<TSource, TDest>> members;

	public CopyMachine(Class<TSource> source, Class<TDest> dest) {
		this.source = new DefaultAspect<>(source);
		this.destination = new DefaultAspect<>(dest);
		this.members = new ArrayList<>(this.source.size());
		for (AspectMember<TSource> sourceMember : this.source) {
			int destIndex = this.destination.indexOf(sourceMember.getName());
			if (destIndex >= 0) {
				AspectMember<TDest> destMember = this.destination.get(destIndex);
				if (!destMember.isReadOnly()) {
					members.add(new CopyMachineMember<>(sourceMember, destMember));
				}
			}
		}
	}

	public void map(String sourceName, String destName) {
		AspectMember<TSource> sourceMember = source.get(sourceName);
		AspectMember<TDest> destinationMember = destination.get(destName);
		if (destinationMember.isReadOnly()) {
			throw new RuntimeException(
					"Destination member cannot be readonly or the Copy Machine will fail: " + destinationMember);
		}
		this.members.add(new CopyMachineMember<>(sourceMember, destinationMember));
	}

	public void copy(TSource sourceEntity, TDest destinationEntity) {
		for (CopyMachineMember<TSource, TDest> member : this.members)
			member.copy(sourceEntity, destinationEntity);
	}

	public TDest copy(TSource sourceEntity) {
		TDest destinationEntity = this.destination.newInstance();
		copy(sourceEntity, destinationEntity);
		return destinationEntity;
	}

}
