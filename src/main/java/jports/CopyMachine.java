package jports;

import java.util.ArrayList;

import jports.reflection.AspectMember;
import jports.reflection.DefaultAspect;

public class CopyMachine<T, D> {

	private final DefaultAspect<T> source;
	private final DefaultAspect<D> destination;
	private final ArrayList<CopyMachineMember<T, D>> members;

	public CopyMachine(Class<T> source, Class<D> dest) {
		this.source = new DefaultAspect<>(source);
		this.destination = new DefaultAspect<>(dest);
		this.members = new ArrayList<>(this.source.size());
		for (AspectMember<T> sourceMember : this.source) {
			int destIndex = this.destination.indexOf(sourceMember.getName());
			if (destIndex >= 0) {
				AspectMember<D> destMember = this.destination.get(destIndex);
				if (!destMember.isReadOnly()) {
					members.add(new CopyMachineMember<>(sourceMember, destMember));
				}
			}
		}
	}

	public static <T, D> CopyMachine<T, D> create(Class<T> source, Class<D> dest) {
		return new CopyMachine<>(source, dest);
	}

	public void map(String sourceName, String destName) {
		AspectMember<T> sourceMember = source.get(sourceName);
		AspectMember<D> destinationMember = destination.get(destName);
		if (destinationMember.isReadOnly()) {
			throw new ShowStopper(
					"Destination member cannot be readonly or the Copy Machine will fail: " + destinationMember);
		}
		this.members.add(new CopyMachineMember<>(sourceMember, destinationMember));
	}

	public void copy(T sourceEntity, D destinationEntity) {
		for (CopyMachineMember<T, D> member : this.members)
			member.copy(sourceEntity, destinationEntity);
	}

	public D copy(T sourceEntity) {
		D destinationEntity = this.destination.newInstance();
		copy(sourceEntity, destinationEntity);
		return destinationEntity;
	}

}
