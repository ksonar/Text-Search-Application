package Server;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import InvertedIndex.BuildInvertedIndex;
import InvertedIndex.LogData;
import InvertedIndex.QueryInvertedIndex;
/*
 * Get result for a pair of multiple words and relation between them.
 * use ',' for OR/UNION relationship
 * Use ';' for AND/INTERSECT relationship
 * Example : (x,y,z) AND_NOT (a;b)
 * @author ksonar
 */
public class ComplexQuery extends HttpServlet {
	private String complexQueryPage = "ComplexQuery.html";
	private String complexResult = "ComplexQueryResult.html";
	private String notBuilt = "NotBuilt.html";
	
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		PrintWriter out = response.getWriter();
		response.setContentType("text/html");
		response.setStatus(HttpServletResponse.SC_OK);
		String build = request.getParameter("build");
		if(build != null && build.equals("true")) {
			LogData.log.info("GET : " + getServletName() + " : " + complexQueryPage);
			out.println(ReadPage.readPage(complexQueryPage));
		}
		else
		{
			LogData.log.info("ILLEGAL ACCESS to " + getServletName() + " redirecting...");
			out.println(ReadPage.readPage(notBuilt));
		}
		
	}
	
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String data = "";
		PrintWriter out = response.getWriter();
		response.setContentType("text/html");
		response.setStatus(HttpServletResponse.SC_OK);
		String build = request.getParameter("build");
		if(build.equals("true")) {
			String word1 = request.getParameter("word1");
			String word2 = request.getParameter("word2");
			String relation = request.getParameter("relation");
			String param = word1 + "/" +  relation + "/" + word2 ;
			String query = request.getParameter("query");
			for(String name : QueryInvertedIndex.list.keySet()) {
				data  += BuildInvertedIndex.queryObj.queryInvertedIndex(name+query, param);
			} 
		
			LogData.log.info(query + " " + param);
			out.println(ReadPage.readAndBuildPage(complexResult, data, query, param));
		}

	}
	
}
