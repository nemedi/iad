package com.axway.trainings.eip.camel.resequencer;

import java.util.Random;

import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.main.Main;

public class Application {

    public static void main(String... args) throws Exception {
        Main main = new Main();
        RouteBuilder routeBuilder = new ResequencerRouteBuilder();
        main.configure().addRoutesBuilder(routeBuilder);
        main.start();
        ProducerTemplate producerTemplate = routeBuilder.getContext().createProducerTemplate();
        Random random = new Random();
        for (int i = 0; i < 10; i++) {
        	producerTemplate.sendBody("direct:start", random.nextInt(10) + 1);
        }
        main.run(args);
    }

}
