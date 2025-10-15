package com.axway.trainings.eip.camel.resequencer;

import static org.apache.camel.support.processor.idempotent.MemoryIdempotentRepository.memoryIdempotentRepository;

import org.apache.camel.builder.RouteBuilder;

public class ResequencerRouteBuilder extends RouteBuilder {

	@Override
	public void configure() throws Exception {
		String[] routes = new String[10];
		for (int i = 0; i < routes.length; i++) {
			routes[i] = "direct:route" + i;
			from(routes[i])
			.log("${body}")
			.to("direct:resequencer")
			.end();
		}

		from("direct:start")
		.multicast()
		.parallelProcessing()
		.to(routes);
		
		from("direct:resequencer")
		.idempotentConsumer(simple("${body}"),
				memoryIdempotentRepository(100))
		.resequence(simple("${body}"))
		.to("stream:out");
	}

}
