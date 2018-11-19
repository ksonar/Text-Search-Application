package InvertedIndex;

import java.io.IOException;
//import PubSub.*;
import java.util.ArrayList;

import PubSub.*;

public class Start {
	public static ArrayList<InvertedIndex> ii = new ArrayList<>();
	public static void main(String[] args) {
		System.out.println("HELLO");
		String reviewFile = "reviews.json";
		String qaFile = "QA.json";
		String cFile = "config.json";
		ArrayList<Subscribers<Data>> subs = new ArrayList<>();;
		//Config cData = Config.readConfig("config.json");
		//cData.toString();
		double sTime = System.currentTimeMillis();
		/*
		try {
			ProcessData p = new ProcessData(cData.pubs());
			//p.userInput();
		} catch (IOException e) {
			e.printStackTrace();
		}
		*/
		//*
		try {
			Setup<Data> setup = new Setup<Data>(cFile, Data.class);
			setup.start();
			setup.join();
			setup.displayCount();
			subs = setup.getSubs();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}//*/
		
		System.out.println("DONE WITH PUB_SUB & INVETRED INDEX");
		
		//System.out.println(ii.get(0).toString());
		/*
		try {
			ProcessData p = new ProcessData(Setup.configData.subs());
			//p.userInput();
		} catch (IOException e) {
			e.printStackTrace();
		}
		*/
		
		System.out.println("\n~~~~~PROCESSING DONE~~~~~\n");
		double eTime = System.currentTimeMillis();
		System.out.printf("Total Processing time : %.2f secs\n.....................\n\n", (eTime-sTime)/1000);
		
	}

}
