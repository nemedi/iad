package com.axway.trainings.eip.camel.throttler;

import org.apache.camel.main.Main;

public class Application {

    public static void main(String... args) throws Exception {
        Main main = new Main();
        main.configure().addRoutesBuilder(new ThrottlerRouteBuilder());
        main.run(args);
    }

}
