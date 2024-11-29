package demo;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class Flight {

	@JsonIgnore
	private String code;
	
	private String number;
	private double latitude;
	private double longitude;
	private String airline;
	private String aircraft;
	private String from;
	private String to;
	private String status;
	
	@JsonIgnore
	private double distance;

	public Flight(String code, String number, double latitude, double longitude) {
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

	public String getAircraft() {
		return aircraft;
	}

	public String getFrom() {
		return from;
	}

	public String getTo() {
		return to;
	}
	
	public String getStatus() {
		return status;
	}
	
	public double getDistance() {
		return distance;
	}
	
	public void setDistance(double distance) {
		this.distance = distance;
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
		this.distance = Math.round(d / 1000);
		return distance;
	}
	
	public void setDetails(String airline, String aircraft, String from, String to, String status) {
		this.airline = airline;
		this.aircraft = aircraft;
		this.from = from;
		this.to = to;
		this.status = status;
	}
}