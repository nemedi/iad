package com.axway.trainings.eip.camel.idempotent;

import org.apache.camel.builder.RouteBuilder;
import static org.apache.camel.support.processor.idempotent.MemoryIdempotentRepository.memoryIdempotentRepository;

public class IdempotentRouteBuilder extends RouteBuilder {

	@Override
	public void configure() throws Exception {
		String[] routes = new String[5];
		for (int i = 0; i < routes.length; i++) {
			routes[i] = "direct:route" + i;
			from(routes[i])
			.to("direct:process");
		}
		
		from("direct:start")
		.to(routes);
		
		from("direct:process")
		.idempotentConsumer(simple("${body}"), memoryIdempotentRepository(10))
		.to("stream:out");
	}

}
