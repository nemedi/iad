package com.axway.trainings.eip.camel.customdataformat;

import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.main.Main;

public class Application {

    public static void main(String... args) throws Exception {
        Main main = new Main();
        RouteBuilder routeBuilder = new CustomDataFormatRouteBuilder();
        main.configure().addRoutesBuilder(routeBuilder);
        main.start();
        ProducerTemplate producerTemplate = routeBuilder.getContext().createProducerTemplate();
        producerTemplate.sendBody("direct:start", "ion ara fara noi");
        main.run(args);
    }

}
