package demo;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MainConfiguration {

	@Value("${port}")
	private int port;
	
	@Value("${cache.folder}")
	private String cacheFolder;
	
	@Value("${ottomotor.endpoint}")
	private String ottomotorEndpoint;
	
	@Value("${ottomotor.query}")
	private String ottomotorQuery;
	
	@Value("${openstreetmap.endpoint}")
	private String openstreetmapEndpoint;
	
	@Value("${openstreetmap.query}")
	private String openstreetmapQuery;
	
	public int getPort() {
		return port;
	}
	
	public String getCacheFolder() {
		return cacheFolder;
	}
	
	public String getOttomotorEndpoint() {
		return ottomotorEndpoint;
	}
	
	public String getOttomotorQuery() {
		return ottomotorQuery;
	}
	
	public String getOpenstreetmapEndpoint() {
		return openstreetmapEndpoint;
	}
	
	public String getOpenstreetmapQuery() {
		return openstreetmapQuery;
	}
}
