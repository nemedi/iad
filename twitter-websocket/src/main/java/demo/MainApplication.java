package demo;

import org.apache.camel.main.Main;

public class MainApplication {

	public static void main(String[] args) throws Exception {
		Main main = new Main();
		final String consumerKey = "910Sa72jIM4kO8kCcGE460tWG";
		final String consumerSecret = "ZidtI7AY9mB1V6fGctFWFQKJNhphvtt2ZrvEofDdAfvxBY6atf";
		final String accessToken = "1452383299326857220-Zbk17Ty7KSWWbw7Kkb3a9Em2S5DuL2";
		final String accessTokenSecret = "eno4lrRghjlGcrXyY2LCy09XAUCGy9QKWh290jgTlQoF0";
		final int port = 7979;
		final int delay = 5000;
		final String topic = "Ukraine";
		main.configure().addRoutesBuilder(new MainRouteBuilder(consumerKey, consumerSecret,
				accessToken, accessTokenSecret,
				port, delay,  topic));
		main.start();
		main.run();
	}

}
