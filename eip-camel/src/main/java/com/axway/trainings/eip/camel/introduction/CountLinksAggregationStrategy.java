package com.axway.trainings.eip.camel.introduction;

import org.apache.camel.AggregationStrategy;
import org.apache.camel.Exchange;

public class CountLinksAggregationStrategy implements AggregationStrategy {

	@Override
	public Exchange aggregate(Exchange oldExchange, Exchange newExchange) {
		if (oldExchange == null) {
			newExchange.getIn().setHeader("count", 0);
			return newExchange;
		}
		int count = oldExchange.getIn().getHeader("count", Integer.class);
		oldExchange.getIn().setHeader("count", count + 1);
		return oldExchange;
	}

}
