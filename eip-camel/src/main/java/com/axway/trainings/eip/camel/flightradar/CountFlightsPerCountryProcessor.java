package com.axway.trainings.eip.camel.flightradar;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

@SuppressWarnings("unchecked")
public class CountFlightsPerCountryProcessor implements Processor {

	@Override
	public void process(Exchange exchange) throws Exception {
		List<Flight> flights = exchange.getIn().getBody(List.class);
		Map<String, Integer> flightsPerCountry = new HashMap<String, Integer>();
		for (Flight flight : flights) {
			String country = flight.getCountry();
			if (country != null) {
				if (!flightsPerCountry.containsKey(country)) {
					flightsPerCountry.put(country, 1);
				}
				else {
					flightsPerCountry.put(country, flightsPerCountry.get(country) + 1);
				}
			}		}
		exchange.getIn().setBody(flightsPerCountry);
	}
	
}
