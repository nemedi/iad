package com.axway.trainings.eip.camel.flightradar;

import java.util.HashMap;
import java.util.Map;

import org.apache.camel.Exchange;
import org.apache.camel.processor.aggregate.AggregationStrategy;

public class CountAggregationStrategy implements AggregationStrategy {
	
	@SuppressWarnings("unchecked")
	@Override
	public Exchange aggregate(Exchange oldExchange, Exchange newExchange) {
		if (oldExchange == null) {
			return newExchange;
		}
		HashMap<String, Integer> map = null;
		if (oldExchange.getIn().getBody() instanceof HashMap) {
			map = oldExchange.getIn().getBody(HashMap.class);
		}
		else {
			map = new HashMap<String, Integer>();
			aggregate(map, oldExchange);
		}
		aggregate(map, newExchange);
		newExchange.getIn().setBody(map);
		return newExchange;
	}
	
	private void aggregate(Map<String, Integer> map, Exchange exchange) {
		if (!(exchange.getIn().getBody() instanceof Flight)) {
			return;
		}
		Flight flight = exchange.getIn().getBody(Flight.class);
		String country = flight.getCountry();
		if (country != null) {
			if (!map.containsKey(country)) {
				map.put(country, 1);
			}
			else {
				map.put(country, map.get(country) + 1);
			}
		}
	}
	
}
