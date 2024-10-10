package demo;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;

import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class MainRouteBuilder extends RouteBuilder {

	public class Result {
		
		@JsonIgnore
		private double latitude;
		
		@JsonIgnore
		private double longitude;
		
		@JsonProperty
		private int count;
		
		@JsonProperty
		private String district;
		
		@JsonIgnore
		private String country;

		public Result(double latitude, double longitude, int count) {
			this.latitude = latitude;
			this.longitude = longitude;
			this.count = count;
		}
		
		public double getLatitude() {
			return latitude;
		}
		
		public double getLongitude() {
			return longitude;
		}
		
		public int getCount() {
			return count;
		}
		
		public String getDistrict() {
			return district;
		}
		
		public void setDistrict(String district) {
			this.district = district;
		}
		
		public String getCountry() {
			return country;
		}
		
		public void setCountry(String country) {
			this.country = country;
		}
		
		public void mergeWith(Result result) {
			if (district.equals(result.getDistrict())) {
				count += result.getCount();
			}
		}
		
		@Override
		public String toString() {
			try {
				return new ObjectMapper().writeValueAsString(this);
			} catch (JsonProcessingException e) {
				return "";
			}
		}
		
	}
	
	@Autowired
	private MainConfiguration configuration;

	@SuppressWarnings("unchecked")
	@Override
	public void configure() throws Exception {
		onException(Throwable.class)
			.log("${body}");
		
		restConfiguration()
			.host("0.0.0.0")
			.port(configuration.getPort())
			.component("jetty");
		
		rest("/api")
			.get("/names/{name}")
			.to("direct:searchName");

		
		from("direct:searchName")
			.removeHeaders("Camel*")
			.choice()
				.when().method(getClass(), "isInCache")
					.to("direct:getFromCache")
				.otherwise()
					.to("direct:getFromBackend");

		from("direct:getFromCache")
			.setHeader("Content-Type").constant("application/json")
			.setBody().method(getClass(), "getFromCache");
		
		from("direct:getFromBackend")
			.setHeader("Accept").constant("application/json")
			.setHeader("Cache-Control", constant("no-cache, no-store, must-revalidate"))
            .setHeader("Pragma", constant("no-cache"))
            .setHeader("Expires", constant("0"))
			.setHeader(Exchange.HTTP_QUERY).method(getClass(), "getOttomotorQuery")
			.to(configuration.getOttomotorEndpoint())
			.convertBodyTo(String.class)
			.unmarshal().json(JsonLibrary.Jackson)
			.bean(getClass(), "getResults")
			.setHeader("id").method(getClass(), "getId")
			.split().body()
			.parallelProcessing()
			.enrich("direct:resolveLocation", (originalExchange, enrichedExchange) -> {
				Result result = originalExchange.getIn().getBody(Result.class);
				if (result != null) {
					Properties properties = enrichedExchange.getIn().getBody(Properties.class);
					result.setDistrict(properties.getProperty("district"));
					result.setCountry(properties.getProperty("country"));
				}
				return originalExchange;
			})
			.filter(exchange -> {
				Result result = exchange.getIn().getBody(Result.class);
				return "România".equalsIgnoreCase(result.getCountry())
						&& result.getDistrict() != null;
			})
			.aggregate(simple("${body.district}"), (oldExchange, newExchange) -> {
				Result result = newExchange.getIn().getBody(Result.class);
				if (oldExchange == null) {
					return newExchange;
				} else {
					oldExchange.getIn()
						.getBody(Result.class)
						.mergeWith(result);
					return oldExchange;
				}
			})
			.completionTimeout(3000)
			.filter(exchange -> exchange.getIn().getBody(Result.class).getCount() > 0)
			.resequence(simple("${body.count}"))
			.aggregate(constant(true), (oldExchange, newExchange) -> {
				Result result = newExchange.getIn().getBody(Result.class);
				if (oldExchange == null) {
					List<Result> results = new ArrayList<Result>();
					results.add(result);
					newExchange.getIn().setBody(results);
					return newExchange;
				} else {
					List<Result> results = oldExchange.getIn().getBody(List.class);
					results.add(result);
					return oldExchange;
				}
			})
			.completionTimeout(3000)
			.marshal().json(JsonLibrary.Jackson)
			.wireTap("direct:setInCache")
			.setHeader("Content-Type").constant("application/json");
		
		from("direct:resolveLocation")
			.removeHeaders("Camel*")
			.setHeader("Accept").constant("application/json")
			.setHeader(Exchange.HTTP_QUERY).method(getClass(), "getOpenstreetmapQuery")
			.to(configuration.getOpenstreetmapEndpoint())
			.convertBodyTo(String.class)
			.unmarshal().json(JsonLibrary.Jackson)
			.bean(getClass(), "resolveLocation");
		
		from("direct:setInCache")
			.bean(getClass(), "setInCache");
	}
	
	private Path getPath(String name) {
		return Paths.get(MessageFormat.format("{0}{1}{2}.json",
				configuration.getCacheFolder(), File.separator, name.toLowerCase()));
	}
	
	public String getId() {
		return UUID.randomUUID().toString();
	}
	
	public boolean isInCache(Exchange exchange) {
		String name = exchange.getIn().getHeader("name", String.class);
		return getPath(name).toFile().exists();
	}

	public String getFromCache(Exchange exchange) throws IOException {
		return new String(Files.readAllBytes(getPath(exchange.getIn().getHeader("name", String.class))));
	}
	
	public String getOttomotorQuery(Exchange exchange) {
		return MessageFormat.format("bridgeEndpoint=true&" + configuration.getOttomotorQuery(),
				exchange.getIn().getHeader("name", String.class));
	}
	
	public String getOpenstreetmapQuery(Exchange exchange) {
		Result result = exchange.getIn().getBody(Result.class);
		return MessageFormat.format("bridgeEndpoint=true&" + configuration.getOpenstreetmapQuery(),
				result.getLatitude(), result.getLongitude());
	}
	
	@SuppressWarnings("unchecked")
	public List<Result> getResults(Exchange exchange) {
		final List<Result> results = new ArrayList<Result>();
		Map<String, Object> body = exchange.getIn().getBody(Map.class);
		for (var item : (List<Map<String, Object>>) body.get("ani")) {
			int count = (int) item.get("count");
			Map<String, Object> area = (Map<String, Object>) item.get("area");
			double latitude = Double.parseDouble((String) area.get("centroid_lat"));
			double longitude = Double.parseDouble((String) area.get("centroid_lng"));
			results.add(new Result(latitude, longitude, count));
		}
		return results;
	}
	
	@SuppressWarnings("unchecked")
	public Properties resolveLocation(Exchange exchange) {
		Map<String, Object> body = (Map<String, Object>) exchange.getIn().getBody(Map.class);
		Map<String, Object> address = (Map<String, Object>) body.get("address");
		Properties properties = new Properties();
		String district = (String) address.get("county");
		if (district == null) {
			district = "";
		}
		String country = (String) address.get("country");
		if (country == null) {
			country = "";
		}
		properties.setProperty("district", district);
		properties.setProperty("country", country);
		return properties;
	}
	
	public void setInCache(Exchange exchange) throws IOException {
		Files.write(getPath(exchange.getIn().getHeader("name", String.class)),
				exchange.getIn().getBody(String.class).getBytes());
	}
	
}
