package com.axway.trainings.eip.camel.filter;

import org.apache.camel.Exchange;
import org.apache.camel.Predicate;

public class FilterPredicate implements Predicate {

	@Override
	public boolean matches(Exchange exchange) {
		return exchange.getIn().getBody(Integer.class) % 2 == 0;
	}

}
