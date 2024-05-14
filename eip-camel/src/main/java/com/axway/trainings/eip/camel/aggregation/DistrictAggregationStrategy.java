package com.axway.trainings.eip.camel.aggregation;

import org.apache.camel.AggregationStrategy;
import org.apache.camel.Exchange;

public class DistrictAggregationStrategy implements AggregationStrategy {
	
	private Integer size;

	public DistrictAggregationStrategy() {
	}
	
	@Override
	public Exchange aggregate(Exchange oldExchange, Exchange newExchange) {
		System.out.println(newExchange.getIn().getBody(City.class));
		if (oldExchange == null) {
			if (size == null) {
				size = newExchange.getIn().getHeader("size", Integer.class);
			}
			City city = newExchange.getIn().getBody(City.class);
			if (city != null) {
				District district = new District(city.getDistrict());
				district.addCityInhabitants(city);
				newExchange.getIn().setBody(district);
			}
			newExchange.getIn().setHeader("size", --size);
			System.out.println(size);
			return newExchange;
		} else {
			District district = oldExchange.getIn().getBody(District.class);
			City city = newExchange.getIn().getBody(City.class);
			if (city != null) {
				if (district == null) {
					district = new District(city.getDistrict());
				}
				district.addCityInhabitants(newExchange.getIn().getBody(City.class));
				oldExchange.getIn().setBody(district);
			}
			oldExchange.getIn().setHeader("size", --size);
			System.out.println(size);
			return oldExchange;
		}
	}

}
