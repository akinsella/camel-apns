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

import java.io.InputStream;

import javax.net.ssl.SSLContext;

import org.apache.camel.component.apns.model.ConnectionStrategy;
import org.apache.camel.component.apns.model.ReconnectionPolicy;
import org.apache.camel.component.apns.util.AssertUtils;
import org.apache.camel.component.apns.util.ParamUtils;
import org.apache.camel.component.apns.util.ResourceUtils;
import org.apache.camel.component.apns.util.StringUtils;

import com.notnoop.apns.APNS;
import com.notnoop.apns.ApnsDelegate;
import com.notnoop.apns.ApnsService;
import com.notnoop.apns.ApnsServiceBuilder;
import com.notnoop.apns.ReconnectPolicy;

public class ApnsServiceFactory {

	private static final int DEFAULT_POOL_SIZE = 15;
	
	private static final int MIN_POOL_SIZE = 1;
	private static final int MAX_POOL_SIZE = 30;

	private String certificatePath;
	
	private String certificatePassword;

	private ConnectionStrategy connectionStrategy;
	
	private ReconnectionPolicy reconnectionPolicy;

	private SSLContext sslContext;

	private int poolSize = DEFAULT_POOL_SIZE;
	
	private String gatewayHost;

	private int gatewayPort;
	
	private String feedbackHost;
	
	private int feedbackPort;
	
	private ApnsDelegate apnsDelegate;

	public ApnsServiceFactory() {
		super();
	}
	
	public String getFeedbackHost() {
		return feedbackHost;
	}

	public void setFeedbackHost(String feedbackHost) {
		this.feedbackHost = feedbackHost;
	}

	public String getGatewayHost() {
		return gatewayHost;
	}

	public void setGatewayHost(String gatewayHost) {
		this.gatewayHost = gatewayHost;
	}

	public int getGatewayPort() {
		return gatewayPort;
	}

	public void setGatewayPort(int gatewayPort) {
		this.gatewayPort = gatewayPort;
	}

	public int getFeedbackPort() {
		return feedbackPort;
	}

	public void setFeedbackPort(int feedbackPort) {
		this.feedbackPort = feedbackPort;
	}

	public int getPoolSize() {
		return poolSize;
	}

	public void setPoolSize(int poolSize) {
		this.poolSize = poolSize;
	}
	public String getCertificatePath() {
		return certificatePath;
	}

	public void setCertificatePath(String certificatePath) {
		this.certificatePath = certificatePath;
	}

	public String getCertificatePassword() {
		return certificatePassword;
	}

	public void setCertificatePassword(String certificatePassword) {
		this.certificatePassword = certificatePassword;
	}

	public ReconnectionPolicy getReconnectionPolicy() {
		return reconnectionPolicy;
	}

	public void setReconnectionPolicy(ReconnectionPolicy reconnectionPolicy) {
		this.reconnectionPolicy = reconnectionPolicy;
	}

	public ConnectionStrategy getConnectionStrategy() {
		return connectionStrategy;
	}

	public void setConnectionStrategy(ConnectionStrategy connectionStrategy) {
		this.connectionStrategy = connectionStrategy;
	}
	
	public SSLContext getSslContext() {
		return sslContext;
	}

	public void setSslContext(SSLContext sslContext) {
		this.sslContext = sslContext;
	}

	public ApnsDelegate getApnsDelegate() {
		return apnsDelegate;
	}

	public ApnsService getApnsService() {
		
		ApnsServiceBuilder builder = APNS.newService();

		configureConnectionStrategy(builder);
		configureReconnectionPolicy(builder);
		configureApnsDelegate(builder);
		configureApnsDestinations(builder);
		configureApnsCertificate(builder);
		
		ApnsService apnsService = builder.build();
		
		return apnsService;
	}

	private void configureApnsCertificate(ApnsServiceBuilder builder) {
		
		if (getSslContext() != null) {
			builder.withSSLContext(getSslContext());
			return;
		}
		
		ParamUtils.checkNotEmpty(getCertificatePath(), "certificatePath");
		ParamUtils.checkNotEmpty(getCertificatePassword(), "certificatePassword");

		InputStream certificateInputStream = null;
		try {
			certificateInputStream = ResourceUtils.getInputStream(getCertificatePath());
			builder.withCert(certificateInputStream, getCertificatePassword()).withProductionDestination();
		}
		finally {
			ResourceUtils.close(certificateInputStream);
		}
	}

	private void configureApnsDestinations(ApnsServiceBuilder builder) {
		ParamUtils.checkDestination(getGatewayHost(), getGatewayPort(), "gateway");
		ParamUtils.checkDestination(getFeedbackHost(), getFeedbackPort(), "feedback");
		
		if (StringUtils.isNotEmpty(getGatewayHost())) {
			builder.withGatewayDestination(getGatewayHost(), getGatewayPort());
		}
		
		if (StringUtils.isNotEmpty(getFeedbackHost())) {
			builder.withFeedbackDestination(getFeedbackHost(), getFeedbackPort());
		}
	}

	private void configureApnsDelegate(ApnsServiceBuilder builder) {
		if (apnsDelegate != null) {
			builder.withDelegate(apnsDelegate);
		}
	}

	private void configureConnectionStrategy(ApnsServiceBuilder builder) {
		
		if (getConnectionStrategy() == ConnectionStrategy.POOL) {
			AssertUtils.isTrue(poolSize >= MIN_POOL_SIZE, "Pool size needs to be greater than: " + MIN_POOL_SIZE);
			AssertUtils.isTrue(poolSize <= MAX_POOL_SIZE, "Pool size needs to be lower than: " + MAX_POOL_SIZE);
		}

		if (getConnectionStrategy() == null) {
			return;
		}
		
		switch(getConnectionStrategy()) {
			case NON_BLOCKING:
				builder.asNonBlocking();
				break;
			case QUEUE:
				builder.asQueued();
				break;
			case POOL:
				builder.asPool(getPoolSize());
				break;
			default: 
				break;
		}
	}

	private void configureReconnectionPolicy(ApnsServiceBuilder builder) {
		if (getReconnectionPolicy() == null) {
			return;
		}
		
		switch(getReconnectionPolicy()) {
			case EVERY_HALF_HOUR:
				builder.withReconnectPolicy(ReconnectPolicy.Provided.EVERY_HALF_HOUR);
				break;
			case EVERY_NOTIFICATION:
				builder.withReconnectPolicy(ReconnectPolicy.Provided.EVERY_NOTIFICATION);
				break;
			default:
				builder.withReconnectPolicy(ReconnectPolicy.Provided.NEVER);
				break;
		}
	}

}
