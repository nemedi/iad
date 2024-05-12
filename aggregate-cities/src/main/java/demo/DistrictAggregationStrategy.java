package demo;

import org.apache.camel.AggregationStrategy;
import org.apache.camel.Exchange;

public class DistrictAggregationStrategy implements AggregationStrategy {
	
	@Override
	public Exchange aggregate(Exchange oldExchange, Exchange newExchange) {
		if (oldExchange == null) {
			City city = newExchange.getIn().getBody(City.class);
			if (city != null) {
				District district = new District(city.getDistrict());
				district.addCityInhabitants(city);
				newExchange.getIn().setBody(district);
			}
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
			return oldExchange;
		}
	}

}
