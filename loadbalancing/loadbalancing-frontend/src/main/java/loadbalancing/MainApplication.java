package loadbalancing;

import java.util.Arrays;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.main.Main;

public class MainApplication {

	public static void main(String[] args) throws Exception {
		if (args.length < 2) {
			return;
		}
		Main main = new Main();
		RouteBuilder routeBuilder = new MainRouteBuilder(Integer.parseInt(args[0]),
				Arrays.copyOfRange(args, 1, args.length));
		main.configure().addRoutesBuilder(routeBuilder);
		main.run();
	}

}
