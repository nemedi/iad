package demo;

import static java.util.stream.Collectors.toMap;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.camel.Exchange;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Flight {

	@JsonIgnore
	private String code;
	
	@JsonProperty
	private String number;
	
	@JsonProperty
	private String aircraft;
	
	@JsonProperty
	private String origin;
	
	@JsonProperty
	private String destination;
	
	private Flight(String code) {
		this.code = code;
	}
	
	public String getCode() {
		return code;
	}
	
	public String getNumber() {
		return number;
	}
	
	public String getAircraft() {
		return aircraft;
	}
	
	public String getOrigin() {
		return origin;
	}
	
	public String getDestination() {
		return destination;
	}
	
	public void setDetails(Map<String, Object> details) {
		var fields = Arrays.asList(getClass().getDeclaredFields())
			.stream()
			.collect(toMap(field -> field.getName(), field -> field));
		for (var entry : fields.entrySet()) {
			if (details.containsKey(entry.getKey())) {
				entry.getValue().setAccessible(true);
				try {
					entry.getValue().set(this, details.get(entry.getKey()));
				} catch (IllegalArgumentException | IllegalAccessException e) {
				}
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	public static List<Flight> extractFlights(Exchange exchange) {
		List<Flight> flights = new ArrayList<Flight>();
		Map<String, Object> body = exchange.getIn().getBody(Map.class);
		for (Entry<String, Object> entry : body.entrySet()) {
			if (entry.getValue() instanceof List) {
				flights.add(new Flight(entry.getKey()));
			}
		}
		return flights;
	}
	
	@SuppressWarnings("unchecked")
	public static Map<String, Object> extractFlightDetails(Exchange exchange) {
		Map<String, Object> details = new HashMap<String, Object>();
		Map<String, Object> body = exchange.getIn().getBody(Map.class);
		var identification = (Map<String, Object>) body.get("identification");
		var number = (Map<String, Object>) identification.get("number");
		var airctaft = (Map<String, Object>) body.get("aircraft");
		var model = (Map<String, Object>) airctaft.get("model");
		var airport = (Map<String, Object>) body.get("airport");
		var origin = (Map<String, Object>) airport.get("origin");
		var destination = (Map<String, Object>) airport.get("destination");
		details.put("number", number.get("default"));
		details.put("aircraft", model.get("text"));
		details.put("origin", origin.get("name"));
		details.put("destination", destination.get("destination"));
		return details;
	}
	
	@Override
	public String toString() {
		try {
			return new ObjectMapper().writeValueAsString(this);
		} catch (JsonProcessingException e) {
			return super.toString();
		}
	}
	
}
