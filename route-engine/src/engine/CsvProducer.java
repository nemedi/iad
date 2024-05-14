package engine;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.function.Consumer;

public class CsvProducer implements Consumer<Exchange> {
	
	private String endpoint;

	public CsvProducer(String endpoint) {
		this.endpoint = endpoint;
	}

	@Override
	public void accept(Exchange exchange) {
		try {
			String path = String.format("%s/%s.csv", endpoint, exchange.getId());
			Files.writeString(Paths.get(path), exchange.getBody().toString());
		} catch (IOException e) {
		}
	}

}
