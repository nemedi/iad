package com.axway.trainings.eip.camel.messagingmapper;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

public class FileReaderProcessor implements Processor {

	@Override
	public void process(Exchange exchange) throws Exception {
		try (InputStream stream = FileReaderProcessor.class.getResourceAsStream("index.html")) {
			try (BufferedReader reader = new BufferedReader(new InputStreamReader(stream))) {
				StringBuilder builder = new StringBuilder();
				final String lineSeparator = System.getProperty("line.separator");
				while (true) {
					String line = reader.readLine();
					if (line == null) {
						break;
					}
					builder.append(line).append(lineSeparator);
				}
				exchange.getIn().setBody(builder.toString());
			}
		}
		
	}

}
