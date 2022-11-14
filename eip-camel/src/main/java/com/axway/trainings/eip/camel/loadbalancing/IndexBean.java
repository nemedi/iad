package com.axway.trainings.eip.camel.loadbalancing;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class IndexBean {

	public String generate() throws IOException {
		try (InputStream stream = IndexBean.class.getResourceAsStream("index.html")) {
			StringBuilder builder = new StringBuilder();
			try (BufferedReader reader = new BufferedReader(new InputStreamReader(stream))) {
				final String lineSeparator = System.getProperty("line.separator");
				while (true) {
					String line = reader.readLine();
					if (line == null) {
						break;
					}
					builder.append(line).append(lineSeparator);
				}
			}
			return builder.toString();
		}
		
	}
}
