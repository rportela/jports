package jports.tests;

import java.util.ArrayList;

import org.junit.Test;

import jports.GenericLogger;
import jports.reflection.TypeInfo;

public class ReflectionTests {

	@Test
	public void getListComponentType() {
		TypeInfo<ArrayList<Integer>> typeInfo = new TypeInfo<ArrayList<Integer>>();
		Class<?> x = (Class<?>) typeInfo.getGenericType(0);
		GenericLogger.info(getClass(), x);
	}

}
