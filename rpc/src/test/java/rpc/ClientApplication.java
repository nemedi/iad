package rpc;

import java.util.ResourceBundle;

import org.apache.camel.Converter;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.main.Main;

@Converter
public class ClientApplication {
	
	public static void main(String[] args) throws Exception {
		ResourceBundle bundle = ResourceBundle.getBundle("application");
		final int port = Integer.parseInt(bundle.getString("port"));
		Main main = new Main();
		main.configure().addRoutesBuilder(new RouteBuilder() {
			
			@Override
			public void configure() throws Exception {
				onException(Exception.class)
				.log("Error: ${body.message}");
				from("stream:in")
				.choice()
				.when(body().isEqualTo("exit"))
				.process(exchange -> exchange.getContext().shutdown())
				.otherwise()
				.process(exchange -> {
					String[] data = exchange.getIn().getBody(String.class).split(":");
					exchange.getIn().setHeader("method", data[0]);
					exchange.getIn().setBody(data.length > 1 ? data[1] : "");
				})
				.setHeader("port").constant(port)
				.toD("rpc://localhost:${header.port}/cache#${header.method}")
				.convertBodyTo(String.class)
				.to("stream:out"); 
			}
		});
		main.start();
		main.run();
	}

}
