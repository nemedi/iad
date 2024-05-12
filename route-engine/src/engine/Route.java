package engine;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

import static java.util.Collections.emptyList;
import static java.util.concurrent.Executors.newFixedThreadPool;
import static java.lang.Thread.sleep;

public class Route {
	
	private static final ExecutorService EXECUTOR_SERVICE =
			newFixedThreadPool(20 * Runtime.getRuntime().availableProcessors());

	private List<Step> steps = new ArrayList<Step>();
	
	private Route() {
		
	}
	
	public static Route from(Supplier<List<?>> supplier) {
		Route route = new Route();
		route.steps.add(new FromStep(supplier));
		return route;
	}
	
	public static Route from(String endpoint) {
		return from(Registry.getInstance().createSupplier(endpoint));
	}
	
	public void to(Consumer<Exchange> consumer) {
		steps.add(new ToStep(consumer));
		EXECUTOR_SERVICE.submit(() -> {
			while (true) {
				try {
					List<Exchange> exchanges = emptyList();
					for (Step step : steps) {
						exchanges = step.process(exchanges);
					}
					sleep(1000);
				} catch (Throwable e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	public void to(String endpoint) {
		to(Registry.getInstance().createConsumer(endpoint));
	}
	
	public Route filter(Predicate<Exchange> predicate) {
		steps.add(new FilterStep(predicate));
		return this;
	}
	
	public Route convertBodyTo(Class<?> type) {
		steps.add(new ConvertBodyToStep(type));
		return this;
	}
	
	public Route aggregate(Function<Exchange, Object> criterion,
			Aggregator<Exchange> aggregator) {
		steps.add(new AggregatorStep(criterion, aggregator));
		return this;
	}
	
	public Route sort(Comparator<Exchange> comparator) {
		steps.add(new SortStep(comparator));
		return this;
	}
	
}
