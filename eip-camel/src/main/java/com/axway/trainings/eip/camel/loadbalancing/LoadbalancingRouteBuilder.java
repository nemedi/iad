package com.axway.trainings.eip.camel.loadbalancing;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.camel.builder.RouteBuilder;

public class LoadbalancingRouteBuilder extends RouteBuilder {

	private int port;
	private int[] backendPorts;
	
	public LoadbalancingRouteBuilder(int port, int...backendPorts) {
		this.port = port;
		this.backendPorts = backendPorts;
	}
	
	@Override
	public void configure() throws Exception {
		List<String> uris = new ArrayList<String>();
		for (int backendPort : backendPorts) {
			String uri = MessageFormat.format("mina2:tcp://localhost:{0}?sync=true",
					Integer.toString(backendPort));
			uris.add(uri);
			from(uri)
			.setHeader(ServiceBean.SERVER_ID, constant("MinaServer:" + backendPort))
			.bean(ServiceBean.class, "generate");
		}
		from("netty-http:http://localhost:" + port + "/index.html")
		.bean(IndexBean.class, "generate");
		from("netty-http:http://localhost:" + port + "/service")
		.convertBodyTo(String.class)
		.loadBalance()
		.roundRobin()
		.to(uris.toArray(new String[uris.size()]));
	}

}
