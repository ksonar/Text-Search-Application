package InvertedIndex;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/*
 * @author ksonar
 * Get query results from list of InvertedIndices
 */
public class QueryInvertedIndex {
	public static HashMap<String, InvertedIndex> list = new HashMap<>();
	public QueryInvertedIndex(HashMap<String, InvertedIndex> list) {
		this.list = list;
		for(Map.Entry<String, InvertedIndex> item : list.entrySet()) {
			item.getValue().sortWordIndex();
		}
	}
	
	public String queryInvertedIndex(String query, String param) {
		String data = "";
		InvertedIndex index;

		if( query.equals("reviewsearch") && (list.get("AmazonReview") != null)) {
			data += list.get("AmazonReview").search(param); 
		}
		else if( query.equals("qasearch") && (list.get("AmazonQA") != null)) {
			data += list.get("AmazonQA").search(param); 
		}
		else if(query.equals("AmazonReviewFrequencyD")) {
			data += list.get("AmazonReview").getTopFrequency(Integer.parseInt(param), "descending"); 
		}
		else if( query.equals("AmazonQAFrequencyD")) {
			data += list.get("AmazonQA").getTopFrequency(Integer.parseInt(param), "descending"); 
		}		
		else if( query.equals("AmazonReviewFrequencyA")) {
			data += list.get("AmazonReview").getTopFrequency(Integer.parseInt(param), "ascending"); 
		}
		else if( query.equals("AmazonQAFrequencyA")) {
			data += list.get("AmazonQA").getTopFrequency(Integer.parseInt(param), "ascending"); 
		}
		else if( query.equals("AmazonReviewDistributionD")) {
			data += list.get("AmazonReview").getDistribution(Integer.parseInt(param), "descending"); 
		}
		else if( query.equals("AmazonQADistributionD")) {
			data += list.get("AmazonQA").getDistribution(Integer.parseInt(param), "descending"); 
		}
		else {
			data += "-10";
		}
		return data;
	}

}
