package loadbalancing;

import java.text.MessageFormat;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JsonLibrary;

public class MainRouteBuilder extends RouteBuilder {

	private int port;
	private String[] backends;

	public MainRouteBuilder(int port, String...backends) {
		this.port = port;
		this.backends = backends;
		for (int i = 0; i < backends.length; i++) {
			if (backends[i].startsWith("\"")) {
				backends[i] = backends[i].substring(1);
			}
			if (backends[i].endsWith("\"")) {
				backends[i] = backends[i].substring(0, backends[i].length() - 1);
			}
			this.backends[i] = MessageFormat.format("mina:tcp://{0}?sync=false", this.backends[i]);
		}
	}

	@Override
	public void configure() throws Exception {
		onException(Throwable.class)
		.log("${body}");
		
		from("file:./runtime/in")
		.convertBodyTo(String.class)
		.setHeader("transformation").method(this, "getTransformation")
		.bean(Request.class, "createRequest")
		.log("Invoking transformation '${header.transformation}'.")
		.marshal().json(JsonLibrary.Jackson)
		.convertBodyTo(String.class)
		.process(debug())
		.loadBalance()
		.roundRobin()
		.to(backends);
		
		fromF("mina:tcp://0.0.0.0:%d", port)
		.convertBodyTo(String.class)
		.process(debug())
		.unmarshal().json(JsonLibrary.Jackson)
		.bean(Response.class, "extractResponse")
		.setHeader("serverId").simple("${body.serverId}")
		.setHeader("transformation").simple("${body.transformation}")
		.setHeader("fileName").simple("${body.fileName}")
		.setHeader("timespan").simple("${body.timespan}")
		.setBody().simple("${body.payload}")
		.log("Received processed file for transformation '${header.transformation}' from backend '${header.serverId}'.")
		.toD("file:./runtime/out?fileName=${header.fileName}");
	}
	
	protected String getTransformation(Exchange exchange) {
		String fileName = exchange.getIn().getHeader("CamelFileName", String.class);
		int index = fileName.lastIndexOf('.');
		if (index > 0) {
			fileName = fileName.substring(0, index);
		}
		return fileName;
	}
	
	protected Processor debug() {
		return exchange -> {
			System.out.println(exchange.getIn());
		};
	}
}
