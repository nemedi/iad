package demo;

import java.util.ArrayList;
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
		component.setPort(configuration.getPort());
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
		.to("http://data-live.flightradar24.com/zones/fcgi/feed.js")
		.convertBodyTo(String.class)
		.unmarshal().json(JsonLibrary.Jackson)
		.setHeader("id").method(MainRouteBuilder.class, "getBatchId")
		.bean(MainRouteBuilder.class, "extractFlights")
		.split().body()
		.filter(exchange -> {
			var distance = distanceBetween(cityLatitude, cityLongitude,
				exchange.getIn().getBody(Flight.class).getLatitude(),
				exchange.getIn().getBody(Flight.class).getLongitude());
			exchange.getIn().getBody(Flight.class).setDistance(distance / 1000);
			return distance <= configuration.getDistance();
		})
		.enrich("direct:flight", (oldExchange, newExchange) -> mergeFileDetails(oldExchange, newExchange))
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
		.setHeader(Exchange.HTTP_QUERY, simple("flight=${body.code}"))
		.setBody(constant(""))
		.to("http://data-live.flightradar24.com/clickhandler")
		.convertBodyTo(String.class)
		.unmarshal().json(JsonLibrary.Jackson)
		.setBody().jsonpath("$.address")
		.setBody(exchange -> {
			Map<String, Object> address = exchange.getIn().getBody(Map.class);
			return new String[] {(String) address.get("county"), (String) address.get("country")};
		});
	}
	
	public static String getBatchId() {
		return UUID.randomUUID().toString();
	}
	
	@SuppressWarnings("unchecked")
	public List<Flight> extractFlights(Map<String, Object> map) {
		final List<Flight> flights = new ArrayList<Flight>();
		for (var entry : map.entrySet()) {
			var value = entry.getValue();
			if (!(value instanceof List<?>)) {
				continue;
			}
			var values = (List<Object>) value;
			var code = entry.getKey();
			double latitude = (Double) values.get(1);
			double longitude = (Double) values.get(2);
			var number = (String) values.get(7);
			var flight = new Flight(code, number, latitude, longitude);
			flights.add(flight);
		}
		return flights;
	}
	
	@SuppressWarnings("unchecked")
	public static Exchange mergeFileDetails(Exchange oldExchange, Exchange newExchange) {
		var flight = oldExchange.getIn().getBody(Flight.class);
		var map = newExchange.getIn().getBody(Map.class);
		var airlineMap = (Map<String, Object>) map.get("airline");
		var airline = (String) airlineMap.get("name");
		flight.setAirline(airline);
		var aircraftMap = (Map<String, Object>) map.get("aircraft");
		var aircraftModelMap = (Map<String, Object>) aircraftMap.get("model");
		var aircfrat = (String) aircraftModelMap.get("text");
		flight.setAircraft(aircfrat);
		var airportMap = (Map<String, Object>) map.get("airport");
		var airportOriginMap = (Map<String, Object>) airportMap.get("origin");
		var from = (String) airportOriginMap.get("name");
		flight.setFrom(from);
		var airportDestinationMap = (Map<String, Object>) airportMap.get("destination");
		var to = (String) airportDestinationMap.get("name");
		flight.setTo(to);
		return oldExchange;
	}
	
	private static double distanceBetween(double sourceLatitude,
			double sourceLongitute, double destinationLatitude,
			double destinationLongitude) {
		double R = 6378137;
		double dLat = (destinationLatitude - sourceLatitude) * Math.PI / 180;
		double dLng = (destinationLongitude - sourceLongitute) * Math.PI / 180;
		double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
				+ Math.cos(sourceLatitude * Math.PI / 180)
				* Math.cos(destinationLatitude * Math.PI / 180)
				* Math.sin(dLng / 2) * Math.sin(dLng / 2);
		double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
		double d = R * c;
		return Math.round(d);
	}

}
