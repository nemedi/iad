package demo;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

import static java.util.Collections.emptyList;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class CsvSupplier implements Supplier<List<String>> {
	
	private String endpoint;

	public CsvSupplier(String endpoint) {
		this.endpoint = endpoint;
	}

	@Override
	public List<String> get() {
		List<String> lines = emptyList();
		try {
			Optional<File> file = Arrays.stream(Paths.get(endpoint).toFile().listFiles())
				.filter(f -> f.isFile() && f.getName().toLowerCase().endsWith(".csv"))
				.findFirst();
			if (file.isPresent()) {
				lines = Files.readAllLines(file.get().toPath());
				file.get().delete();
			}
		} catch (IOException e) {
		}
		return lines;
	}

}
