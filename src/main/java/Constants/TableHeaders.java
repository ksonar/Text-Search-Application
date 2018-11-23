package Constants;

import java.util.HashMap;

public class TableHeaders {
	public static HashMap<String, String> headers = new HashMap<>();
	
	public TableHeaders() {
		headers.put("revewsearch", "<h2>Results from Review Data</h2><style>table, th, td {border: 2px solid black;}</style><table style=\"width:100%\"><tr><th>ASIN</th><th>REVIEWER ID</th><th>TEXT</th><th>SCORE</th><th>FREQUENCY</th></tr>");
		headers.put("qasearch", "<h2>Results from QA Data</h2><style>table, th, td {border: 2px solid black;}</style><table style=\"width:100%\"><tr><th>ASIN</th><th>QUESTION</th><th>ANSWER</th><th>FREQUENCY</th></tr>");
		headers.put("metadata", "<style>table, th, td {border: 2px solid black;}</style><table style=\"width:30%\">");
		headers.put("FrequencyA", "<style>table, th, td {border: 2px solid black;}</style><table style=\"width:40%\"><tr><th>WORD</th><th>COUNT</th></tr>");
		headers.put("FrequencyD", "<style>table, th, td {border: 2px solid black;}</style><table style=\"width:40%\"><tr><th>WORD</th><th>COUNT</th></tr>");
		headers.put("DistributionD", "<style>table, th, td {border: 2px solid black;}</style><table style=\"width:40%\"><tr><th>WORD</th><th>COUNT</th></tr>");
	}

}
