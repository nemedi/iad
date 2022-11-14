package com.axway.trainings.eip.camel.contextrouting;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.ChoiceDefinition;

import com.axway.trainings.eip.camel.aggregation.TestFileResourceUtils;

public class ContextBasedRouteBuilder extends RouteBuilder {

	private String[] types;

	public ContextBasedRouteBuilder(String...types) {
		this.types = types;
	}
	
	@Override
	public void configure() throws Exception {
		final String inUri = TestFileResourceUtils.getFileBaseUti(ContextBasedRouteBuilder.class) + "/in"; 
		final String outBaseUri = TestFileResourceUtils.getFileBaseUti(ContextBasedRouteBuilder.class) + "/out/";
		ChoiceDefinition choice = from(inUri).choice();
		for (String type : types) {
			choice = choice.when(header("CamelFileName").endsWith(type)).to(outBaseUri + type);
		}
		choice.otherwise().to(outBaseUri + "others");
	}

}
