package demo;

import java.util.UUID;

import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;

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
		.log("Invoking transformation '${header.transformation}'.")
		.process(debug())
		.setHeader("serverId").constant(serverId)
		.recipientList().simple("xslt:file:./transformations/${header.transformation}.xsl")
		.setHeader("fileName").method(MainRouteBuilder.class, "getFileName")
		.log("Sending '${header.fileName}' to frontend.")
		.process(debug())
		.recipientList().simple(
				String.format("websocket://%s:%d/reply?fileName=${header.fileName}",
						frontendHost, frontendPort));
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
