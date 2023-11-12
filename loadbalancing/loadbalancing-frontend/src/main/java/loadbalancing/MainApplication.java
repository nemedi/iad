package loadbalancing;

import java.util.Arrays;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.main.Main;

public class MainApplication {

	public static void main(String[] args) throws Exception {
		if (args.length < 2) {
			return;
		}
		int port = Integer.parseInt(args[0]);
		String[] backends = Arrays.copyOfRange(args, 1, args.length);
		Main main = new Main();
		RouteBuilder routeBuilder = new MainRouteBuilder(port, backends);
		main.configure().addRoutesBuilder(routeBuilder);
		main.start();
		main.run(new String[] {});
	}

}
