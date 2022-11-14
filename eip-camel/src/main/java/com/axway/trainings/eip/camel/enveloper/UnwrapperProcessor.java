package com.axway.trainings.eip.camel.enveloper;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

public class UnwrapperProcessor implements Processor {

	@Override
	public void process(Exchange exchange) throws Exception {
		Envelope envelope = exchange.getIn().getBody(Envelope.class);
		Contact contact = envelope.getContact();
		exchange.getIn().setBody(contact);
	}

}
