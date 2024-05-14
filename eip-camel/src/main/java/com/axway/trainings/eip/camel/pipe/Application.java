package com.axway.trainings.eip.camel.pipe;

import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.main.Main;

public class Application {

    public static void main(String... args) throws Exception {
        Main main = new Main();
        RouteBuilder routeBuilder = new PipeRouteBuilder();
        main.configure().addRoutesBuilder(routeBuilder);
        main.start();
        ProducerTemplate producerTemplate = routeBuilder.getContext().createProducerTemplate();
        producerTemplate.sendBody("direct:start", "chrome.exe");
        main.run(args);
    }

}
