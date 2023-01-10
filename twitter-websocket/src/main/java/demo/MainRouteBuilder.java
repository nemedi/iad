package demo;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.twitter.search.TwitterSearchComponent;
import org.apache.camel.component.websocket.WebsocketComponent;

public class MainRouteBuilder extends RouteBuilder {
	
	private String consumerKey;
	private String consumerSecret;
	private String accessToken;
	private String accessTokenSecret;
	private int port;
	private int delay;
	private String topic;

	public MainRouteBuilder(String consumerKey, String consumerSecret,
			String accessToken, String accessTokenSecret,
			int port,
			int delay,
			String topic) {
		this.consumerKey = consumerKey;
		this.consumerSecret = consumerSecret;
		this.accessToken = accessToken;
		this.accessTokenSecret = accessTokenSecret;
		this.port = port;
		this.delay = delay;
		this.topic = topic;
	}

	@Override
	public void configure() throws Exception {
		WebsocketComponent websocketComponent = getContext().getComponent("websocket", WebsocketComponent.class);
		websocketComponent.setPort(port);
		websocketComponent.setStaticResources("classpath:.");
		TwitterSearchComponent twitterSearchComponent =
				getContext().getComponent("twitter-search", TwitterSearchComponent.class);
		twitterSearchComponent.setConsumerKey(consumerKey);
		twitterSearchComponent.setConsumerSecret(consumerSecret);
		twitterSearchComponent.setAccessToken(accessToken);
		twitterSearchComponent.setAccessTokenSecret(accessTokenSecret);
		fromF("twitter-search://%s?delay=%s", topic, delay)
			.to("log:tweet")
			.to("websocket:tweet?sendToAll=true");
	}

}
