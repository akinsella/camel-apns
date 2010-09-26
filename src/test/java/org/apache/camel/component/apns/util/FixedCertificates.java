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

import java.io.InputStream;
import java.security.SecureRandom;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import com.notnoop.apns.internal.Utilities;

public class FixedCertificates {

    public static final String CLIENT_STORE = "clientStore.p12";
    public static final String CLIENT_PASSWD = "123456";

    public static final String SERVER_STORE = "serverStore.p12";
    public static final String SERVER_PASSWD = "123456";

    public static final int TEST_GATEWAY_PORT = 7654;
    public static final int TEST_FEEDBACK_PORT = 7843;
    public static final String TEST_HOST = "localhost";

    public static SSLContext serverContext() {
        try {
            System.setProperty("javax.net.ssl.trustStore", ClassLoader.getSystemResource(CLIENT_STORE).getPath());
            InputStream stream = ClassLoader.getSystemResourceAsStream(SERVER_STORE);
            SSLContext context = Utilities.newSSLContext(stream, SERVER_PASSWD, "PKCS12", "sunx509");

            return context;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static SSLContext clientContext() {
        try {
            InputStream stream = ClassLoader.getSystemResourceAsStream(CLIENT_STORE);
            SSLContext context = Utilities.newSSLContext(stream, CLIENT_PASSWD, "PKCS12", "sunx509");
            context.init(null, new TrustManager[] { new X509TrustManagerTrustAll() }, new SecureRandom());
            return context;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String clientCertPath() {
        return ClassLoader.getSystemResource(CLIENT_STORE).getPath();
    }

    static class X509TrustManagerTrustAll implements X509TrustManager {
        public boolean checkClientTrusted(java.security.cert.X509Certificate[] chain){
            return true;
        }

        public boolean isServerTrusted(java.security.cert.X509Certificate[] chain){
            return true;
        }

        public boolean isClientTrusted(java.security.cert.X509Certificate[] chain){
            return true;
        }

        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
            return null;
        }

        public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) {}

        public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) {}
    }

}
