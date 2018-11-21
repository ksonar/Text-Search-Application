package Server;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import InvertedIndex.BuildInvertedIndex;
import InvertedIndex.LogData;
import org.json.simple.JSONObject;

public class InvertedIndexAPI extends HttpServlet{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String landingPage = "InvertedIndex.html";
	private String builtPage = "BuiltInvertedIndex.html";
	private String config = "config.json";
	private boolean build = false;
	private HashMap<String, Integer> mapCount;
	private String time;
	private ArrayList<String> subs;
	private JSONObject obj = new JSONObject();
	
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		LogData.log.info("GET : " + getServletName());
		response.setContentType("text/html");
		response.setStatus(HttpServletResponse.SC_OK);
		PrintWriter out = response.getWriter();
		
		out.println(ReadPage.readPage(landingPage));
		
	}
	
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
		if(build) {
			
		}
		else {
			response.setContentType("text/html");
			response.setStatus(HttpServletResponse.SC_OK);
			
			PrintWriter out = response.getWriter();
			String[] q = request.getParameterValues("data");
			//String q = request.getParameter("userInput");
			if(q != null) {
				BuildInvertedIndex invertedIndex = new BuildInvertedIndex();
				buildJSON(q);
				invertedIndex.buildInvertedIndex(config);
				mapCount = invertedIndex.getCountMap();
				time = invertedIndex.getTime();
				subs = invertedIndex.getNames();
				
			}
			out.println(ReadPage.readPage(builtPage, mapCount, time, subs));
		}

	}
	
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
		obj.put("poolSize" ,4);
		obj.put("queueSize", 20);
		System.out.println(obj.toJSONString());
		try {
			FileWriter f = new FileWriter(config);
			f.write(obj.toJSONString());
			f.close();
		} catch (IOException e) {
			LogData.log.warning("COULD NOT WRITE JSON TO FILE");
		}
		
	}
}
