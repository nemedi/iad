package com.axway.trainings.eip.camel.transform;

import java.util.StringTokenizer;

import org.apache.camel.Exchange;
import org.apache.camel.Expression;

public class CamelCaseExpression implements Expression {

	@SuppressWarnings("unchecked")
	@Override
	public <T> T evaluate(Exchange exchange, Class<T> type) {
		String content = exchange.getIn().getBody(String.class);
		StringTokenizer tokenizer = new StringTokenizer(content);
		StringBuilder builder = new StringBuilder();
		boolean convertToLower = true;
		while (tokenizer.hasMoreTokens()) {
			String token = tokenizer.nextToken();
			if (convertToLower) {
				token = Character.toLowerCase(token.charAt(0)) + token.substring(1);
				convertToLower = false;
			}
			else {
				token = Character.toUpperCase(token.charAt(0)) + token.substring(1);
			}
			builder.append(token);
		}
		return (T) builder.toString();
	}

}
