package com.axway.trainings.eip.camel.flightradar;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.websocket.WebsocketComponent;
import org.apache.camel.model.dataformat.JsonLibrary;

public class FlightRadarRouteBuilder extends RouteBuilder {
	
	private int port;
	
	public void setPort(int port) {
		this.port = port;
	}
	
	public void configure() {
		
    	WebsocketComponent websocketComponent = getContext().getComponent("websocket", WebsocketComponent.class);
    	websocketComponent.setStaticResources("classpath:.");
    	websocketComponent.setPort(this.port);
    	
		from("timer://pollingTimer?fixedRate=true&delay=0&period=5000")
			.to("http://www.flightradar24.com/zones/europe_all.js")
			.convertBodyTo(String.class)
			.process(new TrimProcessor())
			.unmarshal()
			.json(JsonLibrary.Jackson)
			.split()
			.method(FlightSplitter.class)
			.convertBodyTo(Flight.class)
			.enrich("direct:countryEnricher", new CountryAggregationStrategy())
			.aggregate(constant(true), new CollectFlightsAggregationStrategy())
			//.completionSize(header("size"))
			.completionTimeout(3000)
			.process(new CountFlightsPerCountryProcessor())
			.marshal()
			.json(JsonLibrary.Jackson)
			.convertBodyTo(String.class)
			.to("websocket:flights?sendToAll=true");
		
		from("direct:countryEnricher")
			.recipientList().method(RecipientListBuilder.class, "getGeocoderUri")
			.setBody(header("CamelGeoCoderCountryLong"));
		
	}
     
}
