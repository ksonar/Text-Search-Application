package Server;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import InvertedIndex.BuildInvertedIndex;
import InvertedIndex.InvertedIndex;
import InvertedIndex.QueryInvertedIndex;

public class Stats extends HttpServlet{
	private String statsPage = "Stats.html";
	private String resultPage = "StatsResult.html";
	private String queryA = "FrequencyA";
	private String queryD = "FrequencyD";
	private String query = "DistributionD";
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		PrintWriter out = response.getWriter();
		response.setContentType("text/html");
		response.setStatus(HttpServletResponse.SC_OK);
		out.println(ReadPage.readPage(statsPage));
	}
	
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
		PrintWriter out = response.getWriter();
		response.setContentType("text/html");
		String data = "";
		response.setStatus(HttpServletResponse.SC_OK);
		String param = request.getParameter("count");
		for(String name : QueryInvertedIndex.list.keySet()) {
			data += "<h1>Stats from " + name + "</h1>";
			String compute1 = BuildInvertedIndex.queryObj.queryInvertedIndex(name+queryA, param);
			compute1 = ReadPage.buildTable(compute1, queryA);
			String compute2 = BuildInvertedIndex.queryObj.queryInvertedIndex(name+queryD, param);
			compute2 = ReadPage.buildTable(compute2, queryD);
			String compute3 = BuildInvertedIndex.queryObj.queryInvertedIndex(name+query, param);
			compute3 = ReadPage.buildTable(compute3, queryD);
			data += "<h2>Least Frequent Words<h2>" + compute1 + "</br>";
			data += "<h2>Most Frequent Words<h2>" + compute2 + "</br>";
			data += "<h2>Most Distributed Words<h2>" + compute3 + "</br>";
		}
		//String data = BuildInvertedIndex.queryObj.queryInvertedIndex(query, param);
		out.println(ReadPage.readAndBuildPage(resultPage, data));
	}
	

}
