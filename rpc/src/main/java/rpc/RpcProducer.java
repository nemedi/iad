package rpc;

import java.net.InetAddress;
import java.net.Socket;

import org.apache.camel.Endpoint;
import org.apache.camel.Exchange;
import org.apache.camel.support.DefaultProducer;

public class RpcProducer extends DefaultProducer {
	
	private String host;
	private int port;
	private String service;
	private String method;

	public RpcProducer(Endpoint endpoint) {
		super(endpoint);
		host = ((RpcEndpoint) endpoint).getHost();
		port = ((RpcEndpoint) endpoint).getPort();
		service = ((RpcEndpoint) endpoint).getService();
		method = ((RpcEndpoint) endpoint).getMethod();
	}

	@Override
	public void process(Exchange exchange) throws Exception {
		try (Socket socket = new Socket(InetAddress.getByName(host), port)) {
			RpcRequest request = new RpcRequest(service, method,
					exchange.getIn().getBody().getClass().isArray()
					? (Object[]) exchange.getIn().getBody()
					: new Object[] {exchange.getIn().getBody()});
			RpcTransport.write(request, socket);
			RpcResponse response = RpcTransport.read(socket, RpcResponse.class); 
			if (response.getFault() != null) {
				exchange.setException(new Exception(response.getFault()));
			} else {
				exchange.getIn().setBody(response.getResult());
			}
		}
	}

}
