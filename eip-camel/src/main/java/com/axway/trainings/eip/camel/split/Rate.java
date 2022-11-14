package com.axway.trainings.eip.camel.split;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlValue;

import org.apache.camel.Converter;

@Converter
@XmlRootElement(name = "Rate")
@XmlAccessorType(XmlAccessType.FIELD)
public class Rate {

	@XmlAttribute(name = "currency")
	private String currency;

	@XmlAttribute(name = "multiplier")
	private int multiplier;

	@XmlValue
	private float value;

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public int getMultiplier() {
		return multiplier;
	}

	public void setMultiplier(int multiplier) {
		this.multiplier = multiplier;
	}

	public float getValue() {
		return multiplier > 0 ? value / multiplier : value;
	}

	public void setValue(float value) {
		this.value = value;
	}
	
	@Override
	public String toString() {
		return getCurrency() + " = " + getValue() + " lei";
	}
	
	@Converter
	public static String convertToString(Rate rate) {
		return rate.toString();
	}

}
