package Server;
import javax.servlet.Servlet;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletHandler;
import InvertedIndex.LogData;

public class Start {
	public static void main(String[] args) throws Exception {
		LogData.createLogger();
		Server server = new Server(8080);
		ServletHandler handler = new ServletHandler();
		server.setHandler(handler);
		
		handler.addServletWithMapping(LandingPage.class, "");
		handler.addServletWithMapping(LandingPage.class, "/xyz");
		handler.addServletWithMapping(InvertedIndexAPI.class, "/InvertedIndexAPI");
		//handler.addServletWithMapping((Class<? extends Servlet>) InvertedIndexAPI.class, "/InvertedIndex");
		server.start();
		LogData.log.info("MAPPING DONE, SERVER STARTED");
		server.join();
		
	}

}
