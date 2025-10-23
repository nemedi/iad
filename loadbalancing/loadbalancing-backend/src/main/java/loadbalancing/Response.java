package loadbalancing;

import org.apache.camel.Exchange;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Response {

	@JsonProperty
	private String serverId;
	
	@JsonProperty
	private String transformation;
	
	@JsonProperty
	private String fileName;
	
	@JsonProperty
	private String payload;
	
	@JsonProperty
	private long timespan;

	public Response(String serverId, String transformation, String fileName, long timespan, String payload) {
		this.serverId = serverId;
		this.transformation = transformation;
		this.fileName = fileName;
		this.timespan = timespan;
		this.payload = payload;
	}

	public String getServerId() {
		return serverId;
	}

	public String getTransformation() {
		return transformation;
	}

	public String getFileName() {
		return fileName;
	}

	public long getTimespan() {
		return timespan;
	}
	
	public static Response createResponse(Exchange exchange) {
		String serverId = exchange.getIn().getHeader("serverId", String.class);
		String transformation = exchange.getIn().getHeader("transformation", String.class);
		String fileName = exchange.getIn().getHeader("fileName", String.class);
		long timespan = exchange.getIn().getHeader("timespan", long.class);
		String payload = exchange.getIn().getBody(String.class);
		return new Response(serverId, transformation, fileName, timespan, payload);
	}
	
}
