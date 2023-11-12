package loadbalancing;

import java.util.Date;
import java.util.UUID;

import org.apache.camel.Exchange;
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
		.setHeader("timestamp").method(getClass(), "getTimestamp")
		.recipientList().simple("xslt:file:./runtime/${header.transformation}.xsl")
		.setHeader("timespan").method(getClass(), "getTimespan")
		.setHeader("fileName").method(getClass(), "getFileName")
		.removeHeader("timestamp")
		.bean(Response.class, "createResponse")
		.removeHeader("transformation")
		.log("Sending '${header.fileName}' to frontend.")
		.toF("websocket://%s:%d/reply", frontendHost, frontendPort);
	}
	
	public static long getTimestamp() {
		return new Date().getTime();
	}
	
	public static long getTimespan(Exchange exchange) {
		long timestamp = exchange.getIn().getHeader("timestamp", long.class);
		return new Date().getTime() - timestamp;
	}
	
	public static String getFileName() {
		return UUID.randomUUID().toString() + ".xml";
	}
}
