package engine;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class Component {

	private Class<? extends Supplier<List<?>>> supplierType;
	private Class<? extends Consumer<Exchange>> producerType;

	public Component(Class<? extends Supplier<List<?>>> consumerType,
			Class<? extends Consumer<Exchange>> producerType) {
		this.supplierType = consumerType;
		this.producerType = producerType;
	}
	
	public Supplier<List<?>> createConsumer(String endpoint)
			throws ReflectiveOperationException {
		Supplier<List<?>> supplier = supplierType.getConstructor(String.class)
				.newInstance(endpoint);
		return supplierType.cast(supplier);
	}
	
	public Consumer<Exchange> createProducer(String endpoint)
			throws ReflectiveOperationException {
		Consumer<Exchange> consumer = producerType.getConstructor(String.class)
				.newInstance(endpoint);
		return producerType.cast(consumer);
	}
}
