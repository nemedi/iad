package engine;

import java.util.UUID;

public class Exchange {
	
	private String id;
	private Object body;

	public Exchange(Object body) {
		this.id = UUID.randomUUID().toString();
		this.body = body;
	}

	public String getId() {
		return id;
	}
	
	public Object getBody() {
		return body;
	}
	
	public <T> T getBody(Class<T> type) {
		return type.cast(body);
	}
	
	public void setBody(Object body) {
		this.body = body;
	}
}
