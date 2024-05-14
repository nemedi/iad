package com.axway.trainings.eip.camel.enveloper;

import java.util.HashMap;
import java.util.Map;

import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.main.Main;

public class Application {

    public static void main(String... args) throws Exception {
        Main main = new Main();
        RouteBuilder routeBuilder = new EnveloperRouteBuilder();
        main.configure().addRoutesBuilder(routeBuilder);
        main.start();
        ProducerTemplate producerTemplate = routeBuilder.getContext().createProducerTemplate();
        Contact contact = new Contact();
        contact.setFirstName("Iulian");
        contact.setLastName("Ilie-Nemedi");
        contact.setEmail("iilie@axway.com");
        Map<String, Object> headers = new HashMap<String, Object>();
        headers.put("from", "caraschip@axway.com");
        headers.put("to", "rcgrosu@axway.com");
        producerTemplate.sendBodyAndHeaders("direct:start", contact, headers);
        main.run(args);
    }

}
