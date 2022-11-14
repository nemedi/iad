package com.axway.trainings.eip.camel.flightradar;

import java.util.ArrayList;

import org.apache.camel.Converter;

@Converter
public class FlightConverter {

	@Converter
	public Flight toFlight(ArrayList<Object> item) {
		Flight flight = new Flight();
		flight.setPaylod(item);
		return flight;
	}
}
