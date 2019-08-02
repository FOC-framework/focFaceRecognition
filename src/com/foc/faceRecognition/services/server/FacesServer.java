package com.foc.faceRecognition.services.server;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.session.SessionHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;

import com.foc.Globals;

public class FacesServer {
	
	private static int port = 2501;
	
	public static void main(String[] args) throws Exception {
		Server server = new Server(port);
		 
		ServletContextHandler contextHandler = new ServletContextHandler(ServletContextHandler.SESSIONS);
		contextHandler.setSessionHandler(new SessionHandler());
		contextHandler.addServlet(FaceStartServlet.class, "/start");

		server.setHandler(contextHandler);
		
		server.start();
		Thread th = new Thread(new Runnable() {
			@Override
			public void run() {
			  try{
					Thread.sleep(1000);
					firstCall();
				}catch (InterruptedException e){
					e.printStackTrace();
				}
			}
		});
		th.start();
		server.join();
	}
	
	private static void firstCall() {
		String url = "http://localhost:"+port+"/start";
		HttpClient client = HttpClientBuilder.create().build();
		HttpGet request = new HttpGet(url);
		 
		try{
			HttpResponse response = client.execute(request);
		}catch (Exception e){
			Globals.logException(e);
		}
	}
}
