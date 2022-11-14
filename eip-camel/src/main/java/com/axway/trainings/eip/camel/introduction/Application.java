package com.axway.trainings.eip.camel.introduction;

import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.main.Main;

public class Application {

    public static void main(String... args) throws Exception {
        Main main = new Main();
        main.enableHangupSupport();
        RouteBuilder routeBuilder = new MyFirstRouteBuilder();
        main.addRouteBuilder(routeBuilder);
        main.start();
        ProducerTemplate producerTemplate = routeBuilder.getContext().createProducerTemplate();
        producerTemplate.sendBody("direct:start", "run");
        main.run(args);
    }

}
