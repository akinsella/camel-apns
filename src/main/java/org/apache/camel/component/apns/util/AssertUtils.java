package org.apache.camel.component.apns.util;

public class AssertUtils {

	public static void notNull(Object object, String message) {
		if (object == null) {
			throw new IllegalArgumentException("[Assert not null] " + message);
		}
	}

	public static void isTrue(boolean booleanValue, String message) {
		if (!booleanValue) {
			throw new IllegalArgumentException("[Assert is true] " + message);
		}
	}
	
}
