package PubSub;
import InvertedIndex.*;
import InvertedIndex.Config;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

/*
 * Class to read from config file and setup all resources for the Pub-Sub framework
 * @author ksonar
 */
public class Setup<T> {
	private HashMap<String, Data> dataTypes = new HashMap<>();
	private String cFile;
	public static Config configData;
	private ArrayList<Publisher<T>> publishers = new ArrayList<>();
	private ArrayList<Subscribers<T>> subscribers = new ArrayList<>();
	private Broker<T> broker;
	private ArrayList<Thread> publisherThreads = new ArrayList<>();
	private Thread brokerThread = new Thread();
	private long sTime, eTime;
	//getSubscriberList
	public ArrayList<Subscribers<T>> getSubs() { return subscribers; }
	
	/*
	 * Display count of old, new and equal. Also display count of total items received 
	 */
	public HashMap<String, Integer> displayCount() {
		HashMap<String, Integer> countMap = new HashMap<>();
		for(Subscribers<T> sub : subscribers) {
			countMap.put(sub.getFName() + " Count", sub.getCount1());
			countMap.put(sub.getFName() + " InvertedIndex", sub.getSize());
		}
		return countMap;
	}
	
	
	/*
	 * Constructor that reads and builds the required configurations
	 * @params cFile, type
	 */
	public Setup(String cFile, Class<T> type) throws IOException {
		LogData.createLogger();
		this.cFile = cFile;
		setMap();
		read();
		build();
	}
	
	public void setMap() {
		dataTypes.put("AmazonReview", new AmazonReview());
		dataTypes.put("AmazonQA", new AmazonQA());
	}

	/*
	 * Read the config file and store into an object
	 */
	public void read() {
		Gson gson = new GsonBuilder().create();
		try {
		BufferedReader f = Files.newBufferedReader(Paths.get(cFile));
		configData = gson.fromJson(f,  Config.class);

		//configData = gson.fromJson(cFile, Config.class);
		LogData.log.info(configData.toString());
		}
		
		catch (IOException i) {
			LogData.log.warning("NO SUCH FILE");
			System.out.println("NO SUCH FILE");
			System.exit(1);
		}
		catch (JsonSyntaxException i) {
			LogData.log.warning("JSON ERROR");
		}		
	}
	
	/*
	 * Setup broker, publishers and subscribers and required threads
	 */
	public void build() throws IOException {
		setBroker();
		setPub();
		setSub();
	}
	
	/*
	 * Start all required threads
	 */
	public void start() {
		sTime = System.currentTimeMillis();
		for(Thread t : publisherThreads) {
			t.start();
			LogData.log.info("PUB THREAD STARTED : " + t);
		}
		
		if((configData.type().equals("AsyncOrdered"))) {
			brokerThread.start();
			LogData.log.info("ASYNCORDERED BROKER THREAD STARTED : " + brokerThread);
		}
		
	}
	
	/*
	 * Join all publisherThreads, call shutdown and then close all subscriber files
	 */
	public void join() {
		for(Thread t : publisherThreads) {
			try {
				t.join();
				LogData.log.info("PUB THREAD JOINED : " + t);
				eTime = System.currentTimeMillis();
				LogData.log.info("PUB EXEC TIME : "+ (eTime-sTime)/1000.0);
			} catch (InterruptedException e) {
				LogData.log.warning("THREAD JOIN ERROR");
			}
		}
		broker.shutdown();
		System.out.println("SHUTDOWN EXEC TIME : "+ (eTime-sTime)/1000.0);
		if((configData.type().equals("AsyncOrdered"))) {
			try {
				brokerThread.join();
				LogData.log.info("ASYNCORDERD THREAD JOINED : " + brokerThread);
			} catch (InterruptedException e) {
				LogData.log.warning("ASYNCORDERED BROKER THREAD JOIN ERROR");
			}
		}
		for(Subscribers<T> s : subscribers) {
			s.close();
		}
		LogData.log.info("CLOSED SUBSCRIBER FILES");
	}
	
	/*
	 * Set broker type
	 */
	public void setBroker() {
		String brokerType = configData.type();
		if(brokerType.equals("Sync")) {
			broker = new SyncBroker<T>();
		}
		else if(brokerType.equals("AsyncOrdered")) {
			broker = new AsyncOBroker<T>(configData.getQueueSize());
			brokerThread = new Thread((Runnable) broker);
		}
		else if(brokerType.equals("AsyncUnordered")) {
			broker = new AyncUBroker<T>(configData.getPoolSize());
		}
		else {
			LogData.log.warning("INVALID BROKER, please try again");
			System.exit(1);
		}
	}
	
	/*
	 * Set Publishers and its threads
	 */
	public void setPub() {
		for(int i = 0; i < configData.pubs().size(); i++) {
			String pubFile = configData.pubs().get(i);
			String pubType = configData.getPubTypes().get(i);
			LogData.log.info("PUB FILE : " + pubFile + " TYPE : " + pubType);
			publishers.add(new Publisher<T>(pubFile, pubType, broker));
		}
		for(Publisher<T> p : publishers) {
			publisherThreads.add(new Thread(p));
		}
	}
	
	/*
	 * Set Subscribers
	 */
	public void setSub() throws IOException {
		for(String fName : configData.subs()) {
			subscribers.add(new Subscribers<T>(broker, fName, dataTypes.get(fName)));
		}
		LogData.log.info("#Subs : " + subscribers.size());
		
	}
	
	/*
	 * Return a hashmap of name and corresponding invertedindex of each subscriber
	 */
	public HashMap<String, InvertedIndex> getSubInvertedIndex() {
		HashMap<String, InvertedIndex> ii = new HashMap<>();
		for(Subscribers<T> sub : subscribers) {
			if(ii.containsKey(sub.getFName())) {
				continue;
			}
			else {
				ii.put(sub.getFName(), sub.getInvertedIndex());
			}
		}
		return ii;
	}

}
