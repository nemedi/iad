package demo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JsonLibrary;

public class MainRouteBuilder extends RouteBuilder {
	
	private int port;
	private String folder;
	private String backend;

	public MainRouteBuilder(int port, String folder, String backend) {
		this.port = port;
		this.folder = folder;
		this.backend = backend;
		if (this.backend.endsWith("/")) {
			this.backend = this.backend.substring(0, this.backend.length() - 1);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public void configure() throws Exception {
		fromF("jetty:http://0.0.0.0:%d/?matchOnUriPrefix=true", port)
		.process(new StaticProcessor(folder));
		restConfiguration()
			.host("0.0.0.0")
			.port(port)
			.component("jetty")
			.contextPath("/api");
		rest("/airlines")
			.produces("application/json")
			.get("/{airline}")
			.to("direct:searchAirline")
			.get("/{airline}/flights")
			.to("direct:searchFlights");
		rest("/flights")
			.produces("application/json")
			.get("/{airline}")
			.to("direct:searchFlights");
		from("direct:searchAirline")
			.removeHeaders("CamelHttp*")
			.setHeader("Accept").constant("application/json")
			.setHeader("Accept-Encoding").constant("deflate")
			.setBody(constant(""))
			.setHeader(Exchange.HTTP_QUERY).simple("bridgeEndpoint=true&name=${header.airline}")
			.to(backend + "/airlines")
			.convertBodyTo(String.class)
			.unmarshal().json(JsonLibrary.Jackson)
			.bean(Airline.class, "extractAirlines")
			.marshal().json(JsonLibrary.Jackson);
		from("direct:searchFlights")
			.removeHeaders("CamelHttp*")
			.setHeader("Accept").constant("application/json")
			.setHeader("Accept-Encoding").constant("deflate")
			.setHeader(Exchange.HTTP_QUERY).simple("bridgeEndpoint=true&airline=${header.airline}")
			.setBody(constant(""))
			.to(backend + "/flights")
			.convertBodyTo(String.class)
			.unmarshal().json(JsonLibrary.Jackson)
			.bean(Flight.class, "extractFlights")
			.setHeader("size").simple("${body.size}")
			.split().body()
			.enrich("direct:searchFlightDetails", (oldExchange, newExchange) -> {
				oldExchange.getIn()
					.getBody(Flight.class)
					.setDetails((Map<String, Object>) newExchange.getIn().getBody(Map.class));
				return oldExchange;
			})
			.aggregate(constant(true), (oldExchange, newExchange) -> {
				Flight flight = newExchange.getIn().getBody(Flight.class);
				if (oldExchange == null) {
					List<Flight> flights = new ArrayList<Flight>();
					flights.add(flight);
					newExchange.getIn().setBody(flights);
					return newExchange;
				} else {
					List<Flight> flights = oldExchange.getIn().getBody(List.class);
					flights.add(flight);
					return oldExchange;
				}
			})
			.completionSize(simple("${header.size}"))
			.marshal().json(JsonLibrary.Jackson);
		from("direct:searchFlightDetails")
			.removeHeaders("CamelHttp*")
			.setHeader("Accept").constant("application/json")
			.setHeader("Accept-Encoding").constant("deflate")
			.setHeader(Exchange.HTTP_QUERY).simple("bridgeEndpoint=true&code=${body.code}")
			.setBody(constant(""))
			.to(backend + "/flight")
			.convertBodyTo(String.class)
			.unmarshal().json(JsonLibrary.Jackson)
			.bean(Flight.class, "extractFlightDetails");
	}

}
