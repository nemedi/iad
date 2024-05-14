package com.axway.trainings.eip.camel.normalizer;

import org.apache.camel.main.Main;

public class Application {

    public static void main(String... args) throws Exception {
        Main main = new Main();
        main.configure().addRoutesBuilder(new NormalizerRouteBuilder());
        main.run(args);
    }

}
