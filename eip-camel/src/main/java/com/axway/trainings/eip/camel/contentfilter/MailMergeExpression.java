package com.axway.trainings.eip.camel.contentfilter;

import org.apache.camel.Exchange;
import org.apache.camel.Expression;

public class MailMergeExpression implements Expression {
	
	@SuppressWarnings("unchecked")
	@Override
	public <T> T evaluate(Exchange exchange, Class<T> type) {
		Recipient recipient = exchange.getIn().getBody(Recipient.class);
		String firstName = recipient.getFirstName();
		String lastName = recipient.getLastName();
		String email = recipient.getEmail();
		if (firstName != null && lastName != null && email != null) {
			return (T) Message.getTemplate().replaceAll("\\$firstName", firstName)
			.replaceAll("\\$lastName", lastName)
			.replaceAll("\\$email", email);
		}
		else {
			return (T) "";
		}
	}

}
