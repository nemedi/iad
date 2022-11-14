package com.axway.trainings.eip.camel.messagingmapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

public class ResultsProcessor implements Processor {

	@SuppressWarnings("unchecked")
	@Override
	public void process(Exchange exchange) throws Exception {
		List<Result> results = new ArrayList<Result>();
		Object body = exchange.getIn().getBody();
		for (Map<?, ?> item : (List<Map<?, ?>>) body) {
			results.add(fetch(item));
		}
		exchange.getIn().setBody(results.toArray(new Result[results.size()]));
	}
	
	private Result fetch(Map<?, ?> values) {
		Result result = new Result();
		for (Object key : values.keySet()) {
			Object value = values.get(key);
			if ("name".equals(key)) {
				result.setName(value != null ? value.toString() : "");
			}
			if ("district".equals(key)) {
				result.setDistrict(value != null ? value.toString() : "");
			}
			if ("inhabitants".equals(key)) {
				result.setInhabitants(Integer.parseInt(value != null ? value.toString() : "0"));
			}
		}
		return result;
	}

}
