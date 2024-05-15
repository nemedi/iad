package demo; 

import java.util.ResourceBundle;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.main.Main;

public class MainApplication {

	public static void main(String[] args) throws Exception {
		Main main = new Main();
		ResourceBundle bundle = ResourceBundle.getBundle(MainApplication.class.getName());
		int port = Integer.parseInt(bundle.getString("port"));
		String folder = bundle.getString("folder");
		String backend = bundle.getString("backend");
		RouteBuilder routeBuilder = new MainRouteBuilder(port, folder, backend);
		main.configure().addRoutesBuilder(routeBuilder);
		main.run();
	}

}
