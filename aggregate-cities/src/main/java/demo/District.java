package demo;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAttribute;

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
