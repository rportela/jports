package jports.tests;

import org.junit.Assert;
import org.junit.Test;

import jports.validations.Length;
import jports.validations.LengthValidation;
import jports.validations.Required;
import jports.validations.RequiredValidation;
import jports.validations.Validation;
import jports.validations.ValidationAspect;
import jports.validations.ValidationAspectMember;

public class ValidationTests {

	public static class MyEntity {

		@Required
		public String name;

		@Length(min = 3, max = 10)
		public String name2;

	}

	@Test
	public void requiredValidationTest() {

		ValidationAspect<MyEntity> aspect = ValidationAspect.getInstance(MyEntity.class);
		ValidationAspectMember<MyEntity> member = aspect.get("name");

		Assert.assertNotNull("There should be a member named 'name'", member);
		Assert.assertFalse("There should be one validation on member", member.getValidations().isEmpty());

		Validation validation = member.getValidations().get(0);

		Assert.assertTrue("The validation should be a required validation",
				RequiredValidation.class.equals(validation.getClass()));

	}

	@Test
	public void lengthValidationTest() {

		ValidationAspect<MyEntity> aspect = ValidationAspect.getInstance(MyEntity.class);
		ValidationAspectMember<MyEntity> member = aspect.get("name2");

		Assert.assertNotNull("There should be a member named 'name2'", member);
		Assert.assertFalse("There should be one validation on member", member.getValidations().isEmpty());

		Validation validation = member.getValidations().get(0);

		Assert.assertTrue("The validation should be a length validation",
				LengthValidation.class.equals(validation.getClass()));

	}

}
