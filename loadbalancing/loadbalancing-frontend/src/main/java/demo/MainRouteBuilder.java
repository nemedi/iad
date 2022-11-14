package demo;

import java.text.MessageFormat;

import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.websocket.WebsocketComponent;

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
			this.backends[i] = MessageFormat.format("mina2:tcp://{0}?sync=false", this.backends[i]);
		}
	}

	@Override
	public void configure() throws Exception {
		var component = getContext().getComponent("websocket", WebsocketComponent.class);
		component.setPort(port);
		component.setStaticResources("classpath:.");
		from("websocket:service")
		.convertBodyTo(String.class)
		.log("Received: '${body}' from frontent.")
		.process(debug())
		.loadBalance()
		.roundRobin()
		.to(backends);
		from("file:data")
		.convertBodyTo(String.class)
		.log("Received: '${body}' from backend.")
		.process(debug())
		.to("websocket:service?sendToAll=true");
	}
	
	private static Processor debug() {
		return exchange -> {
			System.out.println(exchange.getIn().getBody());
		};
	}

}
