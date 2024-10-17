package demo;

import java.nio.file.Files;
import java.nio.file.Paths;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

public class StaticProcessor implements Processor {
	
	private String folder;
	
	public StaticProcessor(String folder) {
		this.folder = folder;
	}

	@Override
	public void process(Exchange exchange) throws Exception {
		String path = exchange.getIn().getHeader(Exchange.HTTP_PATH, String.class);
		if (path.startsWith("/")) {
			path = path.substring(1);
		}
		if (Files.exists(Paths.get(folder, path))) {
			exchange.getIn().setBody(Files.readString(Paths.get(folder, path)));
		}
	}

}
