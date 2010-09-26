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
