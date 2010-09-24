package org.apache.camel.component.apns.util;

public class StringUtils {

	private StringUtils() {
		super();
	}

	public static boolean isNotEmpty(String stringValue) {
		if (stringValue == null) {
			return false;
		}
		return stringValue.length() > 0;
	}

	public static boolean isEmpty(String stringValue) {
		return !isNotEmpty(stringValue);
	}

	public static String trim(String stringValue) {
		if (stringValue == null) {
			return stringValue;
		}
		return stringValue.trim();
	}

}
