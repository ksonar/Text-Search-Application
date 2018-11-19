package PubSub;

import java.io.IOException;

/*
 * Driver class that will read and setup resources from the configuration file, start, run and shutdown all processes created
 * @author ksonar
 */

public class PublisherSubscriber {

	public static void main(String[] args) throws IOException, InterruptedException {
		if(args.length < 2) {
			System.out.println("INVALID #ARGS");
			System.exit(1);
		}
		LogData.createLogger();
		for(int i = 1; i < args.length; i++) {
			String cFile = args[i];
			
			Setup<AmazonData> setup = new Setup<AmazonData>(cFile, AmazonData.class);
			long sTime = System.currentTimeMillis();
			setup.start();
			setup.join();
			setup.displayCount();	
			long eTime = System.currentTimeMillis();
			System.out.println("\nEXEC TIME : " + ((eTime-sTime)/1000.0) + "secs");	
			System.out.println("~~~~~~~~~~");
		}
	}
}