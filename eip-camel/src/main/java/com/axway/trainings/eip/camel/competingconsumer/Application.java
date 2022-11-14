package com.axway.trainings.eip.camel.competingconsumer;

import org.apache.camel.ProducerTemplate;
import org.apache.camel.main.Main;

public class Application {

    public static void main(String... args) throws Exception {
        Main main = new Main();
        main.enableHangupSupport();
        CompetingConsumerRouteBuilder routeBuilder = new CompetingConsumerRouteBuilder();
		main.addRouteBuilder(routeBuilder);
		main.start();
        ProducerTemplate producerTemplate = routeBuilder.getContext().createProducerTemplate();
        for (int i = 0; i < 5; i++) {
        	producerTemplate.sendBody("direct:start", i + 1);
        }
        main.run(args);
    }

}
