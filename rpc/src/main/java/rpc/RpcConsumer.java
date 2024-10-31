package rpc;

import java.util.Arrays;

import org.apache.camel.Endpoint;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.component.bean.BeanProcessor;
import org.apache.camel.impl.engine.CamelInternalProcessor;
import org.apache.camel.impl.engine.DefaultChannel;
import org.apache.camel.processor.RoutePipeline;
import org.apache.camel.support.DefaultConsumer;

public class RpcConsumer extends DefaultConsumer {
	
	public RpcConsumer(Endpoint endpoint, Processor processor) {
		super(endpoint, processor);
	}

	public void process(RpcRequest request, RpcResponse response) throws Exception {
		try {
			Exchange exchange = getEndpoint().createExchange();
			exchange.getIn().setBody(request.getArguments());
			Processor processor = getNextProcessor();
			if (processor instanceof BeanProcessor) {
				String method = ((BeanProcessor) processor).getMethod();
				Class<?> type = ((BeanProcessor) processor).getBeanHolder().getBeanInfo().getType();
				Class<?>[] parameterTypes = Arrays.asList(type.getMethods())
					.stream()
					.filter(m -> method.equals(m.getName()))
					.findAny()
					.get()
					.getParameterTypes();
				if (parameterTypes.length == 0) {
					exchange.getIn().setBody(new Object[] {});
				} else if (parameterTypes.length == 1){
					exchange.getIn().setBody(RpcTransport.deserialize((String) request.getArguments()[0],
								parameterTypes[0]));
				} else {
					Object[] arguments = new Object[parameterTypes.length];
					for (int i = 0; i < parameterTypes.length; i++) {
						arguments[i] = RpcTransport.deserialize((String) request.getArguments()[i],
								parameterTypes[i]);
					}
					exchange.getIn().setBody(arguments);
				}
			}
			getProcessor().process(exchange);
			response.setResult(exchange.getIn().getBody());
		} catch (Exception e) {
			response.setFault(e.getMessage());
		}
	}
	
	private Processor getNextProcessor() {
		return ((DefaultChannel) ((RoutePipeline) ((CamelInternalProcessor) getProcessor())
				.getProcessor()).next().get(0)).getNextProcessor();
	}
}
