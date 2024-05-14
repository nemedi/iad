package com.axway.trainings.eip.camel.loadbalancing;

import org.apache.camel.main.Main;

public class Application {

    public static void main(String... args) throws Exception {
        Main main = new Main();
        main.configure().addRoutesBuilder(new LoadbalancingRouteBuilder(8080, 9091, 9092, 9093));
        main.run(args);
    }

}
