package com.axway.trainings.eip.camel.split;

import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.converter.jaxb.JaxbDataFormat;

import jakarta.xml.bind.JAXBContext;

public class SplitRouteBuilder extends RouteBuilder {

	@Override
	public void configure() throws Exception {
		from("direct:start")
		.setHeader(Exchange.HTTP_METHOD, constant("GET"))
		.setBody(constant(""))
		.to("https://www.bnr.ro/nbrfxrates.xml")
		.convertBodyTo(String.class)
		.split()
		.tokenizeXML("Rate", "")
		.unmarshal(new JaxbDataFormat(JAXBContext.newInstance(Rate.class)))
		.convertBodyTo(String.class)
		.to("stream:out");
	}

}
