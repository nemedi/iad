package loadbalancing;

import org.apache.camel.Exchange;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Request {

	@JsonProperty
	private String transformation;
	
	@JsonProperty
	private String payload;
	
	private Request(String transformation, String payload) {
		this.transformation = transformation;
		this.payload = payload;
	}
	
	public String getTransformation() {
		return transformation;
	}
	
	public String getPayload() {
		return payload;
	}
	
	public static Request createRequest(Exchange exchange) {
		String transformation = exchange.getIn().getHeader("transformation", String.class);
		String payload = exchange.getIn().getBody(String.class);
		return new Request(transformation, payload);
	}
}
