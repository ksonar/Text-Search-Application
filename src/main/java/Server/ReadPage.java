package Server;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import InvertedIndex.LogData;

public class ReadPage {
	private static String root = "files/";
	
	public static String readPage(String fName) {
		
		String page = "";
		try {
			InputStream in = new FileInputStream(new File(root+fName));
			page = oneLine(in);
			LogData.log.info("PAGE LOADED : " + fName);
			
		} catch (IOException e) {
			LogData.log.warning("COULD NOT LOAD FILE : " + fName);
		}
		return page;
	}
	
	public static String readPage(String fName, HashMap<String, Integer> mapCount, String time, ArrayList<String> subs) {
		String page = "";
		String subNames = "";
		try {
			InputStream in = new FileInputStream(new File(root+fName));
			page = oneLine(in);
			LogData.log.info("PAGE LOADED : " + fName);

			for(String str : subs) { subNames += str + "  "; }
			
			String table = buildTable(mapCount);
			page = page.replace("<###>", table);
			page = page.replace("<@@@>", time);
			page = page.replace("<$$$>", subNames);
			
		} catch (IOException e) {
			LogData.log.warning("COULD NOT LOAD FILE : " + fName);
		}
		
		
		return page;
	}
	
	public static String buildTable(HashMap<String, Integer> map) {
		String tableHead = "<style>table, th, td {border: 2px solid black;}</style><table style=\"width:50%\">";
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
	private static String oneLine(InputStream instream) throws IOException {
		ByteArrayOutputStream bout = new ByteArrayOutputStream();
		byte b = (byte) instream.read();
		while(b != -1) {
			bout.write(b);
			b = (byte) instream.read();
		}
		return new String(bout.toByteArray());
	}

}
