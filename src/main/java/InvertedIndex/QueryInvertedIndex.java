package InvertedIndex;

import java.util.HashMap;
import java.util.Scanner;

/*
 * @author ksonar
 * Get query results from list of InvertedIndices
 */
public class QueryInvertedIndex {
	public HashMap<String, InvertedIndex> list = new HashMap<>();
	public QueryInvertedIndex(HashMap<String, InvertedIndex> list) {
		this.list = list;
	}
	
	public void queryInvertedIndex() {
		System.out.println("Enter operations like :\nfind <asin>\nreviewsearch/qasearch <term>\nreviewpartialsearch/qapartialsearch <term>\n ");
		Scanner sc = new Scanner(System.in);
		String input;
		String[] split;
		
		do {
			System.out.print("Enter query : ");
			input = sc.nextLine();
			split = input.split("\\s");
			String function;
			if(split.length == 1 && (split[0].toLowerCase().equals("exit")))
				System.exit(0);
			else if(split.length != 2) {
				System.out.println("Incorrect length of arguments, please try again ");
				continue;
			}
			else {
				function = split[1].replaceAll("[^A-Za-z0-9 ]", "").toLowerCase();
			
				switch (split[0].toLowerCase()) {
				case "reviewsearch" :
					list.get("AmazonReview").search(function);
					break;
				case "qasearch" :
					list.get("AmazonQA").search(function);
					break;
				case "reviewpartialsearch" :
					//i1.partialSearch(function);
					
					break;
				case "qapartialsearch" :
					//i2.partialSearch(function);
					break;
					
				default :
					System.out.println("Incorrect input, please try again...");
				}
			}
				
		} while (true);
		
	}

}
