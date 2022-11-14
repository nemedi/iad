package com.axway.trainings.eip.camel.scattegather;

import org.apache.camel.Exchange;
import org.apache.camel.processor.aggregate.AggregationStrategy;

public class CombineAggregationStrategy implements AggregationStrategy {

	@Override
	public Exchange aggregate(Exchange oldExchange, Exchange newExchange) {
		if (oldExchange == null) {
			return newExchange;
		}
		else {
			Contact contact = oldExchange.getIn().getBody(Contact.class);
			if (contact != null) {
				String data = newExchange.getIn().getBody(String.class);
				if (data != null) {
					String[] parts = data.split("=");
					if (parts.length == 2) {
						if ("firstName".equals(parts[0].trim())) {
							contact.setFirstName(parts[1].trim());
						}
						else if ("lastName".equals(parts[0].trim())) {
							contact.setLastName(parts[1].trim());
						}
						else if ("email".equals(parts[0].trim())) {
							contact.setEmail(parts[1].trim());
						}
					}
					oldExchange.getIn().setBody(contact);
				}
			}
			return oldExchange;
		}
	}

}
