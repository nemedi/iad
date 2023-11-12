package loadbalancing;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.main.Main;

public class MainApplication {

	public static void main(String[] args) throws Exception {
		if (args.length != 3) {
			return;
		}
		int backendPort = Integer.parseInt(args[0]);
		String serverId = args[1];
		int frontendPort = Integer.parseInt(args[2]);
		Main main = new Main();
		RouteBuilder routeBuilder = new MainRouteBuilder(backendPort, serverId, frontendPort);
		main.configure().addRoutesBuilder(routeBuilder);
		main.start();
		main.run(new String[] {});
	}

}
