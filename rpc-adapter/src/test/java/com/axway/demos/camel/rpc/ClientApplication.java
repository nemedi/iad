package com.axway.demos.camel.rpc;

import java.util.Collection;
import java.util.stream.Collectors;

import org.apache.camel.Converter;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.main.Main;

@Converter
public class ClientApplication {
	
	@Converter
	public static String toString(Collection<String> collection) {
		return collection.stream().collect(Collectors.joining(", "));
	}

	public static void main(String[] args) throws Exception {
		Main main = new Main();
		main.configure().addRoutesBuilder(new RouteBuilder() {
			
			@Override
			public void configure() throws Exception {
				onException(Exception.class)
				.log("Error: ${body}");
				from("stream:in")
				.process(exchange -> {
					String[] data = exchange.getIn().getBody(String.class).split(":");
					exchange.getIn().setHeader("method", data[0]);
					exchange.getIn().setBody(data.length > 1 ? data[1] : "");
				})
				.toD("rpc://localhost:8080/cache#${header.method}")
				.convertBodyTo(String.class)
				.to("stream:out");
			}
		});
		main.start();
		main.run();
	}

}
