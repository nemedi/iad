package com.axway.trainings.eip.camel.aggregation;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.converter.jaxb.JaxbDataFormat;
import org.apache.camel.model.dataformat.BindyType;

public class AggregationRouteBuilder extends RouteBuilder {

    public void configure() throws JAXBException {
    	from(TestFileResourceUtils.getFileBaseUti(AggregationRouteBuilder.class)
    			+ "?fileName=cities.csv&noop=true")
    	.unmarshal().bindy(BindyType.Csv, City.class)
    	.setHeader("size", simple("${body.size}"))
    	.split().body()
    	.setHeader("district", simple("${body.district}"))
    	.aggregate(header("district"), new DistrictAggregationStrategy())
    	.completionPredicate(exchange -> exchange.getIn().getHeader("size", Integer.class) == 0)
    	.process(e -> {
    		District district = e.getIn().getBody(District.class);
    		System.out.println(district);
    	})
    	.aggregate(constant(true), new DistrictsAggregationStrategy())
    	.completionTimeout(1000)
    	.marshal(new JaxbDataFormat(
    			JAXBContext.newInstance(District.class, DistrictCollection.class)))
    	.to(TestFileResourceUtils.getFileBaseUti(AggregationRouteBuilder.class)
    			+ "?fileName=districts.xml&fileExist=Override")
    	.log("Done");
    }

}
