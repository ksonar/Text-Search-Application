package InvertedIndex;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import PubSub.Setup;
import PubSub.Subscribers;

public class BuildInvertedIndex {
	private QueryInvertedIndex queryObj;
	private Setup<Data> setup;
	private double time;
	private HashMap<String, Integer> countMap = new HashMap<>();
	
	public HashMap<String, Integer> getCountMap() { return countMap; }
	public String getTime() { return time + "secs"; }
	public ArrayList<String> getNames() { 
		ArrayList<String> names = new ArrayList<>();
		for (Subscribers<Data> sub : setup.getSubs()) {
			names.add(sub.getFName());
		}
		return names;
	}
	
	public void buildInvertedIndex(String cFile) {
		LogData.log.info("BUILDING INVERTED INDEX from " + cFile);
		double sTime = System.currentTimeMillis();
		try {
			setup = new Setup<Data>(cFile, Data.class);
			setup.start();
			setup.join();
			countMap = setup.displayCount();
			queryObj = new QueryInvertedIndex(setup.getSubInvertedIndex());
			double eTime = System.currentTimeMillis();
			time = (eTime-sTime)/1000;
			LogData.log.info("BUILT : " + time + "secs");
			//queryObj.queryInvertedIndex();
			
		} catch (IOException e1) {
			LogData.log.warning("UNABLE TO BUILD INVERTED INDEX, IO EXCEPTION");
		}
	}
}
