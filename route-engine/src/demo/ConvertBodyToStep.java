package demo;

import java.util.List;

public class ConvertBodyToStep implements Step {

	private Class<?> type;

	public ConvertBodyToStep(Class<?> type) {
		this.type = type;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public List<Exchange> process(List<Exchange> exchanges) {
		exchanges.stream()
			.forEach(exchange -> {
				Converter converter = Registry.getInstance()
						.getConverter(exchange.getBody().getClass(),
								type);
				exchange.setBody(converter.convert(exchange.getBody())); 
			});
		return exchanges;
	}

}
