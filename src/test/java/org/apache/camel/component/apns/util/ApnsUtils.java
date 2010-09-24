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
