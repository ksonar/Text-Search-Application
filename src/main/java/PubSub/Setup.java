package PubSub;
import InvertedIndex.*;
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
	private Class<T> type;
	private ArrayList<Publisher<T>> publishers = new ArrayList<>();
	private ArrayList<Subscribers<T>> subscribers = new ArrayList<>();
	private Broker<T> broker;
	
	private ArrayList<Thread> publisherThreads = new ArrayList<>();
	private Thread brokerThread = new Thread();
	
	private long sTime, eTime;
	
	public ArrayList<Subscribers<T>> getSubs() { return subscribers; }
	/*
	 * Display count of old, new and equal. Also display count of total items received 
	 */
	public void displayCount() {
		for(Subscribers sub : subscribers) {
			System.out.println(sub.getName() + " : " + sub.getCount1());
			sub.displayStats();
		}
		LogData.log.info("\n\nDONE\n~~~~~~~");
	}
	
	
	/*
	 * Constructor that reads and builds the required configurations
	 * @params cFile, type
	 */
	public Setup(String cFile, Class<T> type) throws IOException {
		LogData.createLogger();
		this.cFile = cFile;
		this.type = type;
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
		configData = gson.fromJson(f, Config.class);
		System.out.println("HHHH : " + configData.toString() + '\n');
		//LogData.log.info(configData.toString());
		}
		catch (IOException | NullPointerException i) {
			//LogData.log.warning("NO SUCH FILE");
			System.out.println("NO SUCH FILE");
			System.exit(1);
		}
		catch (JsonSyntaxException i) {
			//LogData.log.warning("NO SUCH FILE");
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
			//LogData.log.info("PUB THREAD STARTED : " + t);
		}
		
		if((configData.type().equals("AsyncOrdered"))) {
			brokerThread.start();
			//LogData.log.info("ASYNCORDERED BROKER THREAD STARTED : " + brokerThread);
		}
		
	}
	
	/*
	 * Join all publisherThreads, call shutdown and then close all subscriber files
	 */
	public void join() {
		for(Thread t : publisherThreads) {
			try {
				t.join();
				//LogData.log.info("PUB THREAD JOINED : " + t);
				eTime = System.currentTimeMillis();
				//LogData.log.info("PUB EXEC TIME : "+ (eTime-sTime)/1000.0);
			} catch (InterruptedException e) {
				//LogData.log.warning("THREAD JOIN ERROR");
			}
		}
		broker.shutdown();
		System.out.println("SHUTDOWN EXEC TIME : "+ (eTime-sTime)/1000.0);
		if((configData.type().equals("AsyncOrdered"))) {
			try {
				brokerThread.join();
				//LogData.log.info("ASYNCORDERD THREAD JOINED : " + brokerThread);
			} catch (InterruptedException e) {
				//LogData.log.warning("ASYNCORDERED BROKER THREAD JOIN ERROR");
			}
		}
		for(Subscribers<T> s : subscribers) {
			s.close();
		}
		//LogData.log.info("CLOSED SUBSCRIBER FILES");
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
		//Arraylist
		for(int i = 0; i < configData.pubs().size(); i++) {
			String pubFile = configData.pubs().get(i);
			String pubType = configData.getPubTypes().get(i);
			System.out.println("FILE : " + pubFile + " TYPE : " + pubType);
			publishers.add(new Publisher<T>(pubFile, pubType, broker));
		}
		for(Publisher<T> p : publishers) {
			publisherThreads.add(new Thread(p));
		}
		/*
		for(String fName : configData.pubs()) {
			publishers.add(new Publisher<T>(fName, type, broker));
		}
		LogData.log.info("#Pubs : " + publishers.size());
		for(Publisher<T> p : publishers) {
			publisherThreads.add(new Thread(p));
		}
		LogData.log.info("#PubThreads : " + publisherThreads.size());
		*/
	}
	
	/*
	 * Set Subscribers
	 */
	public void setSub() throws IOException {
		for(String fName : configData.subs()) {
			subscribers.add(new Subscribers<T>(broker, fName, dataTypes.get(fName)));
		}
		//LogData.log.info("#Subs : " + subscribers.size());
		
	}
}