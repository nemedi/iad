package demo;

import org.apache.camel.main.Main;

public class MainApplication {

	public static void main(String[] args) throws Exception {
		if (args.length != 4) {
			return;
		}
		int backendPort = Integer.parseInt(args[0]);
		String serverId = args[1];
		String frontendHost = args[2];
		int frontendPort = Integer.parseInt(args[3]);
		Main main = new Main();
		main.configure().addRoutesBuilder(new MainRouteBuilder(backendPort, serverId, frontendHost, frontendPort));
		main.run();
	}

}
