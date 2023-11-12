package demo;

import org.apache.camel.Exchange;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Response {

	@JsonProperty
	private String server;
	
	@JsonProperty
	private long timespan;
	
	@JsonProperty
	private String fileName;
	
	@JsonProperty
	private String payload;
	
	
	private Response(String server, long timespan, String fileName, String payload) {
		this.server = server;
		this.timespan = timespan;
		this.fileName = fileName;
		this.payload = payload;
	}

	public String getServer() {
		return server;
	}
	
	public long getTimespan() {
		return timespan;
	}

	public static Response createResponse(Exchange exchange) {
		String server = exchange.getIn().getHeader("server", String.class);
		long timespan = exchange.getIn().getHeader("timespan", long.class);
		String fileName = exchange.getIn().getBody(String.class);
		String payload = exchange.getIn().getBody(String.class);
		return new Response(server, timespan, fileName, payload);
	}
}
