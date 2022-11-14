package com.axway.trainings.eip.camel.transform;

import org.apache.camel.builder.RouteBuilder;

public class TransformRouteBuilder extends RouteBuilder {

	@Override
	public void configure() throws Exception {
		from("direct:start")
		.transform(new CamelCaseExpression())
		.setBody(simple("Result: ${body}"))
		.to("stream:out");
	}

}
