package demo;

import org.apache.camel.main.Main;

public class AggregationApplication {

    public static void main(String... args) throws Exception {
        Main main = new Main();
        main.configure().addRoutesBuilder(new AggregationRouteBuilder());
        main.run(args);
    }

}
