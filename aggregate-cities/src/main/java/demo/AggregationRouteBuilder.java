package demo;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.converter.jaxb.JaxbDataFormat;
import org.apache.camel.model.dataformat.BindyType;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;

public class AggregationRouteBuilder extends RouteBuilder {

    public void configure() throws JAXBException {
    	from("file:data/input?fileName=cities.csv")
    	.unmarshal().bindy(BindyType.Csv, City.class)
    	.setHeader("size", simple("${body.size}"))
    	.split().body()
    	.setHeader("district", simple("${body.district}"))
    	.aggregate(header("district"), new DistrictAggregationStrategy())
    	.completionTimeout(2000)
    	.resequence(simple("${body.name}"))
    	.aggregate(constant(true), new DistrictsAggregationStrategy())
    	.completionTimeout(2000)
    	.marshal(new JaxbDataFormat(JAXBContext.newInstance(City.class, District.class)))
    	.to("file:data/output?fileName=districts.xml&fileExist=Override")
    	.log("Done");
    }

}
