package loadbalancing;

import java.util.Map;

import org.apache.camel.Exchange;

public class Request {

	private String transformation;
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
	
	@SuppressWarnings("unchecked")
	public static Request extractRequest(Exchange exchange) {
		Map<String, Object> body = exchange.getIn().getBody(Map.class);
		String transformation = (String) body.get("transformation");
		String payload = (String) body.get("payload");
		return new Request(transformation, payload);
	}
}
