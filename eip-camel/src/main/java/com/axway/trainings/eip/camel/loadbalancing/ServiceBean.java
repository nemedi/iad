package com.axway.trainings.eip.camel.loadbalancing;

import java.text.MessageFormat;

import org.apache.camel.Exchange;

public class ServiceBean {
	
	public static final String SERVER_ID = "serverId";

	public String generate(Exchange exchange) {
		String serverId = exchange.getIn().getHeader(SERVER_ID, "", String.class);
		return MessageFormat.format("Your message was processed by {0}.", serverId);
	}
}
