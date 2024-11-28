package demo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Flight {

	private String code;
	private String number;
	private double latitude;
	private double longitude;
	private String airline;
	private String aircraft;
	private String from;
	private String to;
	private double distance;

	private Flight(String code, String number, double latitude, double longitude) {
		this.code = code;
		this.number = number;
		this.latitude = latitude;
		this.longitude = longitude;
	}

	public String getCode() {
		return code;
	}

	public String getNumber() {
		return number;
	}

	public double getLatitude() {
		return latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public String getAirline() {
		return airline;
	}

	public void setAirline(String airline) {
		this.airline = airline;
	}

	public String getAircraft() {
		return aircraft;
	}

	public void setAircraft(String aircraft) {
		this.aircraft = aircraft;
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}
	
	public double getDistance() {
		return distance;
	}
	
	public void setDistance(double distance) {
		this.distance = distance;
	}
	
	public static List<Flight> extractFlights(List<Map<String, Object>> body) {
		final List<Flight> flights = new ArrayList<Flight>();
		for (var item : body) {
			var code = (String) item.get("id"); 
			var latitude = (Double) item.get("latitude");
			var longitude = (Double) item.get("longitude");
			var number = (String) item.get("flight");
			var flight = new Flight(code, number, latitude, longitude);
			flights.add(flight);
		}
		return flights;
	}

	public double distanceTo(double latitude, double longitude) {
		double R = 6378137;
		double dLat = (latitude - this.latitude) * Math.PI / 180;
		double dLng = (longitude - this.longitude) * Math.PI / 180;
		double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
				+ Math.cos(this.latitude * Math.PI / 180)
				* Math.cos(latitude * Math.PI / 180)
				* Math.sin(dLng / 2) * Math.sin(dLng / 2);
		double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
		double d = R * c;
		return Math.round(d);
	}
}