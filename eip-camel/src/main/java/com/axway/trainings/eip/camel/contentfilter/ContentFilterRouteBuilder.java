package com.axway.trainings.eip.camel.contentfilter;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.BindyType;

import com.axway.trainings.eip.camel.aggregation.TestFileResourceUtils;

public class ContentFilterRouteBuilder extends RouteBuilder {

	@Override
	public void configure() throws Exception {
		
		from(TestFileResourceUtils.getFileBaseUti(ContentFilterRouteBuilder.class)
				+ "?fileName=message.txt&noop=true")
		.convertBodyTo(String.class)
		.bean(Message.class, "setTemplate")
		.end();
		
		from(TestFileResourceUtils.getFileBaseUti(ContentFilterRouteBuilder.class)
				+ "?fileName=recipients.csv&noop=true")
		.split(body().tokenize(System.lineSeparator()))
		.unmarshal().bindy(BindyType.Csv, Recipient.class)
		.transform(new MailMergeExpression())
		.to("stream:out");

	}

}
