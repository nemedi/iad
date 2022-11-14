package com.axway.trainings.eip.camel.aggregation;

import java.text.MessageFormat;

import org.apache.camel.dataformat.bindy.annotation.CsvRecord;
import org.apache.camel.dataformat.bindy.annotation.DataField;

@CsvRecord(separator = ",", crlf = "WINDOWS")
public class City {

	@DataField(pos = 1)
	private int id;
	
	@DataField(pos = 2)
	private String name;
	
	@DataField(pos = 3)
	private String district;
	
	@DataField(pos = 4)
	private int inhabitants;
	
	public int getId() {
		return id;
	}
	
	public String getName() {
		return name;
	}
	
	public String getDistrict() {
		return district;
	}
	
	public int getInhabitants() {
		return inhabitants;
	}

	@Override
	public String toString() {
		return MessageFormat.format("Name: {0}, District: {1}", name, district);
	}
	
}
