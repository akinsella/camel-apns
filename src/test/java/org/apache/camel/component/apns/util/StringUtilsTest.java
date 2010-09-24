package org.apache.camel.component.apns.util;

import org.apache.camel.component.apns.util.StringUtils;
import org.junit.Assert;
import org.junit.Test;

public class StringUtilsTest {

	@Test
	public void testIsEmpty() {
		Assert.assertFalse(StringUtils.isEmpty("test"));
		Assert.assertFalse(StringUtils.isEmpty("a"));
		Assert.assertTrue(StringUtils.isEmpty(""));
		Assert.assertTrue(StringUtils.isEmpty(null));
	}
	
	@Test
	public void testIsNotEmpty() {
		Assert.assertTrue(StringUtils.isNotEmpty("test"));
		Assert.assertTrue(StringUtils.isNotEmpty("a"));
		Assert.assertFalse(StringUtils.isNotEmpty(""));
		Assert.assertFalse(StringUtils.isNotEmpty(null));
	}
	
	@Test
	public void testTrim() {
		Assert.assertEquals("", StringUtils.trim(""));
		Assert.assertEquals("", StringUtils.trim(" "));
		
		Assert.assertEquals("test", StringUtils.trim("test"));
		Assert.assertEquals("test", StringUtils.trim("test "));
		Assert.assertEquals("test", StringUtils.trim(" test"));
		Assert.assertEquals("test", StringUtils.trim(" test "));
		
		Assert.assertEquals(null, StringUtils.trim(null));
	}
	
}
