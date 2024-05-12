package demo;

import java.util.List;
import java.util.function.Consumer;

import static java.util.Collections.emptyList;

public class ToStep implements Step {

	private Consumer<Exchange> consumer;

	public ToStep(Consumer<Exchange> consumer) {
		this.consumer = consumer;
	}

	@Override
	public List<Exchange> process(List<Exchange> exchanges) {
		exchanges.stream().forEach(consumer);
		return emptyList();
	}

}
