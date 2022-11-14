package com.axway.trainings.eip.camel.delay;

import org.apache.camel.main.Main;

public class Application {

    public static void main(String... args) throws Exception {
        Main main = new Main();
        main.enableHangupSupport();
        main.addRouteBuilder(new DelayRouteBuilder());
        main.run(args);
    }

}
