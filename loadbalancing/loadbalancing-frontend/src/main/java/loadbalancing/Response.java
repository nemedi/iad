package loadbalancing;

import java.util.Map;

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
	
	public String getPayload() {
		return payload;
	}

	@SuppressWarnings("unchecked")
	public static Response extractResponse(Exchange exchange) {
		Map<String, Object> body = exchange.getIn().getBody(Map.class);
		String serverId = (String) body.get("serverId");
		String transformation = (String) body.get("transformation");
		String fileName = (String) body.get("fileName");
		long timespan = Long.parseLong(body.get("timespan").toString());
		String payload = (String) body.get("payload");
		return new Response(serverId, transformation, fileName, timespan, payload);
	}
}
