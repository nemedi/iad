package demo;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.springframework.stereotype.Service;

@Service
public class MainRouteBuilder extends RouteBuilder {
	
	@Override
	public void configure() throws Exception {
		restConfiguration()
			.host("localhost")
			.port(9090)
			.component("jetty")
			.contextPath("/api");
		rest("/airlines")
			.produces("application/json")
			.get("/{airline}")
			.to("direct:searchAirline");
		from("direct:searchAirline")
			.removeHeaders("CamelHttp*")
			.setBody(constant(""))
			.toD("https://www.flightradar24.com/v1/search/web/find?bridgeEndpoint=true&type=operator&query=${header.airline}")
			.convertBodyTo(String.class)
			.process(exchange -> {
				Object body = exchange.getIn().getBody();
				System.out.println(body);
			})			
			.unmarshal().json(JsonLibrary.Jackson);
	}

}
