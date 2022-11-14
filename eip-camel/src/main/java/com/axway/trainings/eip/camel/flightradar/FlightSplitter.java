package com.axway.trainings.eip.camel.flightradar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.camel.Exchange;

public class FlightSplitter {

	public List<ArrayList<Object>> split(Exchange exchange, HashMap<String, ArrayList<Object>> body) {
		List<ArrayList<Object>> results = new ArrayList<ArrayList<Object>>();
		Set<Entry<String, ArrayList<Object>>> set = body.entrySet();
		for (Entry<String, ArrayList<Object>> item : set) {
			if (item.getValue() instanceof ArrayList) {
				ArrayList<Object> list = item.getValue();
				list.add(item.getKey());
				results.add(list);
			}
		}
		exchange.getIn().setHeader("size", results.size());
		return results;
	}
}
