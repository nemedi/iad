package test;

import static engine.RouteBuilder.from;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import engine.Exchange;
import engine.Registry;

public class Program {
	
	static {
		Registry.getInstance().registerConverter(String.class,
				City.class, line -> new City(line));
	}

	@SuppressWarnings("unchecked")
	public static void main(String[] args) {
		from("csv:data/input")
		.convertBodyTo(City.class)
		.filter(exchange -> exchange.getBody(City.class).getDistrict() != null)
		.aggregate(exchange -> exchange.getBody(City.class).getDistrict(),
				(oldExchange, newExchange) -> {
			City city = newExchange.getBody(City.class);
			District district = oldExchange == null
					? new District(city.getDistrict())
					: oldExchange.getBody(District.class);
			district.addCity(city);
			Exchange exchange = oldExchange == null ? newExchange : oldExchange;
			exchange.setBody(district);
			return exchange;
		})
		.resequence((firstExchange, secondExchange) ->
			firstExchange.getBody(District.class).getName()
				.compareTo(secondExchange.getBody(District.class).getName()))
		.aggregate(exchange -> true, (oldExchange, newExchange) -> {
			District district = newExchange.getBody(District.class);
			List<District> districts = oldExchange == null
					? new ArrayList<District>()
					: oldExchange.getBody(List.class);
			districts.add(district);
			Exchange exchange = oldExchange == null ? newExchange : oldExchange;
			exchange.setBody(districts);
			return exchange;
		})
		.convertBodyTo(String.class)
		.to("csv:data/output");
		
		try (Scanner scanner = new Scanner(System.in)) {
			while (true) {
				if (scanner.hasNextLine()
						&& "exit".equalsIgnoreCase(scanner.nextLine())) {
					System.exit(0);
				}
			}
		}
	}

}
