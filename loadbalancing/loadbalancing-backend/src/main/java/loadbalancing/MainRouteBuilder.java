package loadbalancing;

import java.util.Date;
import java.util.UUID;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JsonLibrary;

public class MainRouteBuilder extends RouteBuilder {
	
	private int backendPort;
	private String serverId;
	private int frontendPort;

	public MainRouteBuilder(int backendPort, String serverId, int frontendPort) {
		this.backendPort = backendPort;
		this.serverId = serverId;
		this.frontendPort = frontendPort;
	}

	@Override
	public void configure() throws Exception {
		onException(Throwable.class)
		.log("${body}");
		
		fromF("mina:tcp://0.0.0.0:%d", backendPort)
		.convertBodyTo(String.class)
		.process(debug())
		.unmarshal().json(JsonLibrary.Jackson)
		.bean(Request.class, "extractRequest")
		.setHeader("transformation").simple("${body.transformation}")
		.setBody().simple("${body.payload}")
		.log("Invoking transformation '${header.transformation}'.")
		.setHeader("serverId").constant(serverId)
		.setHeader("timestamp").method(this, "getTimestamp")
		.recipientList().simple("xslt:file:./runtime/${header.transformation}.xsl")
		.setHeader("timespan").method(this, "getTimespan")
		.setHeader("fileName").method(this, "getFileName")
		.removeHeader("timestamp")
		.bean(Response.class, "createResponse")
		.removeHeader("transformation")
		.setHeader("remoteEndpoint").method(this, "getRemoteEndpoint")
		.log("Sending '${header.fileName}' to frontend.")
		.marshal().json(JsonLibrary.Jackson)
		.convertBodyTo(String.class)
		.process(debug())
		.toD("mina:tcp://${header.remoteEndpoint}");
	}
	
	protected long getTimestamp() {
		return new Date().getTime();
	}
	
	protected long getTimespan(Exchange exchange) {
		long timestamp = exchange.getIn().getHeader("timestamp", long.class);
		return new Date().getTime() - timestamp;
	}
	
	protected String getFileName() {
		return UUID.randomUUID().toString() + ".xml";
	}
	
	protected String getRemoteEndpoint(Exchange exchange) {
		String remoteEndpoint = exchange.getIn().getHeader("CamelMinaRemoteAddress", String.class);
		if (remoteEndpoint.startsWith("/")) {
			remoteEndpoint = remoteEndpoint.substring(1);
		}
		if (remoteEndpoint.contains(":")) {
			remoteEndpoint = remoteEndpoint.substring(0, remoteEndpoint.indexOf(':'));
		}
		remoteEndpoint += ":" + Integer.toString(frontendPort);
		return remoteEndpoint;
	}
	
	protected Processor debug() {
		return exchange -> {
			System.out.println(exchange.getIn());
		};
	}
}
