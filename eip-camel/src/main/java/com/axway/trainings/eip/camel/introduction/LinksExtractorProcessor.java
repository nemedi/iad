package com.axway.trainings.eip.camel.introduction;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

public class LinksExtractorProcessor implements Processor {

	@Override
	public void process(Exchange exchange) throws Exception {
		String content = exchange.getIn().getBody(String.class);
		Pattern pattern = Pattern.compile("href=\"([^\"]*?)\"");
		Matcher matcher = pattern.matcher(content);
		List<String> links = new ArrayList<String>();
		while (matcher.find()) {
			String link = matcher.group(1);
			if (link == null || "#".equals(link)) {
				continue;
			}
			if (link.startsWith("/")) {
				link = "http://www.axway.com" + link;
			}
			links.add(link);
		}
		exchange.getIn().setHeader("size", links.size());
		exchange.getIn().setBody(links);
	}

}
