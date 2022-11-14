package com.axway.trainings.eip.camel.wiretap;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

public class WireTapProcessor implements Processor {

	@Override
	public void process(Exchange exchange) throws Exception {
		exchange.getIn().setBody("Message body was replaced.");
	}

}
