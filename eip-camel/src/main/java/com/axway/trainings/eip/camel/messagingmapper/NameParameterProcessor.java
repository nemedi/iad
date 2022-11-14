package com.axway.trainings.eip.camel.messagingmapper;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.component.http.HttpMessage;

public class NameParameterProcessor implements Processor {
	
	private static final String NAME_HEADER = "name";

	@Override
	public void process(Exchange exchange) throws Exception {
		if (exchange == null
				|| exchange.getIn() == null
				|| !(exchange.getIn().getBody() instanceof HttpMessage)) {
			return;
		}
		HttpMessage message = exchange.getIn().getBody(HttpMessage.class);
		String name = message.getRequest().getParameter(NAME_HEADER);
		if (name != null) {
			exchange.getIn().setHeader(NAME_HEADER, name);
		}
	}
	
	public static String[] getUri(Exchange exchange) {
		String name = exchange.getIn().getHeader(NAME_HEADER, String.class);
		return new String[] {"sql:select id, name, district, inhabitants from cities where name like '" + name + "%' order by name"};
	}

}
