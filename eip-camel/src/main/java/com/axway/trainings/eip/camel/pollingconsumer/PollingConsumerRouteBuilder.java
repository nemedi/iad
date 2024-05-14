package com.axway.trainings.eip.camel.pollingconsumer;

import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.twitter.directmessage.TwitterDirectMessageComponent;
import org.apache.camel.component.websocket.WebsocketComponent;

public class PollingConsumerRouteBuilder extends RouteBuilder {

	private int port;
	private String consumerKey;
	private String consumerSecret;
	private String accessToken;
	private String accessTokenSecret;
	private int delay;
	private String keywords;

	public PollingConsumerRouteBuilder(int port,
			String consumerKey,
			String consumerSecret,
			String accessToken,
			String accessTokenSecret,
			int delay,
			String keywords) {
		this.port = port;
		this.consumerKey = consumerKey;
		this.consumerSecret = consumerSecret;
		this.accessToken = accessToken;
		this.accessTokenSecret = accessTokenSecret;
		this.delay = delay;
		this.keywords = keywords;
	}

	@Override
	public void configure() throws Exception {
		CamelContext context = getContext();
		WebsocketComponent websocketComponent = context.getComponent("websocket", WebsocketComponent.class);
		websocketComponent.setPort(port);
		websocketComponent.setStaticResources("classpath:.");
		TwitterDirectMessageComponent twitterComponent = context.getComponent("twitter", TwitterDirectMessageComponent.class);
		twitterComponent.setConsumerKey(consumerKey);
		twitterComponent.setConsumerSecret(consumerSecret);
		twitterComponent.setAccessToken(accessToken);
		twitterComponent.setAccessTokenSecret(accessTokenSecret);
		
		fromF("twitter://search?type=polling&delay=%s&keywords=%s", delay, keywords)
		.log("${body}")
		.to("websocket:twitter?sendToAll=true");
	}

}
