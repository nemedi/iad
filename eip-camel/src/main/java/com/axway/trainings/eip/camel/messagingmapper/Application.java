package com.axway.trainings.eip.camel.messagingmapper;

import org.apache.camel.main.Main;

public class Application {

    public static void main(String... args) throws Exception {
        Main main = new Main();
        main.enableHangupSupport();
        main.addRouteBuilder(new MessagingMapperRouteBuilder());
        main.run(args);
    }

}
