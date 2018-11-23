package InvertedIndex;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import Server.ReadPage;
/*
 * @author ksonar
 * Builds InvertedIndex, row data received from respective subscribers
 */
public class InvertedIndex {
	private HashMap<String, Integer> mapCount = new HashMap<String, Integer>();
	private HashMap<String, ArrayList<Data>> wordIndex = new HashMap<>();
	private String fullText;
	private String[] split;
	private HashMap<String, Integer> sortedByCount = new HashMap<>();
	private HashMap<String, Integer> sortedByDistribution = new HashMap<>();
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
		int count = 1;
		for(Map.Entry<String, ArrayList<Data>> item : wordIndex.entrySet()) {
			if(item.getKey().equals(term)) {
				check = true;
				for(Data d : item.getValue()) {
					sb.append(d.toString().replaceAll("(?i)" + term.toLowerCase(), "<mark>" + term + "</mark>") + "\n\n");
					count++;
				}
			}
		}
		if(check == false)
			sb.append("-1");
			//System.out.println("Data for key does not exist");
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
	 * Get # most frequent words from InvertedIndex
	 * @param num
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
	
	public String getDistribution(int num, String order) {
		String data = "";
		for(Map.Entry<String, Integer> item : sortedByCount.entrySet()) {
			sortedByDistribution.put(item.getKey(), wordIndex.get(item.getKey()).size());
		}
		sortedByDistribution = sortByValue(sortedByDistribution, order);
		data += getData(sortedByDistribution, num);
		return data;
	}
	
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
	
    // function to sort hashmap by values 
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

}
