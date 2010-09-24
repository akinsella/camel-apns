package org.apache.camel.component.apns.util;

public class ParamUtils {

	private ParamUtils() {
		super();
	}
	
	public static void checkNotNull(Object param, String paramValue) {
		AssertUtils.notNull(paramValue, param + " cannot be null");
	}
	
	public static void checkNotEmpty(String paramValue, String paramName) {
		AssertUtils.notNull(paramValue, paramName + " cannot be null");
		AssertUtils.isTrue(StringUtils.isNotEmpty(paramValue), paramName + " cannot be empty");
	}

	public static void checkDestination(String host, int port, String paramName) {
		if ( (StringUtils.isEmpty(host) && port != 0) ||
			 (StringUtils.isNotEmpty(host) && port == 0) ) {
			throw new IllegalArgumentException(paramName + "host and port parameters are not coherent: host=" + host + ", port=" + port);
		}
	}

}
