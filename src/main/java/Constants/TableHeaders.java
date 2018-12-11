package Constants;

import java.util.HashMap;
/*
 * Store all header data of tables
 */
public class TableHeaders {
	public static HashMap<String, String> headers = new HashMap<>();
	
	public TableHeaders() {
		headers.put("reviewsearch", "<h2>Results from Review Data</h2><style>h2 {color:green;}table, th, td {border: 2px solid black;}</style><table style=\"width:100%\"><tr><th>ASIN</th><th>REVIEWER ID</th><th>TEXT</th><th>SCORE</th><th>FREQUENCY</th></tr>");
		headers.put("qasearch", "<h2>Results from QA Data</h2><style> h2 {color:green;}table, th, td {border: 2px solid black;}</style><table style=\"width:100%\"><tr><th>ASIN</th><th>QUESTION</th><th>ANSWER</th><th>FREQUENCY</th></tr>");
		headers.put("metadata", "<style>table, th, td {border: 2px solid black;}</style><table align=\"center\" style=\"width:30%\">");
		headers.put("FrequencyA", "<style>table, th, td {border: 2px solid black;}</style><table  align=\"center\"style=\"width:40%\" table-layout: \"fixed\"><tr><th>WORD</th><th>COUNT</th></tr>");
		headers.put("FrequencyD", "<style>table, th, td {border: 2px solid black;}</style><table align=\"center\" style=\"width:40%\" table-layout: \"fixed\"><tr><th>WORD</th><th>COUNT</th></tr>");
		headers.put("DistributionD", "<style>table, th, td {border: 2px solid black;}</style><table align=\"center\" style=\"width:40%\" table-layout: \"fixed\"><tr><th>WORD</th><th>COUNT</th></tr>");
		headers.put("ComplexR", "<h2>Results from Review Data</h2><style>h2 {color:green;} table, th, td {border: 2px solid black;}</style><table style=\"width:100%\"><tr><th>ASIN</th><th>REVIEWER ID</th><th>TEXT</th><th>SCORE</th><th>FREQUENCY</th></tr>");
		headers.put("ComplexQ", "<h2>Results from QA Data</h2><style>h2 {color:green;} table, th, td {border: 2px solid black;}</style><table style=\"width:100%\"><tr><th>ASIN</th><th>QUESTION</th><th>ANSWER</th><th>FREQUENCY</th></tr>");

		headers.put("LeastFrequent", "<h2 style=\"color:orange;\">Least Frequent Words<h2>");
		headers.put("MostFrequent", "<h2 style=\"color:orange;\">Most Frequent Words<h2>");
		headers.put("MostDistributed", "<h2 style=\"color:orange;\">Most Distributed Words<h2>");
		
		headers.put("Illegal", "<h1 style=\"color:red;\">You have sent an illegeal query request...</h1>");
		headers.put("DoesNotExist", "<h1 style=\"color:red;\">Data does not exist for the given input []...</h1>");
		headers.put("EmptyQuery", "<h1 style=\"color:red;\">You have sent an empty query, please try again<h1>");
	}

}
