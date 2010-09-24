package org.apache.camel.component.apns.model;

import java.util.Date;

public class InactiveDevice {

	private String deviceToken;
	private Date date;
	
	public InactiveDevice(String deviceToken, Date date) {
		super();
		this.deviceToken = deviceToken;
		this.date = date;
	}

	public String getDeviceToken() {
		return deviceToken;
	}

	public void setDeviceToken(String deviceToken) {
		this.deviceToken = deviceToken;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}
	
}
