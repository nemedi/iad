package demo;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.sql.SqlComponent;
import org.apache.camel.impl.event.CamelContextStartedEvent;
import org.apache.camel.impl.event.CamelContextStoppingEvent;
import org.apache.camel.model.dataformat.YAMLLibrary;
import org.apache.camel.spi.CamelEvent;
import org.apache.camel.support.EventNotifierSupport;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.options.LoadState;

public class MainRouteBuilder extends RouteBuilder {
	
	private static final String PLAYWRIGHT_VARIABLE = "playwright";
	private static final String BROWSER_VARIABLE = "browser";
	private static final String PAGE_VARIABLE = "page"; 

	@SuppressWarnings("unchecked")
	@Override
	public void configure() throws Exception {
		
		final ResourceBundle bundle = ResourceBundle.getBundle("application");
		final String backend = bundle.getString("backend");
		final int size = Integer.parseInt(bundle.getString("size"));
		final String user = bundle.getString("email.user");
		final String password = new String(Base64.getDecoder().decode(bundle.getString("email.password").getBytes()));
		final String driver = bundle.getString("jdbc.driver");
		final String url = bundle.getString("jdbc.url");
		
		SqlComponent component = getContext().getComponent("sql", SqlComponent.class);
		DriverManagerDataSource driverManagerDataSource = new DriverManagerDataSource();
		driverManagerDataSource.setDriverClassName(driver);
		driverManagerDataSource.setUrl(url);
		component.setDataSource(driverManagerDataSource);
		
		getContext().getManagementStrategy().addEventNotifier(new EventNotifierSupport() {
			
			@Override
			public void notify(CamelEvent event) throws Exception {
				if (event instanceof CamelContextStartedEvent) {
					CamelContext context = ((CamelContextStartedEvent) event).getContext();
					Playwright playwright = Playwright.create();
					Browser browser = playwright.chromium().launch(
							new BrowserType.LaunchOptions().setHeadless(true));
					Page page = browser.newPage();
					page.navigate(backend);
					page.waitForLoadState(LoadState.DOMCONTENTLOADED);
					context.getRegistry().bind(PLAYWRIGHT_VARIABLE, playwright);
					context.getRegistry().bind(BROWSER_VARIABLE, browser);
					context.getRegistry().bind(PAGE_VARIABLE, page);
				} else if (event instanceof CamelContextStoppingEvent) {
					CamelContext context = ((CamelContextStoppingEvent) event).getContext();
					Page page = context.getRegistry().lookupByNameAndType(PAGE_VARIABLE, Page.class);
					page.close();
					Browser browser = context.getRegistry().lookupByNameAndType(BROWSER_VARIABLE, Browser.class);
					browser.close();
					Playwright playwright = context.getRegistry().lookupByNameAndType(PLAYWRIGHT_VARIABLE, Playwright.class);
					playwright.close();
				}
			}
		});
		
		onException(Throwable.class)
		.log("${body}");
				
		from("stream:in?promptMessage=Enter message template name: ")
		.choice()
		.when(body().isEqualTo("exit"))
		.process(exchange -> exchange.getContext().shutdown())
		.otherwise()
		.process(exchange -> {
			String name = exchange.getIn().getBody(String.class);
			String template = new String(Files.readAllBytes(Paths.get(String.format("./runtime/templates/%s.yml", name))));
			exchange.getIn().setBody(template);
		})
		.unmarshal().yaml(YAMLLibrary.SnakeYAML)
		.setHeader("subject").simple("${body[subject]}")
		.setHeader("body").simple("${body[body]}")
		.enrich("direct:getPhrases", (originalExchange, detailsExchange) -> {
			Set<String> phrases = detailsExchange.getIn().getBody(Set.class);
			originalExchange.getIn().setHeader("phrases", phrases.stream().collect(Collectors.joining("\n")));
			return originalExchange;
		})
		.to("sql:select * from contacts")
		.split().body()
		.setHeader("from").constant(user)
		.setHeader("to").simple("${body[email]}")
		.process(exchange -> {
			Map<String, Object> map = exchange.getIn().getBody(Map.class);
			String phrases = exchange.getIn().getHeader("phrases", String.class);
			map.put("phrases", phrases);
		})
		.process(exchange -> {
			Map<String, Object> values = exchange.getIn().getBody(Map.class);
			String subject = exchange.getIn().getHeader("subject", String.class).trim();
			String body = exchange.getIn().getHeader("body", String.class);
			for (Entry<String, Object> entry : values.entrySet()) {
				subject = subject.replace("{{" + entry.getKey() + "}}", entry.getValue().toString());
				body = body.replace("{{" + entry.getKey() + "}}", entry.getValue().toString());
			}
			exchange.getIn().setHeader("subject", subject);
			exchange.getIn().setBody(body);
		})
		.removeHeader("body")
		.removeHeader("phrases")
		.log("Sending '${header.subject}' to '${header.to}'.")
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
		
		from("direct:getPhrases")
		.setHeader("size").constant(size)
		.process(exchange -> {
			Page page = exchange.getContext().getRegistry().lookupByNameAndType(PAGE_VARIABLE, Page.class);
			Set<String> phrases = new HashSet<String>();
			int count = exchange.getIn().getHeader("size", int.class);
			while (phrases.size() < count) {
				page.locator("button").all().get(0).click();
				String text = page.locator("#phraseBox").textContent();
				String source = page.locator("#phraseBox").getAttribute("data-source");
				String phrase = String.format("%s (%s)", text, source);
				if (!phrases.contains(phrase)) {
					phrases.add(phrase);
				}
			}
			exchange.getIn().setBody(phrases);
		});
	}
	
}
