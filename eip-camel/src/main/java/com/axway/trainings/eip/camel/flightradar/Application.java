package com.axway.trainings.eip.camel.flightradar;

import org.apache.camel.main.Main;

public class Application {

    public static void main(String... args) throws Exception {
        Main main = new Main();
        main.enableHangupSupport();
        FlightRadarRouteBuilder route = new FlightRadarRouteBuilder();
        route.setPort(8080);
        main.addRouteBuilder(route);
        main.run(args);
    }

}
