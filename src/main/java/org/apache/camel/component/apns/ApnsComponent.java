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

import java.util.Map;

import org.apache.camel.CamelContext;
import org.apache.camel.Endpoint;
import org.apache.camel.component.apns.util.AssertUtils;
import org.apache.camel.impl.DefaultComponent;

import com.notnoop.apns.ApnsService;

/**
 * Represents the component that manages {@link ApnsEndpoint}. It holds the
 * list of named apns endpoints.
 */
public class ApnsComponent extends DefaultComponent {

	private ApnsService apnsService;

	public ApnsComponent() {
		super();
	}

	public ApnsComponent(ApnsService apnsService) {
			super();
		AssertUtils.notNull(apnsService, "apnsService is mandatory");
		this.apnsService = apnsService;
	}

	public ApnsComponent(CamelContext context) {
		super(context);
	}
	
	public ApnsService getApnsService() {
		return apnsService;
	}

	@SuppressWarnings("unchecked")
	protected Endpoint createEndpoint(String uri, String remaining, Map parameters) throws Exception {
		ApnsEndpoint endpoint = new ApnsEndpoint(uri, this);
		setProperties(endpoint, parameters);
		
		return endpoint;
	}
	
	public void setApnsService(ApnsService apnsService) {
		if (this.apnsService != null) {
			throw new IllegalArgumentException("apnsService already setted");
		}
		this.apnsService = apnsService;
	}

}
