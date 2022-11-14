package com.axway.demos.camel.rpc;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLDecoder;

import org.apache.camel.Consumer;
import org.apache.camel.Processor;
import org.apache.camel.Producer;
import org.apache.camel.spi.UriEndpoint;
import org.apache.camel.support.DefaultEndpoint;

@UriEndpoint(
	scheme = "rpc",
	syntax = "rpc:host:port/service#method",
	title = "Remote Procedure Call")
public class RpcEndpoint extends DefaultEndpoint {

	private String host;
	
	private int port;
	
	private String service;
	
	private String method;

	public RpcEndpoint(String uri, RpcComponent component) throws URISyntaxException, UnsupportedEncodingException {
		super(uri, component);
		initialize(new URI(URLDecoder.decode(uri, "utf-8")));
	}
	
	private void initialize(URI uri) {
		host = uri.getHost();
		port = uri.getPort();
		service = uri.getRawPath().substring(1);
		method = uri.getFragment();
	}
	
	public Producer createProducer() throws Exception {
		return new RpcProducer(this);
	}

	public Consumer createConsumer(Processor processor) throws Exception {
		return ((RpcComponent) getComponent())
				.registerConsumer(new RpcConsumer(this, processor));
	}
	
	@Override
	public boolean isSingleton() {
		return true;
	}
	
	public String getHost() {
		return host;
	}
	
	public int getPort() {
		return port;
	}
	
	public String getService() {
		return service;
	}
	
	public String getMethod() {
		return method;
	}

}
