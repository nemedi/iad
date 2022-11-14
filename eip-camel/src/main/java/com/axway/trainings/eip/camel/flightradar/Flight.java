package com.axway.trainings.eip.camel.flightradar;

import java.lang.reflect.Field;
import java.util.ArrayList;

public class Flight {
	
	private enum Fields {
		CODE,
		LATITUDE,
		LONGITUDE,
		TRACK,
		ALTITUDE,
		SPEED,
		STATUS,
		RADAR,
		TYPE,
		ID,
		TIMESTAMP,
		NUMBER;
		
		public static void setPayload(Flight flight, ArrayList<Object> payload) {
			for (Fields value : values()) {
				try {
					int index = value.ordinal();
					Field field = Flight.class.getDeclaredField(value.name().toLowerCase());
					boolean accessible = field.isAccessible();
					if (!accessible) {
						field.setAccessible(true);
					}
					field.set(flight, getString(payload, index));
					if (!accessible) {
						field.setAccessible(false);
					}
				} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
					continue;
				}
			}
		}
		
		private static String getString(ArrayList<Object> payload, int index) {
			if (payload == null
					|| index < 0
					|| index > payload.size()) {
				return "";
			}
			Object value = payload.get(index);
			return value != null ? value.toString() : ""; 
		}
	}

	private String code;
	private String latitude;
	private String longitude;
	private String country;
	private String timestamp;
	private String number;
	private String id;
	private String airline;

	public void setPaylod(ArrayList<Object> payload) {
		Fields.setPayload(this, payload);
	}
	
	public String getCode() {
		return code;
	}
	
	public String getLatitude() {
		return latitude;
	}
	
	public String getLongitude() {
		return longitude;
	}
	
	public String getCountry() {
		return country;
	}
	
	public void setCountry(String country) {
		this.country = country;
	}
	
	public String getTimestamp() {
		return timestamp;
	}
	
	public String getNumber() {
		return number;
	}
	
	public String getId() {
		return id;
	}
	
	public String getAirline() {
		return airline;
	}
	
	public void setAirline(String airline) {
		this.airline = airline;
	}
	
}
