package com.axway.trainings.eip.camel.scattegather;

import org.apache.camel.builder.RouteBuilder;

public class ScatterGatherRouteBuilder extends RouteBuilder {

	@Override
	public void configure() throws Exception {
		from("direct:start")
		.multicast()
		.to("direct:aggregate",
				"direct:firstNameEnricher",
				"direct:lastNameEnricher",
				"direct:emailEnricher");
		
		from("direct:firstNameEnricher")
		.setBody(constant("firstName=Iulian"))
		.to("direct:aggregate");
		
		from("direct:lastNameEnricher")
		.setBody(constant("lastName=Ilie-Nemedi"))
		.to("direct:aggregate");
		
		from("direct:emailEnricher")
		.setBody(constant("email=iilie@axway.com"))
		.to("direct:aggregate");
		
		from("direct:aggregate")
		.aggregate(constant(true), new CombineAggregationStrategy())
		.completionSize(4)
		.to("stream:out");

	}

}
