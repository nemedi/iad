package engine;

import static java.util.stream.Collectors.joining;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Supplier;

@SuppressWarnings({ "unchecked" })
public class Registry {
	
	private static Registry INSTANCE;

	private Map<String, Component> components =
			new HashMap<String, Component>();
	
	private Map<Class<?>, Map<Class<?>, Converter<?, ?>>> converters =
			new HashMap<Class<?>, Map<Class<?>,Converter<?,?>>>();
	
	static {
		
	}
	
	@SuppressWarnings({ "rawtypes" })
	private Registry() {
		Class supplierType = CsvSupplier.class;
		Class consumerType = CsvConsumer.class;
		registerComponent("csv", supplierType, consumerType);
		Converter<String, List<Object>> listToStringConverter = list -> list.stream()
				.map(item -> item.toString())
				.collect(joining(System.getProperty("line.separator")));
		registerConverter(List.class, String.class,
				list -> listToStringConverter.convert(list));
		registerConverter(ArrayList.class, String.class,
				list -> listToStringConverter.convert(list));
	}
	
	public Registry registerComponent(String schema,
			Class<? extends Supplier<List<?>>> supplierType,
			Class<? extends Consumer<Exchange>> consumerType) {
		components.put(schema, new Component(supplierType, consumerType));
		return this;
	}
	
	public <T, V> Registry registerConverter(Class<V> fromType,
			Class<T> toType, Converter<T, V> converter) {
		if (!converters.containsKey(fromType)) {
			converters.put(fromType, new HashMap<Class<?>, Converter<?,?>>());
		}
		converters.get(fromType).put(toType, converter);
		return this;
	}
	
	public <T, V> Converter<T, V> getConverter(Class<V> fromType, Class<T> toType) {
		return (Converter<T, V>) converters.get(fromType).get(toType);
	}
	
	public Supplier<List<?>> createSupplier(String endpoint) {
		try {
			String schema = endpoint.substring(0, endpoint.indexOf(':'));
			endpoint = endpoint.substring(endpoint.indexOf(':') + 1);
			Class<? extends Supplier<List<?>>> supplierType =
					components.get(schema).getSupplierType();
			Supplier<List<?>> supplier = supplierType.getConstructor(String.class)
					.newInstance(endpoint);
			return supplierType.cast(supplier);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public Consumer<Exchange> createConsumer(String endpoint) {
		try {
			String schema = endpoint.substring(0, endpoint.indexOf(':'));
			endpoint = endpoint.substring(endpoint.indexOf(':') + 1);
			Class<? extends Consumer<Exchange>> consumerType =
					components.get(schema).getConsumerType();
			Consumer<Exchange> consumer = consumerType.getConstructor(String.class)
					.newInstance(endpoint);
			return consumerType.cast(consumer);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static Registry getInstance() {
		if (INSTANCE == null) {
			synchronized (Registry.class) {
				if (INSTANCE == null) {
					INSTANCE = new Registry();
				}
			}
		}
		return INSTANCE;
	}
}
