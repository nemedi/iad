package com.axway.trainings.eip.camel.delay;

import org.apache.camel.Body;
import org.apache.camel.builder.RouteBuilder;

public class DelayRouteBuilder extends RouteBuilder {
	
	@Override
	public void configure() throws Exception {
		from("timer://start?fixedRate=true&period=200&delay=0")
		.log("Message created...")
		.setBody(method(System.class, "currentTimeMillis"))
		.delay(1000)
		.setBody(method(DelayRouteBuilder.class, "computeTimespan"))
		.log("Message processed: ${body}")
		.to("mock:stop")
		.end();
	}
	
	public static long computeTimespan(@Body long startTimeMillis) {
		return System.currentTimeMillis() - startTimeMillis; 
	}

}
