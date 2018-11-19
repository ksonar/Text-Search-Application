package InvertedIndex;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

import PubSub.Subscribers;


public class ProcessData {
	Data inst;
	ArrayList<InvertedIndex> indexObj = new ArrayList<>();
	InvertedIndex i1 = new InvertedIndex();
	InvertedIndex i2 = new InvertedIndex();
	HashMap<String, InvertedIndex> ii = new HashMap<>();
	
	/*
	 * Constructor to read file, map  and sort data in appropriate manner.
	 * @param reviewFile, qaFile
	 */

	public ProcessData (ArrayList<String> files) throws IOException {
		AmazonReview r = new AmazonReview();
		AmazonQA qa = new AmazonQA();
		ArrayList<String> pubTypes = Config.configData.getPubTypes();
		for(String obj : Config.configData.subs()) {
			if(ii.containsKey(obj)) { continue; }
			else {
				ii.put(obj, new InvertedIndex());
				System.out.println(ii.get(obj));
			}

		}
		for(int i = 0; i< files.size(); i++) {
			System.out.println(files.get(i));
			double sTime = System.currentTimeMillis();
			//indexObj.add(new InvertedIndex());
			String type = pubTypes.get(i);
			if(type.equals("AmazonReview")) {
				readAndMap(files.get(i),r,ii.get(type));
			}
			else {
				readAndMap(files.get(i),qa,ii.get(type));
			}
			for(Map.Entry<String, InvertedIndex> item : ii.entrySet()) {
				System.out.println(item.getKey() + " : " + item.getValue().wordIndexSize() + " : " + item.getValue().wordIndexHead(5));
			}
			double eTime = System.currentTimeMillis();
			System.out.println("TIME : " + (eTime-sTime)/1000);
		}
		for(Map.Entry<String, InvertedIndex> item : ii.entrySet()) {
			System.out.println(item.getKey() + " : " + item.getValue().wordIndexSize() + " : " + item.getValue().wordIndexHead(5));
		}
	}
	/*
		public ProcessData (ArrayList<Data> subData) {
			int count = 0;
			InvertedIndex i = new InvertedIndex();
			for(Data data : subData) {
				i.addData(data);
				count++;
			}
			System.out.println("COUNT : " + count);
			//Start.ii.add(i);
		}
		*/
		/*
		readAndMap(reviewFile, r);
		i1.sortWordIndex();
		
		//i1.displayCount();
		System.out.println("\n\n\n~~~~~~~~~~~~~~~\n\n\n");
		readAndMap(qaFile, qa);
		i2.sortWordIndex();
		*/
		//i2.displayCount();

	
	
	/*
	 * Read a single file with object type. Store data from GSON object in desired instance of InvertedIndex.
	 * @params file
	 * @params s
	 */
	
	public void readAndMap (String file, Data s, InvertedIndex index) throws IOException {
		int count = 0;
		BufferedReader f = null;
		try {
			f = Files.newBufferedReader(Paths.get(file), StandardCharsets.ISO_8859_1);
		}
		catch (NoSuchFileException i) {
			System.out.printf("MESSAGE : NO SUCH FILE : %s\n",file);
			System.exit(1);
		}
		
		
		Gson gson = new GsonBuilder().create();
		String line;
		
		while((line = f.readLine()) != null) {
			try {
				inst = gson.fromJson(line, s.getClass());
				//System.out.println(inst.toString());
				count++;
			}
			catch (JsonSyntaxException i) {
				System.out.printf("MESSAGE : JsonSyntaxException\n");
			}
			index.addData(inst);
		}
		System.out.println("COUNT : " + count);
	}
	
}
