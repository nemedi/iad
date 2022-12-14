package demo;

import java.io.Serializable;

import org.apache.camel.Exchange;

public class Response implements Serializable {

	private static final long serialVersionUID = 1L;
	private String server;
	private long timespan;
	
	
	private Response(String server, long timespan) {
		this.server = server;
		this.timespan = timespan;
	}

	public String getServer() {
		return server;
	}
	
	public long getTimespan() {
		return timespan;
	}

	public static Response create(Exchange exchange, long timespan) {
		String server = exchange.getIn().getHeader("server", String.class);
		return new Response(server, timespan);
	}
}
