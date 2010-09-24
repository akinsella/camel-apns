package org.apache.camel.component.apns.factory;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.apache.camel.component.apns.factory.ApnsServiceFactory;
import org.apache.camel.component.apns.model.ConnectionStrategy;
import org.apache.camel.component.apns.util.FixedCertificates;
import org.junit.Test;

import com.notnoop.apns.ApnsService;

public class ApnsServiceFactoryTest {

	@Test
	public void testApnsServiceFactoryWithFixedCertificates() {
        ApnsServiceFactory apnsServiceFactory = createApnsServiceFactoryWithFixedCertificates();
        ApnsService apnsService = apnsServiceFactory.getApnsService();
        
        doBasicAsserts(apnsService);
	}

	@Test(expected=IllegalArgumentException.class)
	public void testApnsServiceFactoryAsPool0() {
        ApnsServiceFactory apnsServiceFactory = createApnsServiceFactoryWithFixedCertificatesAsPool(0);
        ApnsService apnsService = apnsServiceFactory.getApnsService();
     
        doBasicAsserts(apnsService);
	}

	@Test
	public void testApnsServiceFactoryAsPool1() {
        ApnsServiceFactory apnsServiceFactory = createApnsServiceFactoryWithFixedCertificatesAsPool(1);
        ApnsService apnsService = apnsServiceFactory.getApnsService();
     
        doBasicAsserts(apnsService);
	}
	
	private void doBasicAsserts(Object apnsService) {
        assertNotNull(apnsService);
        assertTrue(apnsService instanceof ApnsService);
	}
    
    public static ApnsServiceFactory createApnsServiceFactoryWithFixedCertificates() {
        ApnsServiceFactory apnsServiceFactory = new ApnsServiceFactory();
        
        apnsServiceFactory.setFeedbackHost(FixedCertificates.TEST_HOST);
        apnsServiceFactory.setFeedbackPort(FixedCertificates.TEST_FEEDBACK_PORT);
        apnsServiceFactory.setGatewayHost(FixedCertificates.TEST_HOST);
        apnsServiceFactory.setGatewayPort(FixedCertificates.TEST_GATEWAY_PORT);
        apnsServiceFactory.setSslContext(FixedCertificates.clientContext());
        
        return apnsServiceFactory;
    }
	
    private ApnsServiceFactory createApnsServiceFactoryWithFixedCertificatesAsPool(int poolSize) {
        ApnsServiceFactory apnsServiceFactory = createApnsServiceFactoryWithFixedCertificates();
        apnsServiceFactory.setConnectionStrategy(ConnectionStrategy.POOL);

        apnsServiceFactory.setPoolSize(poolSize);
        
        return apnsServiceFactory;
    }

}
