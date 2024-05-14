package com.axway.trainings.eip.camel.normalizer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.camel.Converter;
import org.apache.camel.dataformat.bindy.annotation.CsvRecord;
import org.apache.camel.dataformat.bindy.annotation.DataField;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlRootElement;

@Converter
@XmlRootElement(name = "person")
@XmlAccessorType(XmlAccessType.FIELD)
@CsvRecord(separator = ",", crlf = "WINDOWS")
public class Person {

	@XmlAttribute(name = "firstName")
	@DataField(pos = 1)
	private String firstName;

	@XmlAttribute(name = "lastName")
	@DataField(pos = 2)
	private String lastName;

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	
	@Override
	public String toString() {
		return firstName + " " + lastName;
	}
	
	@Converter
	public static String format(Person person) {
		return person.toString();
	}
	
	@Converter
	public static Person parse(Map<String, Object> map) {
		Person person = new Person();
		person.firstName = (String) map.get("firstName");
		person.lastName = (String) map.get("lastName");
		return person;
	}
	
	@SuppressWarnings("unchecked")
	@Converter
	public static List<Person> parse(List<Object> items) {
		List<Person> list = new ArrayList<Person>();
		for (Object item : items) {
			list.add(parse((Map<String, Object>) item));
		}
		return list;
	}
	
	public static final Person parse(String data) {
		Person person = new Person();
		return person;
	}

}
