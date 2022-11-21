package demo;

import java.util.Scanner;

import org.apache.camel.CamelContext;
import org.apache.camel.impl.DefaultCamelContext;

public class FileInputApplication {

	public static void main(String[] args) throws Exception {
		try (CamelContext context = new DefaultCamelContext()) {
			context.addRoutes(new FileInputRouteBuilder());
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
