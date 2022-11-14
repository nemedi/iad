package com.axway.trainings.eip.camel.filter;

import org.apache.camel.builder.RouteBuilder;

public class FilterRouteBuilder extends RouteBuilder {

	@Override
	public void configure() throws Exception {
		from("direct:start")
		.filter(exchange -> exchange.getIn().getBody(Integer.class) %  2 == 0)
		.to("stream:out");
	}

}
