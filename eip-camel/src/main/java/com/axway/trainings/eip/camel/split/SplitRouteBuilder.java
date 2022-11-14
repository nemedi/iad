package com.axway.trainings.eip.camel.split;

import javax.xml.bind.JAXBContext;

import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.converter.jaxb.JaxbDataFormat;

public class SplitRouteBuilder extends RouteBuilder {

	@Override
	public void configure() throws Exception {
		from("direct:start")
		.setHeader(Exchange.HTTP_METHOD, constant("GET"))
		.setBody(constant(""))
		.to("http://www.bnro.ro/nbrfxrates.xml")
		.split()
		.tokenizeXML("Rate", "")
		.unmarshal(new JaxbDataFormat(JAXBContext.newInstance(Rate.class)))
		.convertBodyTo(String.class)
		.to("stream:out");
	}

}
