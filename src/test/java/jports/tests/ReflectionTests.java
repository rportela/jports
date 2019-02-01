package jports.tests;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import jports.adapters.AdapterAspect;
import jports.adapters.AdapterAspectMember;
import jports.adapters.ListAdapter;

public class ReflectionTests {

	public static class Entity {
		public List<Integer> values;
	}

	@Test
	public void getListComponentType() {

		AdapterAspect<Entity> aspect = AdapterAspect.getInstance(Entity.class);

		AdapterAspectMember<Entity> member = aspect.get(0);

		Assert.assertTrue(
				"The first member adapter should be a list adapter",
				ListAdapter.class.isInstance(member.getAdapter()));
	}

}
