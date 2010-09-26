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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.apache.camel.CamelException;
import org.apache.camel.Exchange;
import org.apache.camel.component.apns.model.ApnsConstants;
import org.apache.camel.component.apns.model.MessageType;
import org.apache.camel.component.apns.util.StringUtils;
import org.apache.camel.impl.DefaultProducer;

import com.notnoop.apns.APNS;
import com.notnoop.exceptions.ApnsException;

public class ApnsProducer extends DefaultProducer {
	
	private ApnsEndpoint endpoint;
	
	private List<String> tokenList;

	public ApnsProducer(ApnsEndpoint endpoint) {
		super(endpoint);
		this.endpoint = endpoint;
		initiate(endpoint);
	}

	private void initiate(ApnsEndpoint apnsEndpoint) {
		configureTokens(apnsEndpoint);
	}

	private void configureTokens(ApnsEndpoint apnsEndpoint) {
		if (StringUtils.isNotEmpty(apnsEndpoint.getTokens())) {
			try {
				this.tokenList = extractTokensFromString(apnsEndpoint.getTokens());
			} catch (CamelException e) {
				throw new IllegalArgumentException(e);
			}
		}
	}

	public boolean isTokensConfiguredUsingUri() {
		return tokenList != null;
	}

	public void process(Exchange exchange) throws Exception {
		notify(exchange);
	}

	private void notify(Exchange exchange) throws ApnsException, CamelException {
		String message = exchange.getIn().getBody(String.class);
		
		Collection<String> tokens = null;
		if (isTokensConfiguredUsingUri()) {
			if (hasTokensHeader(exchange)) {
				throw new CamelException("Tokens already configured on endpoint " + ApnsConstants.HEADER_TOKENS);
			}
			tokens = new ArrayList<String>(tokenList);
		}
		else {
			String tokensHeader = getHeaderTokens(exchange);
			tokens = extractTokensFromString(tokensHeader);
		}
		
		MessageType messageType = getHeaderMessageType(exchange, MessageType.STRING);
		
		String payload = null;
		if (messageType == MessageType.STRING) {
			payload = APNS.newPayload().alertBody(message).build();
		}
		else {
			payload = message;
		}
		
		endpoint.getApnsService().push(tokens, payload);
	}
	
	public String getHeaderTokens(Exchange exchange) {
		String tokens = (String)exchange.getIn().getHeader(ApnsConstants.HEADER_TOKENS);
		
		return tokens;
	}
	
	public MessageType getHeaderMessageType(Exchange exchange, MessageType defaultMessageType) {
		String messageTypeStr = (String)exchange.getIn().getHeader(ApnsConstants.HEADER_MESSAGE_TYPE);

		if (messageTypeStr == null) {
			return defaultMessageType;
		}
		
		MessageType messageType = MessageType.valueOf(messageTypeStr);
		
		return messageType;
	}

	private boolean hasTokensHeader(Exchange exchange) throws CamelException {
		return getHeaderTokens(exchange) != null;
	}
	
	private List<String> extractTokensFromString(String tokensStr) throws CamelException {
		
		tokensStr = StringUtils.trim(tokensStr);
		
		if (tokensStr.isEmpty()) {
			throw new CamelException("No token specified");
		}
		
		String[] tokenArray = tokensStr.split(";");
		
		int tokenArrayLength = tokenArray.length;
		for (int i = 0 ; i < tokenArrayLength ; i++) {
			String token = tokenArray[i];
			tokenArray[i] = token.trim();
			int tokenLength = token.length();
			if (tokenLength != 64) {
				throw new CamelException("Token has wrong size['" + tokenLength + "']: " + token);
			}
		}
		
		List<String> tokens = Arrays.asList(tokenArray);	
		
		return tokens;
	}

}
