package com.axway.trainings.eip.camel.introduction;

import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;

public class MyFirstRouteBuilder extends RouteBuilder {

	@Override
	public void configure() throws Exception {
		from("direct:start")
		.setHeader(Exchange.HTTP_METHOD, constant("GET"))
		.setBody(constant(""))
		.to("https://www.axway.com?followRedirects=true")
		.convertBodyTo(String.class)
		.process(new LinksExtractorProcessor())
		.split(body())
		.log("${body}")
		.aggregate(constant(true), new CountLinksAggregationStrategy())
		.completionSize(header("size"))
		.setBody(simple("There are ${header.count} links on Axway first page."))
		.to("stream:out");
	}

}
