package InvertedIndex;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;


import Server.ReadPage;
/*
 * @author ksonar
 * Builds InvertedIndex, row data received from respective subscribers.
 * Supports -search, partial search, find
 * 			-getTopFrequency(high/low), getDistribution
 * 			-getComplex, union, intersect
 * 
 */
public class InvertedIndex {
	private HashMap<String, Integer> mapCount = new HashMap<String, Integer>();
	private HashMap<String, ArrayList<Data>> wordIndex = new HashMap<>();
	private String fullText;
	private String[] split;
	private HashMap<String, Integer> sortedByCount = new HashMap<>();
	private HashMap<String, Integer> sortedByDistribution = new HashMap<>();
	private HashMap<String, ArrayList<String>> wordList = new HashMap<>();
	private ArrayList<String> words = new ArrayList<>();

	/*
	 * Adds all required text from row data into InvertedIndex
	 * @params data
	 */
	public void addData(Data data) {
		if ( data instanceof AmazonReview) {
			fullText = ((AmazonReview) data).getReviewText();
		}
		else {
			fullText = ((AmazonQA) data).getQuestion() + " " + ((AmazonQA) data).getAnswer();
		}
		splitAndRegex(fullText);
		update(data);		
	}
	//getSize
	public int wordIndexSize() { return wordIndex.size(); }
		
	/*
	 * Splits text data and removes all non-alphanumeric data	
	 * @param fullText
	 */
	public void splitAndRegex (String fullText) {
		split = fullText.replaceAll("[^A-Za-z0-9 ]", "").toLowerCase().split("\\s+");
	}
	
	/*
	 * Update the wordIndex row data and frequency of a particular word.
	 * @param data
	 */
	public void update (Data data) {
		int count = 0;
		Data obj;
		
		HashMap<String, Integer> hh = new HashMap<>(); //To store unique words
		setMap(split); 
		for(String word : split) {
			if (hh.containsKey(word))
				continue;
			else {
				hh.put(word, 1);	
				count = mapCount.get(word); //get count of a specific word
				obj = new Data(data, count); //create new instance of Data object
				if(wordIndex.get(word) != null) {
					wordIndex.get(word).add(obj);
				}
				else {
					wordIndex.put(word, new ArrayList<Data>());
					wordIndex.get(word).add(obj);
				}
			}
		}
	}
	
	/*
	 * Create a temporary Map of word count of certain Data object
	 * @param text 
	 */
	public void setMap (String[] text) {
		mapCount.clear();
		for(String word : text) {
			if(mapCount.containsKey(word))
				mapCount.put(word, mapCount.get(word) + 1);
			else
				mapCount.put(word, 1);
		}
	}
	
	/*
	 * Searches and displays all objects that contain a particular word/term with full match.
	 * @param term
	 */
	public String search (String term) {
		StringBuffer sb = new StringBuffer();
		Boolean check = false;
		for(Map.Entry<String, ArrayList<Data>> item : wordIndex.entrySet()) {
			if(item.getKey().equals(term)) {
				check = true;
				for(Data d : item.getValue()) {
					sb.append(d.toString().replaceAll(" (?i)" + term.toLowerCase() , " <mark>" + term + "</mark> ") + "\n\n");
				}
			}
		}
		if(check == false)
			sb.append("-1");
		return sb.toString();
	}
	
	/*
	 * Sort all values within a key of wordIndex by countOfWord. Method is called after all JSON objects are read and mapped.
	 */
	public void sortWordIndex() {
		for(Map.Entry<String, ArrayList<Data>> item : wordIndex.entrySet()) {
			Collections.sort(item.getValue());
		}
	}
	/*
	 * Get # most frequent words from InvertedIndex, highest/lowest
	 * @param num, order
	 */
	public String getTopFrequency(int num, String order) {
		HashMap<String, Integer> freqCount = new HashMap<>();
		String data = "";
		String[] stopwords = ReadPage.readPage("stopwords.txt").split(",");
		ArrayList<String> stopWords = new ArrayList<>();
		for(String ss : stopwords) {
			stopWords.add(ss);
		}
		
		int count = 0;
		for(Map.Entry<String, ArrayList<Data>> item : wordIndex.entrySet()) {
			count = 0;
			if(item.getKey().length() < 4 || stopWords.contains(item.getKey())) { continue; }
			for(Data d : item.getValue()) {
				count += d.getFrequency();
			}
			freqCount.put(item.getKey(), count);
		}
		sortedByCount = sortByValue(freqCount, order);
		data += getData(sortedByCount, num);
		return data;
	}
	/*
	 * Get # most distributed words from InvertedIndex, highest/lowest
	 * @param num, order
	 */
	public String getDistribution(int num, String order) {
		String data = "";
		for(Map.Entry<String, Integer> item : sortedByCount.entrySet()) {
			sortedByDistribution.put(item.getKey(), wordIndex.get(item.getKey()).size());
		}
		sortedByDistribution = sortByValue(sortedByDistribution, order);
		data += getData(sortedByDistribution, num);
		return data;
	}
	
