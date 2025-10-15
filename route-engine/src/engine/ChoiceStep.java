package engine;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ChoiceStep implements Step {
	
	private List<WhenStep> steps;
	
	public ChoiceStep() {
		this.steps = new ArrayList<WhenStep>();
	}

	@Override
	public List<Exchange> process(List<Exchange> exchanges) {
		List<Exchange> list = new ArrayList<Exchange>();
		for (Exchange exchange : exchanges) {
			for (WhenStep step : steps) {
				if (step.test(exchange)) {
					list.addAll(step.process(Arrays.asList(exchange)));
				}
			}
		}
		return list;
	}

}
