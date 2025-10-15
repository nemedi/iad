package com.axway.trainings.eip.camel.aggregation;

import org.apache.camel.AggregationStrategy;
import org.apache.camel.Exchange;

public class DistrictAggregationStrategy implements AggregationStrategy {
	
	@Override
	public Exchange aggregate(Exchange oldExchange, Exchange newExchange) {
		City city = newExchange.getIn().getBody(City.class);
		if (oldExchange == null) {
			District district = new District(city.getDistrict());
			district.addCityInhabitants(city);
			newExchange.getIn().setBody(district);
			return newExchange;
		} else {
			District district = oldExchange.getIn().getBody(District.class);
			district.addCityInhabitants(newExchange.getIn().getBody(City.class));
			return oldExchange;
		}
	}

}
