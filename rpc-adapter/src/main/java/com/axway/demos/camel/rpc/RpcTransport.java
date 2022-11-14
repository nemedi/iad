package com.axway.demos.camel.rpc;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import com.google.gson.Gson;

class RpcTransport {
	
	public static <T> T read(Socket socket, Class<T> type) throws IOException {
		BufferedReader reader = new BufferedReader(
				new InputStreamReader(socket.getInputStream()));
		return deserialize(reader.readLine(), type);
	}
	
	public static <T> void write(T object, Socket socket) throws IOException {
		PrintWriter writer = new PrintWriter(socket.getOutputStream());
		writer.println(new Gson().toJson(object));
		writer.flush();
	}
	
	public static <T> T deserialize(String data, Class<T> type) throws IOException {
		return new Gson().fromJson(data, type);
	}
}