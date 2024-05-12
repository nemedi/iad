package demo;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class Component {

	private Class<? extends Supplier<List<?>>> supplierType;
	private Class<? extends Consumer<Exchange>> consumerType;

	public Component(Class<? extends Supplier<List<?>>> supplierType,
			Class<? extends Consumer<Exchange>> consumerType) {
		this.supplierType = supplierType;
		this.consumerType = consumerType;
	}
	
	public Class<? extends Supplier<List<?>>> getSupplierType() {
		return supplierType;
	}
	
	public Class<? extends Consumer<Exchange>> getConsumerType() {
		return consumerType;
	}
}
