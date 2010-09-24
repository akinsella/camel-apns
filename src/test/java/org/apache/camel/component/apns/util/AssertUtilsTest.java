package org.apache.camel.component.apns.util;

import org.apache.camel.component.apns.util.AssertUtils;
import org.junit.Assert;
import org.junit.Test;

public class AssertUtilsTest {

	@Test
	public void testAssertIsTrueValid() {
		AssertUtils.isTrue(true, " message");
	}
	
	@Test
	public void testAssertIsTrueInvalid() {
		try {
			AssertUtils.isTrue(false, "message");
		}
		catch(IllegalArgumentException e) { 
			Assert.assertEquals("[Assert is true] message", e.getMessage());
		}
	}
	
	@Test
	public void testAssertNoNullValid() {
		AssertUtils.notNull(new Object(), " message");
	}
	
	@Test
	public void testAssertNotNullInvalid() {
		try {
			AssertUtils.notNull(null, "message");
		}
		catch(IllegalArgumentException e) { 
			Assert.assertEquals("[Assert not null] message", e.getMessage());
		}
	}
	
}
