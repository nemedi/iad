package com.axway.trainings.eip.camel.aggregation;

public class TestFileResourceUtils {

	public static final String getFileBaseUti(Class<?> type) {
		return "file:src/test/resources/" + type.getPackage().getName();
	}
}
