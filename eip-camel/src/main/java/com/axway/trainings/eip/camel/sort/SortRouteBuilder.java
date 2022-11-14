package com.axway.trainings.eip.camel.sort;

import org.apache.camel.builder.RouteBuilder;

public class SortRouteBuilder extends RouteBuilder {

	@Override
	public void configure() throws Exception {
		from("direct:start")
		.aggregate(constant(true), new CollectAggregationStrategy())
		.completionSize(3)
		.sort(simple("${body}"))
		.split(body())
		.to("stream:out");
	}

}
