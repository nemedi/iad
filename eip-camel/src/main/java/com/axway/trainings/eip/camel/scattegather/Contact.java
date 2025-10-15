package com.axway.trainings.eip.camel.scattegather;

import java.text.MessageFormat;

public class Contact {

	private String firstName;
	private String lastName;
	private String email;

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

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	@Override
	public String toString() {
		return MessageFormat.format("{0} {1} <{2}>",
				firstName,
				lastName,
				email);
	}
	
}
