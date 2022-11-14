package com.axway.trainings.eip.camel.aggregation;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;

@XmlAccessorType(XmlAccessType.FIELD)
public class District {

	@XmlAttribute(name = "name")
	private String name;
	
	@XmlAttribute(name = "inhabitants")
	private int inhabitants;
	
	private District() {
	}
	
	public District(String name) {
		this();
		this.name = name;
	}
	
	public void addCityInhabitants(City city) {
		if (city != null) {
			inhabitants += city.getInhabitants();
		}
	}

	public String getName() {
		return name;
	}
	
	public int getInhabitants() {
		return inhabitants;
	}
	
	@Override
	public String toString() {
		return name;
	}
	
}
