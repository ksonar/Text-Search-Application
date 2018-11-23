package Server;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import Constants.TableHeaders;
import InvertedIndex.BuildInvertedIndex;
import InvertedIndex.LogData;

public class ReadPage {
	private static String root = "files/";
	private static String page = "";
	private static TableHeaders tableHeaders = new TableHeaders();
	
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
	
	public static String readAndBuildPage(String fName, String data, String query) {
		long sTime = System.currentTimeMillis();
		String result = "";
		if(data.equals("-10")) {
			result += "<h1>You have sent an illegeal query request...</h1>";
		}
		else if (data.equals("-1")) {
			result += "<h1>Data does not exist for the given input...</h1>";
		}
		else { result = buildTable(data, query); }

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
	
	public static String buildTable(String data, String query) {
		String[] lineByLine;
		String content  = "";

		lineByLine = data.split("\n\n");
		if(!query.contains("Frequency")) {
			content += "<p>" + "#Records : " + lineByLine.length + "</p>";
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
