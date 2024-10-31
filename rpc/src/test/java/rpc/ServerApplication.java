package rpc;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.UUID;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.main.Main;

public class ServerApplication {
	
	private static class Cache {
		private static final Map<String, String> cache =
				Collections.synchronizedMap(new HashMap<String, String>());
		
		@SuppressWarnings("unused")
		public static String put(String data) {
			final String id = UUID.randomUUID().toString();
			cache.put(id, data);
			return id;
		}
		
		@SuppressWarnings("unused")
		public static String get(String id) {
			if (cache.containsKey(id)) {
				return cache.get(id);
			} else {
				return null;
			}
		}
		
		@SuppressWarnings("unused")
		public static boolean remove(String id) {
			if (cache.containsKey(id)) {
				cache.remove(id);
				return true;
			} else {
				return false;
			}
		}
			
		@SuppressWarnings("unused")
		public static Set<String> keys() {
			return cache.keySet();
		}
		
		@SuppressWarnings("unused")
		public static Collection<String> values() {
			return cache.values();
		}
		
	}
	
	
	public static void main(String[] args) throws Exception {
		ResourceBundle bundle = ResourceBundle.getBundle("application");
		final int port = Integer.parseInt(bundle.getString("port"));
		Main main = new Main();
		main.configure().addRoutesBuilder(new RouteBuilder() {
			
			@Override
			public void configure() throws Exception {
				onException(Exception.class)
				.log("Error: ${body.message}");
				fromF("rpc://localhost:%d/cache#put", port)
				.bean(Cache.class, "put");
				fromF("rpc://localhost:%d/cache#get", port)
				.bean(Cache.class, "get");
				fromF("rpc://localhost:%d/cache#remove", port)
				.bean(Cache.class, "remove");
				fromF("rpc://localhost:%d/cache#keys", port)
				.bean(Cache.class, "keys");
				fromF("rpc://localhost:%d/cache#values", port)
				.bean(Cache.class, "values");
				from("stream:in")
				.choice()
				.when(body().isEqualTo("exit")).process(exchange -> exchange.getContext().shutdown());
			}
		});
		main.start();
		main.run();
	}

}
