package org.apache.camel.component.apns.util;

import java.io.BufferedInputStream;
import java.io.Closeable;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import com.notnoop.exceptions.RuntimeIOException;

public class ResourceUtils {

	private static final String CLASSPATH_PREFIX = "classpath:";
	
	private ResourceUtils() {
		super();
	}

	public static boolean isClasspathResource(String path) {
		return path.startsWith(CLASSPATH_PREFIX);
	}
	
	public static String getClasspathResourcePath(String path) {
		return path.substring(CLASSPATH_PREFIX.length());
	}

	public static InputStream getInputStream(String path) {

		InputStream is = null;
		
		if (isClasspathResource(path)) {
			String classpathResourcePath = ResourceUtils.getClasspathResourcePath(path);
			is = ResourceUtils.class.getResourceAsStream(classpathResourcePath);
		    if (is == null) {
		    	throw new RuntimeIOException("Certificate stream is null: '" + classpathResourcePath + "'");
		    }
		}
		else {
			try {
				is = new BufferedInputStream(new FileInputStream(path));
			} catch (FileNotFoundException e) {
				throw new RuntimeIOException(e);
			}
		}
	    
	    return is;
	}
	
	public static void close(Closeable closeable) {
		if (closeable != null) {
			try {
				closeable.close();
			}
			catch (IOException e) { /* Nothing to do */ }
		}
	}
	
}
