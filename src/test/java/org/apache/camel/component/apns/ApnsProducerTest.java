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
package org.apache.camel.component.apns;

import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.apns.factory.ApnsServiceFactory;
import org.apache.camel.component.apns.model.ApnsConstants;
import org.apache.camel.component.apns.stub.ApnsServerStub;
import org.apache.camel.component.apns.util.ApnsUtils;
import org.apache.camel.component.apns.util.FixedCertificates;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.notnoop.apns.APNS;
import com.notnoop.apns.ApnsNotification;
import com.notnoop.apns.ApnsService;

/**
 * Unit test that we can produce JMS message from files
 */
public class ApnsProducerTest extends CamelTestSupport {

    private ApnsServerStub server;
    private String FAKE_TOKEN = "19308314834701ACD8313AEBD92AEFDE192120371FE13982392831701318B943";
 
    public ApnsProducerTest() {
    	super();
    }

    @Before
    public void startup() {
        server = ApnsServerStub.prepareAndStartServer(FixedCertificates.TEST_GATEWAY_PORT, FixedCertificates.TEST_FEEDBACK_PORT);
    }
    
    @After
    public void stop() {
        server.stop();
    }
    
    @Test(timeout=2000)
    public void testProducer() throws Exception {
    	String message = "Hello World";
    	String messagePayload = APNS.newPayload().alertBody(message).build();
    	
        ApnsNotification apnsNotification = new ApnsNotification(FAKE_TOKEN, messagePayload);
        server.stopAt(apnsNotification.length());

        template.sendBody("direct:test", message);
        
        server.messages.acquire();
        assertArrayEquals(apnsNotification.marshall(), server.received.toByteArray());
    }

    protected CamelContext createCamelContext() throws Exception {
        CamelContext camelContext = super.createCamelContext();
        
        ApnsServiceFactory apnsServiceFactory = ApnsUtils.createDefaultTestConfiguration();
        ApnsService apnsService = apnsServiceFactory.getApnsService();

        ApnsComponent apnsComponent = new ApnsComponent(apnsService);
        camelContext.addComponent("apns", apnsComponent);

        return camelContext;
    }

    protected RouteBuilder createRouteBuilder() throws Exception {
        return new RouteBuilder() {
            public void configure() throws Exception {
                from("direct:test").
                setHeader(ApnsConstants.HEADER_TOKENS, constant(FAKE_TOKEN)).
                to("apns:notify");
            }
        };
    }
}
