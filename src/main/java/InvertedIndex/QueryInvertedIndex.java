package InvertedIndex;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;


/*
 * @author ksonar
 * Get query results from list of InvertedIndexes
 * Acts as an inner interface between front end and back end
 */
public class QueryInvertedIndex {
	public static HashMap<String, InvertedIndex> list = new HashMap<>();
	public QueryInvertedIndex(HashMap<String, InvertedIndex> list) {
		QueryInvertedIndex.list = list;
		for(Map.Entry<String, InvertedIndex> item : list.entrySet()) {
			item.getValue().sortWordIndex();
		}
	}
	
	public String queryInvertedIndex(String query, String param) {
		String data = "";

		if( query.equals("AmazonReviewreviewsearch")) {
			data += list.get("AmazonReview").search(param); 
		}
		else if( query.equals("AmazonQAqasearch")) {
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
		else if (query.contains("Complex")) {
			String[] params = param.split("/");
			if(params.length != 3) {
				data += "-1";
			}
			else
			{	
				LinkedHashMap<String, String> paramData = new LinkedHashMap<>();
				paramData.put("0" , params[0]);
				paramData.put("1" ,params[2]);
				String relation = params[1];
				if (query.equals("AmazonReviewComplexR")) {
					data += list.get("AmazonReview").getComplex(paramData, relation);
				}
				else if (query.equals("AmazonQAComplexQ")){ 
					data += list.get("AmazonQA").getComplex(paramData, relation);
				}
			}
		}
		if(data.equals("")) { data = "-10"; }
		return data;
	}

}
