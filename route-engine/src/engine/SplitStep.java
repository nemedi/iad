package engine;

import java.util.ArrayList;
import java.util.List;
import static java.util.stream.Collectors.toList;

public class SplitStep implements Step {

	@SuppressWarnings("unchecked")
	@Override
	public List<Exchange> process(List<Exchange> exchanges) {
		List<Exchange> list = new ArrayList<Exchange>();
		for (Exchange exchange : exchanges) {
			if (exchange.getBody() instanceof List) {
				List<Object> body = exchange.getBody(List.class);
				list.addAll(body.stream()
						.map(item -> {
							Exchange newExchange = new Exchange(item);
							newExchange.setHeaders(exchange.getHeaders());
							return newExchange;
						})
						.collect(toList()));
			} else {
				list.add(exchange);
			}
		}
		return list;
	}

}
