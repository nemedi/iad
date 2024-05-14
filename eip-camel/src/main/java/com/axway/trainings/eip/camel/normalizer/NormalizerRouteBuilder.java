package com.axway.trainings.eip.camel.normalizer;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.BindyType;
import org.apache.camel.model.dataformat.JaxbDataFormat;
import org.apache.camel.model.dataformat.JsonLibrary;

import jakarta.xml.bind.JAXBContext;

public class NormalizerRouteBuilder extends RouteBuilder {

	@Override
	public void configure() throws Exception {
		final String packageName = NormalizerRouteBuilder.class.getPackage().getName();
		from("file:src/test/resources/" + packageName + "?noop=true")
		.convertBodyTo(String.class)
		.choice()
			.when(header("CamelFileName").endsWith(".csv"))
				.unmarshal().bindy(BindyType.Csv, Person.class)
				.to("direct:process")
			.when(header("CamelFileName").endsWith(".json"))
				.unmarshal().json(JsonLibrary.Jackson)
				.convertBodyTo(Person.class)
				.to("direct:process")
			.when(header("CamelFileName").endsWith(".xml"))
				.unmarshal(new JaxbDataFormat(JAXBContext.newInstance(Person.class)))
				.to("direct:process")
			.otherwise()
				.bean(Person.class, "parse")
		.endChoice();
		
		from("direct:process")
		.convertBodyTo(String.class)
		.to("stream:out");
			
	}

}
