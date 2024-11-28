package demo;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MainConfiguration {

	@Value("${city}")
	private String city;
	
	@Value("${interval}")
	private int interval;
	
	@Value("${distance}")
	private double distance;
	
	@Value("${frontendPort}")
	private int frontendPort;
	
	@Value("${backendPort}")
	private int backendPort;
	
	public String getCity() {
		return city;
	}
	
	public int getInterval() {
		return 1000 * interval;
	}
	
	public double getDistance() {
		return 1000 * distance;
	}
	
	public int getFrontendPort() {
		return frontendPort;
	}
	
	public int getBackendPort() {
		return backendPort;
	}
}