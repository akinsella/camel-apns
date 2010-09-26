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

import java.util.Random;

import org.apache.camel.component.apns.factory.ApnsServiceFactory;

import com.notnoop.apns.internal.Utilities;


public class ApnsUtils {

	private static Random random = new Random();
	
	private ApnsUtils() {
		super();
	}
	
	public static byte[] createRandomDeviceTokenBytes() {
    	byte[] deviceTokenBytes = new byte[32];
    	random.nextBytes(deviceTokenBytes);
        
        return deviceTokenBytes;
	}
	
	public static String encodeHexToken(byte[] deviceTokenBytes) {
        String deviceToken = Utilities.encodeHex(deviceTokenBytes);
        
        return deviceToken;
	}
	
    public static byte[] pack(byte[]... args) {
        int total = 0;
        for (byte[] arg : args)
            total += arg.length;

        byte[] result = new byte[total];

        int index = 0;
        for (byte[] arg : args) {
            System.arraycopy(arg, 0, result, index, arg.length);
            index += arg.length;
        }
        return result;
    }
    
    public static ApnsServiceFactory createDefaultTestConfiguration() {
        ApnsServiceFactory apnsServiceFactory = new ApnsServiceFactory();
        
        apnsServiceFactory.setFeedbackHost(FixedCertificates.TEST_HOST);
        apnsServiceFactory.setFeedbackPort(FixedCertificates.TEST_FEEDBACK_PORT);
        apnsServiceFactory.setGatewayHost(FixedCertificates.TEST_HOST);
        apnsServiceFactory.setGatewayPort(FixedCertificates.TEST_GATEWAY_PORT);
//        apnsServiceFactory.setCertificatePath("classpath:/" + FixedCertificates.CLIENT_STORE);
//        apnsServiceFactory.setCertificatePassword(FixedCertificates.CLIENT_PASSWD);
        apnsServiceFactory.setSslContext(FixedCertificates.clientContext());
        
        return apnsServiceFactory;
    }

	public static byte[] generateFeedbackBytes(byte[] deviceTokenBytes) {
        byte[] feedbackBytes = pack (/* time_t */  new byte[] {0, 0, 0, 0},
        /* length */  new byte[] { 0, 32 },
        /* device token */ deviceTokenBytes );
        
        return feedbackBytes;
	}

}
