package com.axway.trainings.eip.camel.aggregation;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.converter.jaxb.JaxbDataFormat;
import org.apache.camel.model.dataformat.BindyType;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;

public class AggregationRouteBuilder extends RouteBuilder {

    public void configure() throws JAXBException {
    	from(TestFileResourceUtils.getFileBaseUti(AggregationRouteBuilder.class)
    			+ "?fileName=cities.csv&noop=true")
    	.unmarshal().bindy(BindyType.Csv, City.class)
    	.split().body()
    	.setHeader("district", simple("${body.district}"))
    	.aggregate(header("district"), new DistrictAggregationStrategy())
    	.completionTimeout(1000)
    	.aggregate(constant(true), new DistrictsAggregationStrategy())
    	.completionTimeout(1000)
    	.marshal(new JaxbDataFormat(
    			JAXBContext.newInstance(District.class, DistrictCollection.class)))
    	.to(TestFileResourceUtils.getFileBaseUti(AggregationRouteBuilder.class)
    			+ "?fileName=districts.xml&fileExist=Override")
    	.log("Done");
    }

}
