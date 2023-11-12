package loadbalancing;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.main.Main;

public class MainApplication {

	public static void main(String[] args) throws Exception {
		if (args.length != 3) {
			return;
		}
		Main main = new Main();
		RouteBuilder routeBuilder = new MainRouteBuilder(Integer.parseInt(args[0]),
				args[1], Integer.parseInt(args[2]));
		main.configure().addRoutesBuilder(routeBuilder);
		main.run();
	}

}
