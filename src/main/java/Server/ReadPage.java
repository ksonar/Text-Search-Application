package Server;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import Constants.TableHeaders;
import InvertedIndex.BuildInvertedIndex;
import InvertedIndex.LogData;

/*
 * Class to read and/or build html pages and tables
 * @auhtor ksonar
 */
public class ReadPage {
	private static String root = "files/";
	private static String page = "";
	private static TableHeaders tableHeaders = new TableHeaders();
	
	/*
	 * Read a static page
	 * @param fName
	 */
	public static String readPage(String fName) {
		try {
			InputStream in = new FileInputStream(new File(root+fName));
			page = readData(in);
			LogData.log.info("PAGE LOADED : " + fName);
			
		} catch (IOException e) {
			LogData.log.warning("COULD NOT LOAD FILE : " + fName);
		}
		return page;
	}
	
	/*
	 * Read and dynamically build a page with data provided
	 */
	public static String readAndBuildPage(String fName, String data) {
		try {
			InputStream in = new FileInputStream(new File(root+fName));
			page = readData(in);
			LogData.log.info("PAGE LOADED : " + fName);
			page = page.replace("<###>", data);
		} catch (IOException e) {
			LogData.log.warning("COULD NOT LOAD FILE : " + fName);
		}
		return page;
	}
	
	/*
	 * Read and dynamically build tables and other parts for a page
	 * @params fName, data, query, param
	 */
	public static String readAndBuildPage(String fName, String data, String query, String param) {
		long sTime = System.currentTimeMillis();
		String result = "";
		if(data.equals("-10")) {
			result += TableHeaders.headers.get("Illegal");
		}
		else if (data.equals("-1")) {
			result += TableHeaders.headers.get("DoesNotExist").replace("[]", "[" + param + "]");
		}
		else if (data.equals("-100")) {
			result += TableHeaders.headers.get("EmptyQuery");
		}
		else { result = buildTable(data, query, param).replaceAll("-10", ""); }

		try {
			InputStream in = new FileInputStream(new File(root+fName));
			page = readData(in);

			page = page.replace("<###>", result);
			LogData.log.info("PAGE LOADED : " + fName);
			long eTime = System.currentTimeMillis();
			long execTime = (eTime-sTime)/1000;
			String exec = "<p>Total time : " + (execTime) + "secs</p>";
			page = page.replace("<!!!>", exec);
			
		} catch (IOException e) {
			LogData.log.warning("COULD NOT LOAD FILE : " + fName);
		}
		return page;
		
	}
	
	/*
	 * Read and dynamically build a page with metadata of Inverted Indexes
	 * @params fName, index
	 */
	public static String readAndBuildPage(String fName, BuildInvertedIndex index) {
		String subNames = "";
		String pubNames = "";
		try {
			InputStream in = new FileInputStream(new File(root+fName));
			page = readData(in);
			LogData.log.info("PAGE LOADED : " + fName);

			pubNames = index.getPubNames().toString();
			subNames = index.getSubNames().toString();
			String table = buildTable(index.getCountMap());
			if(fName.equals("Query.html")) {
				
			}
			page = page.replace("<###>", table);
			page = page.replace("<@@@>", index.getTime());
			page = page.replace("<%%%>", pubNames);
			page = page.replace("<$$$>", subNames);
			
		} catch (IOException e) {
			LogData.log.warning("COULD NOT LOAD FILE : " + fName);
		}
		return page;
	}
	
	/*
	 * Build a table from data provided
	 * @params data, query, param
	 */
	public static String buildTable(String data, String query, String param) {
		String[] lineByLine;
		String content  = "";

		lineByLine = data.split("\n\n");
		if(query.contains("Complex")) {
			String wordParam[] = param.split("/");
			String lhs = "[" + wordParam[0].replaceAll(",", " OR ") + "]";
			String rhs = "[" + wordParam[2].replaceAll(",", " OR ") + "]";
			lhs = lhs.replaceAll(";", " AND ");
			rhs = rhs.replaceAll(";", " AND ");
			String middle = "  <b>" + wordParam[1] + "</b>  ";
			String paramList = lhs + middle + rhs;
			content += "<p>" + "Number of records : " + lineByLine.length + "</p><p>Word : " + paramList + "</p>";
		}
		else if (!query.contains("Frequency")) {
			content += "<p>" + "Number of records : " + lineByLine.length + "</p><p>Word : " + param + "</p>";
		}
		String tableHead;
		tableHead = TableHeaders.headers.get(query);

		content += tableHead;
		for(String line : lineByLine) {
			String[] objData = line.split("\t");
			content += "<tr>";
			for(String obj : objData) {
					content += obj;
			}
			content += "</tr>";
		}
		content += "</table>";

		return content;
	}
	
	/*
	 * Build table for metadata of Inverted Indexes
	 */
	public static String buildTable(HashMap<String, Integer> map) {
		String tableHead = TableHeaders.headers.get("metadata");
		String table = tableHead + "<tr>";
		for(Map.Entry<String, Integer> item : map.entrySet()) {
			table += "<td>" + item.getKey() + "</td>" + "<td>" + item.getValue() + "</td></tr>";
		}
		table += "</table>";
		return table;
	}
	
	/*
	 * Read data from file inputstream
	 * @instream
	 */
	private static String readData(InputStream instream) throws IOException {
		ByteArrayOutputStream bout = new ByteArrayOutputStream();
		byte b = (byte) instream.read();
		while(b != -1) {
			bout.write(b);
			b = (byte) instream.read();
		}
		return new String(bout.toByteArray());
	}

}
