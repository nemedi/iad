package engine;

import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

public class FromStep implements Step {

	private Supplier<?> supplier;

	public FromStep(Supplier<?> supplier) {
		this.supplier = supplier;
	}

	@Override
	public List<Exchange> process(List<Exchange> exchanges) {
		return Arrays.asList(new Exchange(supplier.get()));
	}

}
