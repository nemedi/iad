package com.axway.trainings.eip.camel.pipe;

import org.apache.camel.builder.RouteBuilder;

public class PipeRouteBuilder extends RouteBuilder {

	@Override
	public void configure() throws Exception {
		
		from("direct:start")
			.setHeader(ContentFilterProcessor.KEYWORD_HEADER, body())
			.pipeline("direct:listTasks",
				"direct:filterContent",
				"direct:displayResults")
		.end();
		
		from("direct:listTasks")
			.to("exec:tasklist?args=/fo csv");
		
		from("direct:filterContent")
			.process(new ContentFilterProcessor());

		from("direct:displayResults")
			.log("${body}");
		
	}

}
