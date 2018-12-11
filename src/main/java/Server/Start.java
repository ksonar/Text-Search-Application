package Server;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletHandler;
import InvertedIndex.LogData;
/*
 * Create a server, add mapping of servlets and start listening on server
 * @author ksonar
 */
public class Start {
	public static void main(String[] args) throws Exception {
		LogData.createLogger();
		Server server = new Server(8080);
		ServletHandler handler = new ServletHandler();
		server.setHandler(handler);
		
		handler.addServletWithMapping(LandingPage.class, "");
		handler.addServletWithMapping(InvertedIndexAPI.class, "/InvertedIndexAPI");
		handler.addServletWithMapping(SimpleQuery.class, "/InvertedIndexAPI/SimpleQuery");
		handler.addServletWithMapping(Stats.class, "/InvertedIndexAPI/Stats");
		handler.addServletWithMapping(ComplexQuery.class, "/InvertedIndexAPI/ComplexQuery");
		server.start();
		LogData.log.info("MAPPING DONE, SERVER STARTED");
		server.join();
		
	}

}
