package Server;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import InvertedIndex.BuildInvertedIndex;
import InvertedIndex.LogData;

public class SimpleQuery extends HttpServlet {
	private String queryPage = "SimpleQuery.html";
	private String queryResult = "SimpleQueryResult.html";
	private String notBuilt = "NotBuilt.html";
	
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		PrintWriter out = response.getWriter();
		response.setContentType("text/html");
		response.setStatus(HttpServletResponse.SC_OK);
		String build = request.getParameter("build");
		if(build != null && build.equals("true")) {
			LogData.log.info("GET : " + getServletName() + " : " + queryPage);
			out.println(ReadPage.readPage(queryPage));
		}
		else
		{
			LogData.log.info("ILLEGAL ACCESS to " + getServletName() + " redirecting...");
			out.println(ReadPage.readPage(notBuilt));
		}
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
		PrintWriter out = response.getWriter();
		response.setContentType("text/html");
		response.setStatus(HttpServletResponse.SC_OK);
		String build = request.getParameter("build");
		if(build.equals("true")) {
			String query = request.getParameter("query");
			String param = request.getParameter("param");
			String x = "query=" + query + " param=" + param;
			System.out.println("----"+x);
			String data  = BuildInvertedIndex.queryObj.queryInvertedIndex(query, param);

			out.println(ReadPage.readAndBuildPage(queryResult, data, query));
		}
	}

}
