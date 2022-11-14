package com.axway.trainings.eip.camel.contentfilter;

public class Message {

	private static String template;
	
	public static void setTemplate(String template) {
		Message.template = template;
	}
	
	public static String getTemplate() {
		return template;
	}
}
