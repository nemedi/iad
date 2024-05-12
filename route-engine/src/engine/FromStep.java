package engine;

import java.util.List;
import java.util.function.Supplier;

import static java.util.stream.Collectors.toList;

public class FromStep implements Step {

	private Supplier<List<?>> supplier;

	public FromStep(Supplier<List<?>> supplier) {
		this.supplier = supplier;
	}

	@Override
	public List<Exchange> process(List<Exchange> exchanges) {
		return supplier.get()
				.stream()
				.map(body -> new Exchange(body))
				.collect(toList());
	}

}
