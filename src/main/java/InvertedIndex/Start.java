package InvertedIndex;

import java.io.IOException;
//import PubSub.*;
import java.util.ArrayList;

import PubSub.*;
/*
 * @author ksonar
 * Start PubSub InveetedIndex model and query
 */
public class Start {
	public static ArrayList<InvertedIndex> ii = new ArrayList<>();
	public static void main(String[] args) {
		//LogData.createLogger();
		QueryInvertedIndex queryObj;
		System.out.println("HELLO");

		String cFile = "config.json";

		double sTime = System.currentTimeMillis();

		try {
			Setup<Data> setup = new Setup<Data>(cFile, Data.class);
			setup.start();
			setup.join();
			setup.displayCount();
			queryObj = new QueryInvertedIndex(setup.getSubInvertedIndex());
			System.out.println("DONE WITH PUB_SUB & INVETRED INDEX");
			System.out.println("\n~~~~~PROCESSING DONE~~~~~\n");
			double eTime = System.currentTimeMillis();
			System.out.printf("Total Processing time : %.2f secs\n.....................\n\n", (eTime-sTime)/1000);
			queryObj.queryInvertedIndex();
			
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

}
