package com.axway.trainings.eip.camel.enveloper;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

public class WrapperProcessor implements Processor {

	@Override
	public void process(Exchange exchange) throws Exception {
		Contact contact = exchange.getIn().getBody(Contact.class);
		String from = exchange.getIn().getHeader("from", String.class);
		String to = exchange.getIn().getHeader("to", String.class);
		Envelope envelope = new Envelope(from, to, contact);
		exchange.getIn().setBody(envelope);
	}

}
