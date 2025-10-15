package engine;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class WhenStep implements Step {
	
	private Predicate<Exchange> predicate;
	private List<Step> steps;
	
	public WhenStep(Predicate<Exchange> predicate) {
		this.predicate = predicate;
		this.steps = new ArrayList<Step>();
	}
	
	public boolean test(Exchange exchange) {
		return predicate.test(exchange);
	}

	@Override
	public List<Exchange> process(List<Exchange> exchanges) {
		for (Step step : steps) {
			exchanges = step.process(exchanges);
		}
		return exchanges;
	}

}
