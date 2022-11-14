package com.axway.trainings.eip.camel.aggregation;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "districts")
@XmlAccessorType(XmlAccessType.FIELD)
public class DistrictCollection {

	@XmlElement(name = "district")
	private List<District> districts;
	
	public DistrictCollection() {
		districts = new ArrayList<District>();
	}
	
	public List<District> getDistricts() {
		return districts;
	}
	
	public void collect(District district) {
		districts.add(district);
	}
}
