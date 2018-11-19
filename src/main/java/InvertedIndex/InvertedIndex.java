package InvertedIndex;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class InvertedIndex {
	private HashMap<String, Integer> mapCount = new HashMap<String, Integer>();
	private HashMap<String, ArrayList<Data>> wordIndex = new HashMap<>();
	private String fullText;
	private String[] split;

	
	public void addData(Data d) {
		if ( d instanceof AmazonReview) {
			fullText = ((AmazonReview) d).getReviewText();
		}
		else {
			fullText = ((AmazonQA) d).getQuestion() + " " + ((AmazonQA) d).getAnswer();
		}
		splitAndRegex(fullText);
		update(d);
		//System.out.println("Updated with a new review");
		
	}
	
	public int wordIndexSize() { return wordIndex.size(); }
	public String wordIndexHead(int size) { 
		String head = ""; int count = 0;
		for(String term : wordIndex.keySet()) {
			if (count < size) {
				head += term +'\t';
				count++;
			}
			else {
				break;
			}
			
		}
		return head;
	}
	
	/*
	 * Splits text data and removes all non-alphanumeric data	
	 * @param fullText
	 */
	
	public void splitAndRegex (String fullText) {
		//System.out.println(fullText);
		split = fullText.replaceAll("[^A-Za-z0-9 ]", "").toLowerCase().split("\\s+");
		
	}
	
	/*
	 * Update the wordIndex with a MetaData object which contains a Data object and frequency of a particular word.
	 * @param a
	 */
	
	public void update (Data a) {
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
				obj = new Data(a, count); //create new instance of MetaData object

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
	
	public void displayCount() {
		for(Map.Entry<String, ArrayList<Data>> item : wordIndex.entrySet()) {
			System.out.println(item.getKey() + ":\t" + item.getValue().size());
		}
	}
	
	/*
	 * Sort all values within a key of wordIndex by countOfWord. Method is called after all JSON objects are read and mapped.
	 */

	public void sortWordIndex() {
		for(Map.Entry<String, ArrayList<Data>> item : wordIndex.entrySet()) {
			Collections.sort(item.getValue());
		}
	}

}
