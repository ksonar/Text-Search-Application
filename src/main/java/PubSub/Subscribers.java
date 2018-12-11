package PubSub;

import InvertedIndex.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

/*
 * Subscriber class to write JSON objects to a file depending on unixTime
 * @author ksonar
 */

public class Subscribers<T> implements Subscriber<T> {
	private Data jsonData = new Data();
	private Data type;
	private int count1 = 0;
	private String fName;
	private BufferedWriter write;
	public ArrayList<Data> subData = new ArrayList<>();
	private InvertedIndex invertedIndex = new InvertedIndex();
	
	public int getCount1() { return count1;}
	
	public String getFName() { return fName; }
	
	public int getSize() { return invertedIndex.wordIndexSize(); }
	
	public InvertedIndex getInvertedIndex() { return invertedIndex; } 
	/*
	 * Subscriber constructor that will initialize and subscribe itself to the broker
	 * @params broker, fName, unixTime
	 */
	public Subscribers(Broker<T> broker, String fName, Data type) {
		this.fName = fName;
		this.type = type;
		setup();
		broker.subscribe(this);
		LogData.log.info("SUBSCRIBER ADDED");
	}
	
	/*
	 * Close subscriber file
	 * @see PubSub.Subscriber#close()
	 */
	@Override
	public void close()  {
		try {
			write.close();
		} catch (IOException e) {
			LogData.log.warning("UNABLE TO CLOSE FILE");
		}
		
	}
	
	/*
	 * Initialize a write object to append data to a file.
	 */
	private void setup() {
		try {
			write = new BufferedWriter(new FileWriter(new File(fName+".json")));
		} catch (IOException e1) {
			LogData.log.warning("IOException @ Setup");
			System.out.println("IO ERROR @ SETUP");
		}
	}
	
	
	/*
	 * Filter received data to store selective JSON objects to file.
	 * @see PubSub.Subscriber#onEvent(java.lang.Object)
	 */
	@Override
	public synchronized void onEvent(T item) {
		if(item.getClass().equals(type.getClass())) {
			
			if(fName.equals("AmazonReview")) {
				jsonData = (AmazonReview) item;
			}
			else if (fName.equals("AmazonQA")){
				jsonData = (AmazonQA) item;
			}
			
			invertedIndex.addData(jsonData);

			count1++;			
		}

	}
}
