package com.axway.trainings.eip.camel.flightradar;

import java.text.MessageFormat;

import org.apache.camel.Body;

public class RecipientListBuilder {

	public String[] getGeocoderUri(@Body Flight flight) {
		return new String[] {
				MessageFormat.format("geocoder:latlng:{0},{1}",
				flight.getLatitude(), flight.getLongitude())
		};
	}
}
