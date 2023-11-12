package demo;

import java.util.Date;
import java.util.UUID;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JsonLibrary;

public class MainRouteBuilder extends RouteBuilder {
	
	private int backendPort;
	private String serverId;
	private String frontendHost;
	private int frontendPort;

	public MainRouteBuilder(int backendPort, String serverId, String frontendHost, int frontendPort) {
		this.backendPort = backendPort;
		this.serverId = serverId;
		this.frontendHost = frontendHost;
		this.frontendPort = frontendPort;
	}

	@Override
	public void configure() throws Exception {
		fromF("mina2:tcp://0.0.0.0:%d", backendPort)
		.convertBodyTo(String.class)
		.unmarshal().json(JsonLibrary.Jackson)
		.bean(Request.class, "extractRequest")
		.setHeader("transformation").simple("${body.transformation}")
		.setBody().simple("${body.payload}")
		.log("Invoking transformation '${header.transformation}'.")
		.setHeader("serverId").constant(serverId)
		.process(exchange -> {
			exchange.getIn().setHeader("timestamp", new Date().getTime());
			exchange.getIn().setHeader("fileName", UUID.randomUUID().toString() + ".xml");
		})
		.recipientList().simple("xslt:file:./transformations/${header.transformation}.xsl")
		.process(exchange -> exchange.getIn().setHeader("timespan",
				new Date().getTime() - exchange.getIn().getHeader("timestamp", long.class)))
		.bean(Response.class, "createResponse")
		.log("Sending '${header.fileName}' to frontend.")
		.recipientList().simple(
				String.format("websocket://%s:%d/reply?fileName=${header.fileName}",
						frontendHost, frontendPort));
	}
}
