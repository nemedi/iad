package engine;

import java.util.List;
import java.util.function.Predicate;

import static java.util.stream.Collectors.toList;

public class FilterStep implements Step {

	private Predicate<Exchange> predicate;

	public FilterStep(Predicate<Exchange> predicate) {
		this.predicate = predicate;
	}

	@Override
	public List<Exchange> process(List<Exchange> exchanges) {
		return exchanges.stream()
				.filter(predicate)
				.collect(toList());
	}

}
