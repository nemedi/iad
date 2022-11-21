package demo;

import static demo.Utilities.configureDataSource;

import java.io.IOException;
import java.util.ResourceBundle;

import javax.mail.BodyPart;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMultipart;

import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;

public class MailboxInputRouteBuilder extends RouteBuilder {

	@Override
	public void configure() throws Exception {
		final ResourceBundle bundle = ResourceBundle.getBundle("application");
		
		
		configureDataSource(getContext(), bundle);
		fromF("imap://localhost:%d"
				+ "?username=%s"
				+ "&password=%s"
				+ "&delete=true"
				+ "&delay=5000",
				Integer.parseInt(bundle.getString("imap.port")),
				bundle.getString("mailbox.username"),
				bundle.getString("mailbox.password"))
		.setHeader("subject").simple("${header.subject}")
		.setHeader("message").method(MailboxInputRouteBuilder.class, "getMessage")
		.to("sql:select * from contacts")
		.split().body()
		.setHeader("from").constant(bundle.getString("from"))
		.setHeader("to").simple("${body[email]}")
		.bean(Utilities.class, "replaceFields")
		.removeHeader("message")
		.log("Sending '${body}' to ${header.to}")
		.toF("smtp://localhost:%d", Integer.parseInt(bundle.getString("smtp.port")));
	}
	
	public static String getMessage(Exchange exchange) throws MessagingException, IOException {
		MimeMultipart body = exchange.getIn().getBody(MimeMultipart.class);
		for (int i = 0; i < body.getCount(); i++) {
			BodyPart part = body.getBodyPart(i);
			if (part.getContentType().toLowerCase().startsWith("text/plain")) {
				return part.getContent().toString();
			}
		}
		return "";
	}

}
