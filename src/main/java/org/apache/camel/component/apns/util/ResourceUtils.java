/**
 * Copyright (C) 2010 Alexis Kinsella - http://www.helyx.org - <Helyx.org>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
