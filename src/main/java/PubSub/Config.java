package PubSub;

import java.util.ArrayList;



/*
 * Config class to load data from the configuration file
 * @author ksonar
 */

public class Config {
	public static Config configData;
	private ArrayList<String> pubFiles;
	private ArrayList<String> pubTypes;
	private ArrayList<String> subInvertedIndex;
	private String brokerType;
	private int poolSize;
	private int queueSize;
	
	@Override
	public String toString() {
		return pubFiles + "\n" + pubTypes + "\n" + subInvertedIndex + "\n" + brokerType + '\n' + poolSize + '\n' + queueSize;
		
	}
	
	//getters
	public ArrayList<String> pubs() { return pubFiles; }
	public ArrayList<String> subs() { return subInvertedIndex; }
	public String type() { return brokerType; }
	public int getPoolSize() { return poolSize; }
	public int getQueueSize() { return queueSize; }
	public ArrayList<String> getPubTypes() { return pubTypes; }

}
