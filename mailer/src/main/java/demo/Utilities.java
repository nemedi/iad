package demo;

import java.util.Map;
import java.util.ResourceBundle;
import java.util.Map.Entry;

import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.component.sql.SqlComponent;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

public class Utilities {

	public static void configureDataSource(CamelContext context, ResourceBundle bundle) {
		SqlComponent component = context.getComponent("sql", SqlComponent.class);
		DriverManagerDataSource driverManagerDataSource = new DriverManagerDataSource();
		driverManagerDataSource.setDriverClassName(bundle.getString("driver"));
		driverManagerDataSource.setUrl(bundle.getString("url"));
		driverManagerDataSource.setUsername(bundle.getString("user"));
		driverManagerDataSource.setPassword(bundle.getString("password"));
		component.setDataSource(driverManagerDataSource);
	}
	
	@SuppressWarnings("unchecked")
	public static String replaceFields(Exchange exchange) {
		Map<String, Object> values = exchange.getIn().getBody(Map.class);
		String message = exchange.getIn().getHeader("message", String.class);
		for (Entry<String, Object> entry : values.entrySet()) {
			message = message.replace("{{" + entry.getKey() + "}}", entry.getValue().toString());
		}
		return message;		
	}
}
