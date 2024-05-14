package com.axway.trainings.eip.camel.flightradar;

import org.apache.camel.AggregationStrategy;
import org.apache.camel.Exchange;

public class CountryAggregationStrategy implements AggregationStrategy {
	
	@Override
	public Exchange aggregate(Exchange oldExchange, Exchange newExchange) {
		Flight flight = oldExchange.getIn().getBody(Flight.class);
		String country = newExchange.getIn().getHeader("CamelGeoCoderCountryLong", String.class);
		flight.setCountry(country);
		return oldExchange;
	}

}
