package mailer;

import java.io.IOException;
import java.util.Base64;
import java.util.Map;
import java.util.Map.Entry;
import java.util.ResourceBundle;

import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.sql.SqlComponent;
import org.apache.camel.model.dataformat.YAMLLibrary;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import jakarta.mail.BodyPart;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMultipart;

public class MainRouteBuilder extends RouteBuilder {
	
	private static final String SUBJECT_MARKER = "@mailer";
	
	@Override
	public void configure() throws Exception {
		onException(Throwable.class)
		.log("${body}");
		ResourceBundle bundle = ResourceBundle.getBundle("application");
		String user = bundle.getString("email.user");
		String password = new String(Base64.getDecoder().decode(bundle.getString("email.password")));
		String driver = bundle.getString("jdbc.driver");
		String url = bundle.getString("jdbc.url");
		String type = bundle.getString("type");
		configureDataSource(getContext(), driver, url);
		if ("file.system".equals(type)) {
			from("file:./runtime/input")
			.convertBodyTo(String.class)
			.filter().method(getClass(), "isTemplateMessage")
			.unmarshal().yaml(YAMLLibrary.SnakeYAML)
			.removeHeaders("*")
			.setHeader("subject").simple("${body[subject]}")
			.setHeader("body").simple("${body[body]}")
			.to("direct:start");
		} else if ("email.system".equals(type)) {
			int interval = Integer.parseInt(bundle.getString("email.interval"));
			fromF("imaps://imap.gmail.com"
				+ "?username=%s&password=%s"
				+ "&delete=true&unseen=true&delay=%d",
				user, password, interval * 1000)
			.filter().method(getClass(), "isTemplateMessage")
			.setHeader("body").method(getClass(), "getMessageBody")
			.to("direct:start");
		}
		from("direct:start")
		.to("sql:select * from contacts")
		.split().body()
		.process(exchange -> {
			Object body = exchange.getIn().getBody();
			System.out.println(body);
		})
		.setHeader("from").constant(user)
		.setHeader("to").simple("${body[email]}")
		.bean(getClass(), "replaceFields")
		.removeHeader("body")
		.log("Sending '${header.subject}' to ${header.to}")
		.setHeader("user").constant(user)
		.setHeader("password").constant(password)
		.toD("smtp://smtp.gmail.com:587"
                + "?username=${header.user}"
                + "&password=${header.password}"
                + "&from=${header.user}"
                + "&to=${header.to}"
                + "&subject=${header.subject}"
                + "&mail.smtp.auth=true"
                + "&mail.smtp.starttls.enable=true");
	}
	
	private void configureDataSource(CamelContext context, String driver, String url) {
		SqlComponent component = context.getComponent("sql", SqlComponent.class);
		DriverManagerDataSource driverManagerDataSource = new DriverManagerDataSource();
		driverManagerDataSource.setDriverClassName(driver);
		driverManagerDataSource.setUrl(url);
		component.setDataSource(driverManagerDataSource);
	}

	public boolean isTemplateMessage(Exchange exchange) {
		if (exchange.getIn().getHeader("CamelFileName") != null) {
			String fileName = exchange.getIn().getHeader("CamelFileName", String.class);
			return fileName.toLowerCase().endsWith(".yml");
		} else if (exchange.getIn().getHeader("subject") != null) {
			String subject = exchange.getIn().getHeader("subject", String.class).trim();
			if (subject.startsWith(SUBJECT_MARKER)) {
				subject = subject.substring(SUBJECT_MARKER.length()).trim();
				exchange.getIn().setHeader("subject", subject);
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}

	public String getMessageBody(Exchange exchange) throws MessagingException, IOException {
		MimeMultipart body = exchange.getIn().getBody(MimeMultipart.class);
		for (int i = 0; i < body.getCount(); i++) {
			BodyPart part = body.getBodyPart(i);
			if (part.getContentType().toLowerCase().startsWith("text/plain")) {
				return part.getContent().toString();
			}
		}
		return "";
	}
	
	@SuppressWarnings("unchecked")
	public String replaceFields(Exchange exchange) {
		Map<String, Object> values = exchange.getIn().getBody(Map.class);
		String subject = exchange.getIn().getHeader("subject", String.class).trim();
		String body = exchange.getIn().getHeader("body", String.class);
		for (Entry<String, Object> entry : values.entrySet()) {
			subject = subject.replace("{{" + entry.getKey() + "}}", entry.getValue().toString());
			body = body.replace("{{" + entry.getKey() + "}}", entry.getValue().toString());
		}
		exchange.getIn().setHeader("subject", subject);
		return body;		
	}
	
}