	/*
	 * Returns stats data with HTML tags
	 * @params map, num
	 */
	public String getData(HashMap<String, Integer> map, int num) {
		int count = 0;
		String data = "";
		for(Map.Entry<String, Integer> item : map.entrySet()) {
			if(count < num) {
				data += "<td>" + item.getKey() + "</td>\t<td>" + item.getValue() + "</td>\n\n";
				count++;
			}
			else {
				break;
			}
		}
		return data;
	}
	
    /*
     * Sorts a hashmap by values 
     */
    public static HashMap<String, Integer> sortByValue(HashMap<String, Integer> hm, String order) 
    { 
        // Create a list from elements of HashMap 
        List<Map.Entry<String, Integer> > list = 
               new LinkedList<Map.Entry<String, Integer> >(hm.entrySet()); 
  
        // Sort the list 
        Collections.sort(list, new Comparator<Map.Entry<String, Integer> >() { 
            public int compare(Map.Entry<String, Integer> o1,  
                               Map.Entry<String, Integer> o2) 
            { 
            	if(order.equals("ascending")) { return (o1.getValue()).compareTo(o2.getValue()); }
            	else { return (o2.getValue()).compareTo(o1.getValue()); }
            } 
        }); 
          
        // put data from sorted list to hashmap  
        HashMap<String, Integer> temp = new LinkedHashMap<String, Integer>(); 
        for (Map.Entry<String, Integer> aa : list) { 
            temp.put(aa.getKey(), aa.getValue()); 
        } 
        return temp; 
    } 
    
    /*
     * Gets results of each input side and applies logic/relation
     * Each operation updates a datastructure that stores all needed records
     * @params complexQuery, relation
     */
    public String getComplex(HashMap<String, String> complexQuery, String relation) {
    	StringBuffer sb = new StringBuffer();
    	ArrayList<Data> result = new ArrayList<>();

    	for(Map.Entry<String, String> item : complexQuery.entrySet()) {
    		String splitBy = findSplitBy(item.getValue());
    		setTerms(item.getKey(),item.getValue(), splitBy);
    		ArrayList<Data> rowData = new ArrayList<>();
    		rowData = getAllData(item.getValue(), splitBy);
    		result = updateList(result, rowData, relation, item.getKey());
    	}
    	if(result.size() == 0) {
    		sb.append("-1");
    	}
    	else {
    		
        	for(Data d : result) {
        		String str = highlightData(d.toString(), words);
        		sb.append(str + "\n\n");
        	}
    	}

    	return sb.toString();
    }
    
    /*
     * Highlight all required words in data
     * @params text, words
     */
    public String highlightData(String text, ArrayList<String> words) {
    	for(String word : words) {
    		text = text.replaceAll(" (?i)" + word.toLowerCase() , " <mark>" + word + "</mark>");
    	}
    	return text;
    }
    
    /*
     * To find the splitBy param for each individual input which can contain multiple words
     */
    public String findSplitBy(String input) {
    	int l1, l2;
    	l1 = input.split(",").length;
    	l2 = input.split(";").length;
    	String split = "";
    	if((l1 == 1 && l2 == 1) || (l1 > 1 && l2 == 1)) {
    		split = ",";
    	}
    	else { split = ";"; }
    	return split;
    }
    
