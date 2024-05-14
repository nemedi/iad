package com.axway.trainings.eip.camel.enveloper;

import org.apache.camel.builder.RouteBuilder;
import static org.apache.camel.support.processor.idempotent.MemoryIdempotentRepository.memoryIdempotentRepository;

public class EnveloperRouteBuilder extends RouteBuilder {

	@Override
	public void configure() throws Exception {
		String[] routes = new String[10];
		for (int i = 0; i < routes.length; i++) {
			routes[i] = "direct:route" + i;
			from(routes[i])
			.log("Message from ${body.from} to ${body.to} with payload ${body.payload}")
			.to("direct:destination");
		}
		from("direct:start")
		.process(new WrapperProcessor())
		.multicast().parallelProcessing().to(routes);
		from("direct:destination")
		.idempotentConsumer(simple("${body.payload}"),
				memoryIdempotentRepository(100))
		.process(new UnwrapperProcessor())
		.log("Contact: ${body.firstName} ${body.lastName}")
		.end();
	}

}
