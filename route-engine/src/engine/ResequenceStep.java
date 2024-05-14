package engine;

import java.util.Comparator;
import java.util.List;

public class ResequenceStep implements Step {

	private Comparator<Exchange> comparator;

	public ResequenceStep(Comparator<Exchange> comparator) {
		this.comparator = comparator;
	}

	@Override
	public List<Exchange> process(List<Exchange> exchanges) {
		exchanges.sort(comparator);
		return exchanges;
	}

}
