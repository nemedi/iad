package com.axway.trainings.eip.camel.pipe;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

public class ContentFilterProcessor implements Processor {
	
	public static final String KEYWORD_HEADER = "keyword";

	@Override
	public void process(Exchange exchange) throws Exception {
		final String keyword = exchange.getIn().getHeader(KEYWORD_HEADER, String.class).toLowerCase();
		final String lineSeparator = System.getProperty("line.separator");
		String body = exchange.getIn().getBody(String.class);
		String[] lines = body.split(lineSeparator);
		StringBuilder builder = new StringBuilder();
		for (String line : lines) {
			String[] parts = line.split(",");
			if (parts.length > 0
					&& parts[0].equalsIgnoreCase("\"" + keyword + "\"")) {
				if (builder.length() > 0) {
					builder.append(lineSeparator);
				}
				builder.append(line);
			}
		}
		exchange.getIn().setBody(builder.toString());
	}

}
