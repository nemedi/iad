package com.axway.trainings.eip.camel.contentfilter;

import org.apache.camel.main.Main;

public class Application {

    public static void main(String... args) throws Exception {
        Main main = new Main();
        main.configure().addRoutesBuilder(new ContentFilterRouteBuilder());
        main.run(args);
    }

}
