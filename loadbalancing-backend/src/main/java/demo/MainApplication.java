package demo;

import org.apache.camel.main.Main;

public class MainApplication {

	public static void main(String[] args) throws Exception {
		if (args.length != 2) {
			return;
		}
		int port = Integer.parseInt(args[0]);
		String id = args[1];
		Main main = new Main();
		main.enableHangupSupport();
		main.addRouteBuilder(new MainRouteBuilder(port, id));
		main.run();
	}

}
