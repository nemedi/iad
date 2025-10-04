package test;

import static engine.RouteBuilder.from;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import engine.Exchange;
import engine.Registry;

public class Program {
	
	static {
		Registry.getInstance()
			.registerConverter(String.class,
					City.class, line -> new City(line))
			.registerConverter(District.class,
					String.class, district -> String.format("%s,%d", district.getName(), district.getInhabitants()));
	}

	@SuppressWarnings("unchecked")
	public static void main(String[] args) {
		from("csv:runtime/input")
		.convertBodyTo(City.class)
		.filter(exchange -> exchange.getBody(City.class).getDistrict() != null)
		.process(exchange -> {
			City city = exchange.getBody(City.class);
			System.out.println(String.format("City: %s, %s, %d", city.getName(), city.getDistrict(), city.getInhabitants()));
		})
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
		.process(exchange -> {
			District district = exchange.getBody(District.class);
			System.out.println(String.format("District: %s, %d", district.getName(), district.getInhabitants()));
		})
		.convertBodyTo(String.class)
		.aggregate(exchange -> true, (oldExchange, newExchange) -> {
			String line = newExchange.getBody(String.class);
			List<String> lines = oldExchange == null
					? new ArrayList<String>()
					: oldExchange.getBody(List.class);
			lines.add(line);
			Exchange exchange = oldExchange == null ? newExchange : oldExchange;
			exchange.setBody(lines);
			return exchange;
		})
		.convertBodyTo(String.class)
		.to("csv:runtime/output");
		
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
