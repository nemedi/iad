package com.axway.trainings.eip.camel.aggregation;

import org.apache.camel.main.Main;

public class AggregationApplication {

    public static void main(String... args) throws Exception {
        Main main = new Main();
        main.enableHangupSupport();
        main.addRouteBuilder(new AggregationRouteBuilder());
        main.run(args);
    }

}
