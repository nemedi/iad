package demo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.camel.Exchange;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Airline {

	@JsonProperty
	private String id;
	
	@JsonProperty
	private String name;
	
	private Airline(String id, String name) {
		this.id = id;
		this.name = name;
	}
	
	public String getId() {
		return id;
	}
	
	public String getName() {
		return name;
	}
	
	@SuppressWarnings("unchecked")
	public static List<Airline> extractAirlines(Exchange exchange) {
		List<Airline> airlines = new ArrayList<Airline>();
		Map<String, Object> body = exchange.getIn().getBody(Map.class);
		List<Map<String, Object>> results = (List<Map<String, Object>>) body.get("results");
		for (var result : results) {
			airlines.add(new Airline((String) result.get("id"), (String) result.get("label")));
		}
		return airlines;
	}
}
