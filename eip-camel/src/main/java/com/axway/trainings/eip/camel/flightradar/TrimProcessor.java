package com.axway.trainings.eip.camel.flightradar;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

public class TrimProcessor implements Processor {

	@Override
	public void process(Exchange exchange) throws Exception {
		String content = exchange.getIn().getBody(String.class);
		if (content == null) {
			return;
		}
		int startIndex = content.indexOf("(");
		int endIndex = content.lastIndexOf(")");
		if (startIndex > -1 && startIndex < endIndex) {
			content = content.substring(startIndex + 1, endIndex);
			exchange.getIn().setBody(content);
		}
	}

}
