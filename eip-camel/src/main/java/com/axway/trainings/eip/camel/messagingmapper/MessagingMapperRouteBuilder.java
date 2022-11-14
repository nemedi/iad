package com.axway.trainings.eip.camel.messagingmapper;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.sql.SqlComponent;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

public class MessagingMapperRouteBuilder extends RouteBuilder {

	@Override
	public void configure() throws Exception {
		SqlComponent sqlComponent = getContext().getComponent("sql", SqlComponent.class);
		DriverManagerDataSource dataSource = new DriverManagerDataSource();
		dataSource.setDriverClassName("com.mysql.jdbc.Driver");
		dataSource.setUrl("jdbc:mysql://localhost:3306/cities");
		dataSource.setUsername("root");
		dataSource.setPassword("szervusz");
		sqlComponent.setDataSource(dataSource);

		from("jetty:http://localhost:8080/index.html")
		.process(new NameParameterProcessor())
		.choice()
			.when(header("name").isNotNull())
				.to("direct:executeQuery")
			.otherwise()
				.to("direct:sendIndex")
			.endChoice();
		
		from("direct:sendIndex")
			.process(new FileReaderProcessor());
		
		from("direct:executeQuery")
			.recipientList().method(NameParameterProcessor.class, "getUri")
			.process(new ResultsProcessor())
			.marshal().json(JsonLibrary.Jackson);
	}

}
