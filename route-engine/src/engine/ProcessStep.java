package engine;

import java.util.List;
import java.util.function.Consumer;

public class ProcessStep implements Step {
	
	private Consumer<Exchange> consumer;

	public ProcessStep(Consumer<Exchange> consumer) {
		this.consumer = consumer;
	}

	@Override
	public List<Exchange> process(List<Exchange> exchanges) {
		exchanges.stream().forEach(consumer);
		return exchanges;
	}

}
