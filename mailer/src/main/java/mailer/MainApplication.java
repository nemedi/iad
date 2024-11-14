package mailer;

import org.apache.camel.main.Main;

public class MainApplication {

	public static void main(String[] args) throws Exception {
		Main main = new Main();
		main.configure().addRoutesBuilder(new MainRouteBuilder());
		main.run();
	}

}
