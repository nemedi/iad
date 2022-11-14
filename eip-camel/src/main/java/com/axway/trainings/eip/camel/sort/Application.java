package com.axway.trainings.eip.camel.sort;

import java.util.Random;

import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.main.Main;

public class Application {

    public static void main(String... args) throws Exception {
        Main main = new Main();
        main.enableHangupSupport();
        RouteBuilder routeBuilder = new SortRouteBuilder();
        main.addRouteBuilder(routeBuilder);
        main.start();
        ProducerTemplate producerTemplate = routeBuilder.getContext().createProducerTemplate();
        Random random = new Random();
        for (int i = 0; i < 3; i++) {
        	producerTemplate.sendBody("direct:start", random.nextInt(100));
        }
        main.run(args);
    }

}
