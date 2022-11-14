package com.axway.trainings.eip.camel.wiretap;

import org.apache.camel.builder.RouteBuilder;

public class WireTapRouteBuilder extends RouteBuilder {

	@Override
	public void configure() throws Exception {
		
		from("direct:start")
		.wireTap("direct:continue")
		.process(new WireTapProcessor())
		.log("${body}")
		.end();
		
		from("direct:continue")
		.to("stream:out");
	}

}
