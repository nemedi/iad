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
	private long timespan;
	
	@JsonProperty
	private String payload;
	
	private Response(String serverId,
			String transformation,
			String fileName,
			long timespan,
			String payload) {
		this.serverId = serverId;
		this.timespan = timespan;
		this.transformation = transformation;
		this.fileName = fileName;
		this.payload = payload;
	}

	public String getServerId() {
		return serverId;
	}
	
	public long getTimespan() {
		return timespan;
	}
	
	public String getTransformation() {
		return transformation;
	}
	
	public String getFileName() {
		return fileName;
	}
	
	public String getPayload() {
		return payload;
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
