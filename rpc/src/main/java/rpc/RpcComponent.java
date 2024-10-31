package rpc;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.camel.Endpoint;
import org.apache.camel.support.DefaultComponent;

public class RpcComponent extends DefaultComponent {
	
	private final Map<String, Map<String, Map<String, RpcConsumer>>> consumers =
			Collections.synchronizedMap(new HashMap<String, Map<String, Map<String, RpcConsumer>>>());
	private final ExecutorService executorService =
			Executors.newFixedThreadPool(10 * Runtime.getRuntime().availableProcessors());

	@Override
	protected Endpoint createEndpoint(String uri,
			String remaining,
			Map<String, Object> parameters) throws Exception {
		RpcEndpoint endpoint = new RpcEndpoint(uri, this);
        setProperties(endpoint, parameters);
        return endpoint;
	}
	
	public RpcConsumer registerConsumer(RpcConsumer consumer) throws IOException {
		boolean openSocket = false;
		RpcEndpoint endpoint = (RpcEndpoint) consumer.getEndpoint();
		String channel = endpoint.getHost() + endpoint.getPort();
		if (!consumers.containsKey(channel)) {
			openSocket = true;
			consumers.put(channel,
					Collections.synchronizedMap(new HashMap<String, Map<String, RpcConsumer>>()));
		}
		Map<String, Map<String, RpcConsumer>> channelConsumers = consumers.get(channel);
		if (!channelConsumers.containsKey(endpoint.getService())) {
			channelConsumers.put(endpoint.getService(),
					Collections.synchronizedMap(new HashMap<String, RpcConsumer>()));
		}
		channelConsumers.get(endpoint.getService())
			.put(endpoint.getMethod(), consumer);
		if (openSocket) {
			final ServerSocket serverSocket = new ServerSocket(endpoint.getPort(),
					10, InetAddress.getAllByName(endpoint.getHost())[0]);
			executorService.submit(() -> {
				while (!serverSocket.isClosed()) {
					try {
						final Socket socket = serverSocket.accept();
						executorService.submit(() -> {
							while (!socket.isClosed()) {
								try {
									if (socket.getInputStream().available() > 0) {
										RpcRequest request = RpcTransport.read(socket, RpcRequest.class);
										RpcResponse response = new RpcResponse();
										if (consumers.get(channel)
												.containsKey(request.getService())) {
											if (consumers.get(channel)
													.get(request.getService())
													.containsKey(request.getMethod())) {
												consumers.get(channel)
													.get(request.getService())
													.get(request.getMethod())
													.process(request, response);
											} else {
												response.setFault(String.format("Method '%s' is not implemented by service '%s'.",
														request.getMethod(),
														request.getService()));
											}
										} else {
											response.setFault(String.format("Service '%s' unavailabe.",
													request.getService()));
										}
										RpcTransport.write(response, socket);
									}
								} catch (Exception e) {
								}
							}
						});
					} catch (IOException e) {
					}
				}
			});
		}
		return consumer;
	}

}
