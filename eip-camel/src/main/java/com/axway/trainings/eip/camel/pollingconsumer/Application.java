package com.axway.trainings.eip.camel.pollingconsumer;

import org.apache.camel.main.Main;

public class Application {

    public static void main(String... args) throws Exception {
        Main main = new Main();
        main.enableHangupSupport();
        int port = 8080;
		String consumerKey = "67WOyGmRPtCOAaLWqb6EoGnSH";
		String consumerSecret = "mREHeX5bfJ0XBtzTLl0ZousjtqWdmUfcUY3Ob29FqGC1FEtB5W";
		String accessToken = "1586003647-fLooSj6gOtiyI9fykmLWkR9GjTh2YTinrqvdeIT";
		String accessTokenSecret = "XBglJOdZI2UAnyjQYup4Se80UJTpmQqEStKRv5BRfGkwz";
		int delay = 5;
		String keywords = "Hungary";
        main.addRouteBuilder(new PollingConsumerRouteBuilder(port,
        		consumerKey, consumerSecret, accessToken, accessTokenSecret, delay, keywords));
        main.run(args);
    }

}
