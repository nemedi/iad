package engine;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Exchange {
	
	private String id;
	private Object body;
	private Map<String, Object> headers;
	private Object attachment;

	public Exchange(Object body) {
		this.id = UUID.randomUUID().toString();
		this.body = body;
		this.headers = new HashMap<String, Object>();
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
	
	public void setHeader(String name, Object value) {
		headers.put(name, value);
	}
	
	public Map<String, Object> getHeaders() {
		return headers;
	}
	
	public void setHeaders(Map<String, Object> headers) {
		this.headers = headers;
	}
	
	public Object getHeader(String name) {
		return headers.get(name);
	}
	
	public <T> T getHeader(String name, Class<T> type) {
		return type.cast(headers.get(name));
	}
	
	public void removeHeader(String name) {
		headers.remove(name);
	}
	
	public void removeHeaders() {
		headers.clear();
	}
	
	public void setAttachment(Object attachment) {
		this.attachment = attachment;
	}
	
	public Object getAttachment() {
		return attachment;
	}
	
	public <T> T getAttachment(Class<T> type) {
		return type.cast(attachment);
	}
	
	public void removeAttachment() {
		attachment = null;
	}
}
