package demo;

import java.util.Scanner;

import org.apache.camel.CamelContext;
import org.apache.camel.impl.DefaultCamelContext;

public class MailboxInputApplication {

	public static void main(String[] args) throws Exception {
		try (CamelContext context = new DefaultCamelContext()) {
			context.addRoutes(new MailboxInputRouteBuilder());
			context.start();
			try (Scanner scanner = new Scanner(System.in)) {
				while (true) {
					if ("exit".equalsIgnoreCase(scanner.nextLine())) {
						break;
					}
				}
			}
			context.stop();
		} 
	}

}
