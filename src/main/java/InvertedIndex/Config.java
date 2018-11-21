package InvertedIndex;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

/*
 * @author ksonar
 * Read data from config file for setting up PubSub model
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
		return pubFiles + "\t" + pubTypes + "\t" + subInvertedIndex + "\t" + brokerType + '\t' + poolSize + '\t' + queueSize;
		
	}
	/*
	public static Config readConfig(String file) {
		Gson gson = new GsonBuilder().create();
		try {
		BufferedReader f = Files.newBufferedReader(Paths.get(file));
		configData = gson.fromJson(f, Config.class);
		System.out.println(configData.toString() + '\n');
		LogData.log.info(configData.toString());
		}
		catch (IOException | NullPointerException i) {
			LogData.log.warning("NO SUCH FILE");
			System.out.println("NO SUCH FILE");
			System.exit(1);
		}
		catch (JsonSyntaxException i) {
			System.out.println("JSON");
			LogData.log.warning("NO SUCH FILE");
		}		
	return configData;
	}
	*/
	
	//getters
	public ArrayList<String> pubs() { return pubFiles; }
	public ArrayList<String> subs() { return subInvertedIndex; }
	public String type() { return brokerType; }
	public int getPoolSize() { return poolSize; }
	public int getQueueSize() { return queueSize; }
	public ArrayList<String> getPubTypes() { return pubTypes; }

}
