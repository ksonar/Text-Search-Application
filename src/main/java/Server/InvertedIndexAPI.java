package Server;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import InvertedIndex.BuildInvertedIndex;
import InvertedIndex.LogData;
import org.json.simple.JSONObject;
/*
 * @author ksonar
 * Build a set of Inverted Indexes based on user choice
 */
public class InvertedIndexAPI extends HttpServlet{
	private static final long serialVersionUID = 1L;
	private String landingPage = "InvertedIndex.html";
	private String builtPage = "BuiltInvertedIndex.html";
	private String queryPage = "QueryType.html";
	private String config = "config.json";
	private boolean build = false;
	private JSONObject obj = new JSONObject();
	public static BuildInvertedIndex invertedIndex;
	

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		PrintWriter out = response.getWriter();
		response.setContentType("text/html");
		response.setStatus(HttpServletResponse.SC_OK);
		if(build) {
			LogData.log.info("GET : " + getServletName() + " : " + queryPage);
			out.println(ReadPage.readPage(queryPage));
		}
		else {
			LogData.log.info("GET : " + getServletName() + " : " + landingPage);
			out.println(ReadPage.readPage(landingPage));
		}		
	}
	/*
	 * Process user input and build Inverted Indexes through a PubSUb
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
		PrintWriter out = response.getWriter();
		response.setContentType("text/html");
		response.setStatus(HttpServletResponse.SC_OK);
		
		String[] q = request.getParameterValues("data");
		if(q != null) {
			invertedIndex = new BuildInvertedIndex();
			buildJSON(q);
			invertedIndex.buildInvertedIndex(config);
			out.println(ReadPage.readAndBuildPage(builtPage, invertedIndex));
		}
		build = true;
	}
	
	/*
	 * Taking input fed by user and defining params for the PubSub
	 * @param query
	 */
	public void buildJSON(String[] query) {
		String type;
		ArrayList<String> pubFiles = new ArrayList<>();

		ArrayList<String> pubTypes = new ArrayList<>();

		ArrayList<String> subInvertedIndex = new ArrayList<>();

		
		for(String file : query) {
			pubFiles.add(file);
			if(file.toLowerCase().contains("review")) {
				type = "AmazonReview";
			}
			else {
				type = "AmazonQA";
				}
			pubTypes.add(type);
			if(!subInvertedIndex.contains(type)) {
				subInvertedIndex.add(type);
			}
		}

		obj.put("pubFiles", pubFiles);
		obj.put("pubTypes" ,pubTypes);
		obj.put("subInvertedIndex" ,subInvertedIndex);
		obj.put("brokerType" ,"AsyncUnordered");
		obj.put("poolSize" ,6);
		obj.put("queueSize", 20);
		try {
			FileWriter f = new FileWriter(config);
			f.write(obj.toJSONString());
			f.close();
		} catch (IOException e) {
			LogData.log.warning("COULD NOT WRITE JSON TO FILE");
		}
		
	}
}
