package demo;

import java.util.UUID;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JsonLibrary;

public class MainRouteBuilder extends RouteBuilder {
	
	private int port;
	private String id;

	public MainRouteBuilder(int port, String id) {
		this.port = port;
		this.id = id;
	}

	@Override
	public void configure() throws Exception {
		fromF("mina2:tcp://0.0.0.0:%d", port)
		.convertBodyTo(String.class)
		.log("Received '${body} from frontend.'")
		.process(debug())
		.setHeader("server").constant(id)
		.setHeader("begin").javaScript("new Date().getTime()")
		.delay(2000)
		.setHeader("end").javaScript("new Date().getTime()")
		.setBody().method(MainRouteBuilder.class, "getTimespan")
		.bean(Response.class, "create")
		.setHeader("fileName").method(MainRouteBuilder.class, "getFileName")
		.marshal().json(JsonLibrary.Jackson)
		.log("Sending '${body}' to frontend.")
		.process(debug())
		.recipientList().simple("file:data?fileName=${header.fileName}");
	}
	
	public static long getTimespan(Exchange exchange) {
		long begin = exchange.getIn().getHeader("begin", Long.class);
		long end = exchange.getIn().getHeader("end", Long.class);
		return end - begin;
	}
	
	public static String getFileName() {
		return UUID.randomUUID().toString();
	}
	
	private static Processor debug() {
		return exchange -> {
			System.out.println(exchange.getIn().getBody());
		};
	}

}
