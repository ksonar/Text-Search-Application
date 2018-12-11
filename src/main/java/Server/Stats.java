package Server;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import Constants.TableHeaders;
import InvertedIndex.BuildInvertedIndex;
import InvertedIndex.LogData;
import InvertedIndex.QueryInvertedIndex;
/*
 * Compute and display stats like most & least frequent and distributed words
 * @author ksonar
 */
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
			compute1 = ReadPage.buildTable(compute1, queryA, param);
			String compute2 = BuildInvertedIndex.queryObj.queryInvertedIndex(name+queryD, param);
			compute2 = ReadPage.buildTable(compute2, queryD, param);
			String compute3 = BuildInvertedIndex.queryObj.queryInvertedIndex(name+query, param);
			compute3 = ReadPage.buildTable(compute3, queryD, param);
			//data += TableHeaders.headers.get("Frequency") + reOrganize(compute1, compute2, Integer.parseInt(param));
			data += TableHeaders.headers.get("LeastFrequent") + compute1 + "</br>";
			data += TableHeaders.headers.get("MostFrequent") + compute2 + "</br>";
			data += TableHeaders.headers.get("MostDistributed") + compute3 + "</br>";
		}
		LogData.log.info("Stats page returned with num of rows = " + param);
		out.println(ReadPage.readAndBuildPage(resultPage, data));
	}
	
	public String reOrganize(String s1, String s2, int num) {
		String[] r1 = s1.split("\n\n");
		String[] r2 = s2.split("\n\n");
		String result = "";
		for(int i = 0; i < num; i++) {
			result += r1[i] + "\t" + r2[i];
		}
		return result;
	}

}
