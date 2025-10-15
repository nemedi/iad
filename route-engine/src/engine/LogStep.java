package engine;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.List;
import java.util.Map.Entry;

public class LogStep implements Step {
	
	private String message;
	
	public LogStep(String message) {
		this.message = message;
	}

	@Override
	public List<Exchange> process(List<Exchange> exchanges) {
		exchanges.stream().forEach(exchange -> {
			String message = this.message;
			if (exchange.getBody() != null
					&& message.indexOf('$') > 0
					&& message.indexOf('{') > message.indexOf('$')
					&& message.indexOf('}') > message.indexOf('{')) {
				Object body = exchange.getBody();
				for (Field field : body.getClass().getDeclaredFields()) {
					if (!field.canAccess(body)) {
						field.setAccessible(true);
					}
					try {
						Object value = field.get(Modifier.isStatic(field.getModifiers()) ? null : body);
						message = message.replace("${body." + field.getName() + "}",
								value != null ? value.toString() : "");
					} catch (IllegalArgumentException | IllegalAccessException e) {
						continue;
					}
				}
			}
			if (!exchange.getHeaders().isEmpty()) {
				for (Entry<String, Object> entry : exchange.getHeaders().entrySet()) {
					message = message.replace("${header." + entry.getKey() + "}", entry.getValue().toString());
				}
			}
		});
		System.out.println(message);
		return exchanges;
	}

}
