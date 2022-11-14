package demo;

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

}
