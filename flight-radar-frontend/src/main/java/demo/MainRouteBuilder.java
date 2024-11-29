package demo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.camel.AggregationStrategy;
import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.websocket.WebsocketComponent;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jayway.jsonpath.JsonPath;

@Service
public class MainRouteBuilder extends RouteBuilder {
	
	@Autowired
	private MainConfiguration configuration;
	
	private Double cityLatitude;
	private Double cityLongitude;

	@SuppressWarnings("unchecked")
	@Override
	public void configure() throws Exception {
		var component = getContext().getComponent("websocket", WebsocketComponent.class);
		component.setPort(configuration.getFrontendPort());
		component.setStaticResources("classpath:.");
		onException(Exception.class).log("${body}");
		fromF("timer:poll?fixedRate=true&delay=0&period=%d", configuration.getInterval())
		.choice()
			.when(exchange -> cityLatitude == null || cityLongitude == null)
			.setBody(constant(""))
			.setHeader(Exchange.HTTP_METHOD, constant("GET"))
			.toF("https://nominatim.openstreetmap.org/search?city=%s&format=json", configuration.getCity())
			.convertBodyTo(String.class)
			.unmarshal().json(JsonLibrary.Jackson)
			.process(exchange -> {
				var list = exchange.getIn().getBody(List.class);
				var map = (Map<String, Object>) list.get(0);
				cityLatitude = Double.parseDouble((String) map.get("lat"));
				cityLongitude = Double.parseDouble((String) map.get("lon"));
			})
		.end()
		.setBody(constant(""))
		.setHeader(Exchange.HTTP_METHOD, constant("GET"))
		.setHeader("cityLatitude").method(getClass(), "getCityLatitude")
		.setHeader("cityLongitude").method(getClass(), "getCityLongitude")
		.setHeader("northLatitude").method(getClass(), "getNorthLatitude")
		.setHeader("southLatitude").method(getClass(), "getSouthLatitude")
		.setHeader("westLongitude").method(getClass(), "getWestLongitude")
		.setHeader("eastLongitude").method(getClass(), "getEastLongitude")
		.setHeader("backendPort").method(getClass(), "getBackendPort")
		.setHeader(Exchange.HTTP_QUERY, simple("bounds=${header.northLatitude}"
				+ ",${header.westLongitude}"
				+ ",${header.southLatitude}"
				+ ",${header.eastLongitude}"))
		.toD("http://localhost:${header.backendPort}/flights")
		.convertBodyTo(String.class)
		.unmarshal().json(JsonLibrary.Jackson)
		.setHeader("id").method(MainRouteBuilder.class, "getBatchId")
		.split().body()
		.setBody(exchange -> {
			var body = exchange.getIn().getBody(Map.class);
			var code = (String) JsonPath.read(body, "$.id");
			var number = (String) JsonPath.read(body, "$.flight");
			var latitude = (Double) JsonPath.read(body, "$.latitude");
			var longitude = (Double) JsonPath.read(body, "$.longitude");
			return new Flight(code, number, latitude, longitude);
		})
		.filter(exchange ->
			exchange.getIn().getBody(Flight.class)
				.distanceTo(cityLatitude, cityLongitude) <= configuration.getDistance()
		)
		.enrich("direct:flight", (oldExchange, newExchange) -> {
			var body = newExchange.getIn().getBody(Map.class);
			var airline = (String) body.get("flight.airline");
			var aircraft = (String) body.get("flight.aircraft");
			var from = (String) body.get("flight.from");
			var to = (String) body.get("flight.to");
			var status = (String) body.get("flight.status");
			oldExchange.getIn().getBody(Flight.class).setDetails(airline, aircraft, from, to, status);
			return oldExchange;
		})
		.resequence(simple("${body.distance}"))
		.aggregate((AggregationStrategy) (oldExchange, newExchange) -> {
			var flight = newExchange.getIn().getBody(Flight.class);
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
		.header("id")
		.completionTimeout(configuration.getInterval())
		.marshal().json(JsonLibrary.Jackson)
		.log("${body}")
		.to("websocket:flights?sendToAll=true");
		
		from("direct:flight")
		.removeHeaders("*")
		.setHeader(Exchange.HTTP_METHOD, constant("GET"))
		.setHeader("code").simple("${body.code}")
		.setBody(constant(""))
		.toD("http://localhost:9090/flights/${header.code}")
		.convertBodyTo(String.class)
		.unmarshal().json(JsonLibrary.Jackson)
		.process(exchange -> {
			var body = exchange.getIn().getBody(Map.class);
			Map<String, String> properties = new HashMap<String, String>();
			properties.put("flight.airline", "$.airline.name");
			properties.put("flight.aircraft", "$.aircraft.model.text");
			properties.put("flight.from", "$.airport.origin.name");
			properties.put("flight.to", "$.airport.destination.name");
			properties.put("flight.status", "$.status.text");
			for (var entry : properties.entrySet()) {
				String value = null;
				try {
					value = JsonPath.read(body, entry.getValue());
					entry.setValue(value);
				} catch (Throwable t) {
					entry.setValue("Unknown");
				}
				if (entry.getValue() == null) {
					entry.setValue("Unknown");
				}
			}
			exchange.getIn().setBody(properties);
		});
	}
	
	public static String getBatchId() {
		return UUID.randomUUID().toString();
	}
	
	public int getBackendPort() {
		return configuration.getBackendPort();
	}
	
	public Double getCityLatitude() {
		return cityLatitude;
	}
	
	public Double getCityLongitude() {
		return cityLongitude;
	}
	
	public Double getNorthLatitude() {
		return cityLatitude + 1;
	}

	public Double getSouthLatitude() {
		return cityLatitude - 1;
	}
	
	public Double getWestLongitude() {
		return cityLongitude - 1;
	}

	public Double getEastLongitude() {
		return cityLongitude + 1;
	}

}