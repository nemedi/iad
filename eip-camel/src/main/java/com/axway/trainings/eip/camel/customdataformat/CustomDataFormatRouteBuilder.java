package com.axway.trainings.eip.camel.customdataformat;

import org.apache.camel.builder.RouteBuilder;

public class CustomDataFormatRouteBuilder extends RouteBuilder {

	@Override
	public void configure() throws Exception {
		from("direct:start")
		.unmarshal(new ReverseStringDataFormat())
		.to("stream:out");
	}

}