    /*
     * For each input, gather the resulting data using its inner relation (',' or ';')
     */
    public ArrayList<Data> getAllData(String input, String splitBy) {
    	String[] terms;
    	words.clear();
    	ArrayList<Data> data = new ArrayList<>();
        terms = input.split(splitBy);
        for(String t : terms) { words.add(t); }
    	for(String term : terms) {
    		if(wordIndex.get(term) != null && splitBy.equals(",")) {
    			data = union(data, wordIndex.get(term.trim()));
    		}
    		else if(wordIndex.get(term) != null && splitBy.equals(";")) {
        			if(data.isEmpty()) { data = wordIndex.get(term); words.remove(term);}
        			else { 
        				data = intersect(data, words);
        			}
        		}
    	}
    	return data;
    }
    /*
     * Set all words of an input row of a HashMap into an ArrayList for other methods to access
     * @params key, value, split
     */
    public void setTerms(String key, String value, String split) { 
		ArrayList<String> termList = new ArrayList<>();
		for(String word : value.split(split)) {
			termList.add(word);
		}
		wordList.put(key, termList);
    }
    /*
     * Dervie final result between to the set of inputs (OR, AND, NOT_AND, AND_NOT)
     * @params dataVar, rowData, relation, term
     */
    public ArrayList<Data> updateList (ArrayList<Data> dataVar, ArrayList<Data> rowData, String relation, String term) {
    	ArrayList<Data> newResult = dataVar;
    	if(newResult.isEmpty()) { 
    		newResult.addAll(rowData); 
    		}
		else {		
			if(relation.equals("OR")) {
				newResult = union(newResult, rowData);
				words.addAll(wordList.get("0"));
				words.addAll(wordList.get("1"));
			}
			else if(relation.equals("AND")) {
				if(dataVar.size() >= rowData.size()) { newResult = intersect(newResult, wordList.get("1")); }
				else { newResult = intersect(rowData, wordList.get("0")); }
				
				words.addAll(wordList.get("0"));
				words.addAll(wordList.get("1"));
			}
			else if(relation.equals("AND_NOT")) {
				newResult = intersectNot(newResult, wordList.get("1"));
				words.addAll(wordList.get("0"));
			}
			else if(relation.equals("NOT_AND")) {
				newResult = intersectNot(rowData, wordList.get("0"));
				words.addAll(wordList.get("1"));
			}
		}
    return newResult;
    }
    
    /*
     * A U B, remove duplicate elemtns
     * @param d1, d2
     */
    public ArrayList<Data> union(ArrayList<Data> d1, ArrayList<Data> d2) {
    	ArrayList<Data> temp = d1;
    	ArrayList<Data> d3 = new ArrayList<>();
    	boolean match;
    	for(Data obj1 : d2) {
    		match = false;
    		for(Data obj2 : temp) {
    			if((obj1.toString().equals(obj2.toString()))) {
    				match = true;
    				break;
    			}
    		}
    		if (match == false) {
    			d3.add(obj1);
    		}
    	}
    	d3.addAll(d1);
    	return d3;
    }
    
    /*
     * A ^ B, in A find all rows of data containing words from wordList(B)
     */
    public ArrayList<Data> intersect(ArrayList<Data> data, ArrayList<String> term) {
    	ArrayList<Data> d3 = new ArrayList<>();
    	
    	for(Data d : data) {
    		HashMap<String, Integer> check = new HashMap<>();
    		int count = 0;
    		int counter = term.size();
    		String[] split = d.toString().replaceAll("[^A-Za-z0-9 ]", "").toLowerCase().split("\\s+");
    		for(String s : split) {
    			if(term.indexOf(s) != -1 && count < counter && !check.keySet().contains(s)) {
    				check.put(s, 1);
    				count++;
    			}
    			if(count == counter) {
    				d3.add(d);
    				break;
    			}

    		}
 	
    	}
    	return d3;
    }
    /*
     * A !^ B or A ^! B, remove all rows of data containg words from wordList(other)
     */
    public ArrayList<Data> intersectNot(ArrayList<Data> data, ArrayList<String> term) {
    	ArrayList<Data> d3 = new ArrayList<>();
    	for(Data d : data) {
    		boolean flag = false;
			String[] split = d.toString().replaceAll("[^A-Za-z0-9 ]", "").toLowerCase().split("\\s+");
			for(String s : split) {
				if(term.indexOf(s) != -1) {
					flag = true;
					break;
				}
			}
			if(flag == false) {
				d3.add(d);
			}
    	}
    	return d3;
    }

}
