package com.axway.demos.camel.rpc;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.main.Main;

public class ServerApplication {
	
	private static class Cache {
		private static final Map<Integer, String> cache =
				Collections.synchronizedMap(new HashMap<Integer, String>());
		private static int nextId;
		
		@SuppressWarnings("unused")
		public static int publish(String data) {
			int id = ++nextId;
			cache.put(id, data);
			return id;
		}
		
		@SuppressWarnings("unused")
		public static boolean unpublish(int id) {
			if (cache.containsKey(id)) {
				cache.remove(id);
				return true;
			} else {
				return false;
			}
		}
			
		@SuppressWarnings("unused")
		public static Collection<String> list() {
			return cache.values();
		}

	}
	
	
	public static void main(String[] args) throws Exception {
		Main main = new Main();
		main.configure().addRoutesBuilder(new RouteBuilder() {
			
			@Override
			public void configure() throws Exception {
				onException(Exception.class)
				.log("Error: ${body.message}");
				from("rpc://localhost:8080/cache#publish")
				.bean(Cache.class, "publish");
				from("rpc://localhost:8080/cache#unpublish")
				.bean(Cache.class, "unpublish");
				from("rpc://localhost:8080/cache#list")
				.bean(Cache.class, "list");
			}
		});
		main.start();
		main.run();
	}

}
