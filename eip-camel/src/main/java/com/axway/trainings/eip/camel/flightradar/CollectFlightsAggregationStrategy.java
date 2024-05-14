package com.axway.trainings.eip.camel.flightradar;

import java.util.ArrayList;
import java.util.List;

import org.apache.camel.AggregationStrategy;
import org.apache.camel.Exchange;

public class CollectFlightsAggregationStrategy implements AggregationStrategy {

	private int count;
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public Exchange aggregate(Exchange oldExchange, Exchange newExchange) {
		if (oldExchange == null) {
			return newExchange;
		}
		else {
			Object body = oldExchange.getIn().getBody();
			List list = null; 
			if (body instanceof Flight) {
				list = new ArrayList<Flight>();
				list.add(body);
			}
			else if (body instanceof List) {
				list = (List) body; 
			}
			if (list != null) {
				list.add(newExchange.getIn().getBody());
				oldExchange.getIn().setBody(list);
			}
			System.out.println(++count);
			return oldExchange;
		}
	}

}
