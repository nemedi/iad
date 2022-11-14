package com.axway.trainings.eip.camel.competingconsumer;

import org.apache.camel.builder.RouteBuilder;

public class CompetingConsumerRouteBuilder extends RouteBuilder {

	@Override
	public void configure() throws Exception {
		
		from("direct:start")
			.log("Message received: ${body}.")
			.to("seda:process");

		for (int i = 0; i < 5; i++) {
			from("seda:process")
				.log("Processor " + (i + 1) + " got message ${body}.")
				.to("stream:out");
		}
		
	}

}
