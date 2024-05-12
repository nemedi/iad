package com.axway.trainings.eip.camel.enveloper;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.apache.commons.codec.binary.Base64;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.Unmarshaller;

public class Envelope {

	private String from;
	private String to;
	private String payload;
	
	public Envelope(String from, String to, Contact contact) throws JAXBException, IOException {
		this.from = from;
		this.to = to;
		JAXBContext context = JAXBContext.newInstance(Contact.class);
		Marshaller marshaller = context.createMarshaller();
		try (ByteArrayOutputStream stream = new ByteArrayOutputStream()) {
			marshaller.marshal(contact, stream);
			byte[] bytes = Base64.encodeBase64(stream.toByteArray());
			this.payload = new String(bytes);
		}
	}
	
	public String getFrom() {
		return from;
	}
	
	public String getPayload() {
		return payload;
	}
	
	public String getTo() {
		return to;
	}
	
	public Contact getContact() throws JAXBException, IOException {
		JAXBContext context = JAXBContext.newInstance(Contact.class);
		Unmarshaller unmarshaller = context.createUnmarshaller();
		byte[] bytes = Base64.decodeBase64(payload.getBytes());
		try (ByteArrayInputStream stream = new ByteArrayInputStream(bytes)) {
			return (Contact) unmarshaller.unmarshal(stream);
		}
	}
	
}
