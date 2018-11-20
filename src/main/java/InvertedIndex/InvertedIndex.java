package InvertedIndex;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
/*
 * @author ksonar
 * Builds InvertedIndex, row data received from respective subscribers
 */
public class InvertedIndex {
	private HashMap<String, Integer> mapCount = new HashMap<String, Integer>();
	private HashMap<String, ArrayList<Data>> wordIndex = new HashMap<>();
	private String fullText;
	private String[] split;

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
	public void search (String term) {
		Boolean check = false;
		int count = 1;
		for(Map.Entry<String, ArrayList<Data>> item : wordIndex.entrySet()) {
			if(item.getKey().equals(term)) {
				check = true;
				for(Data d : item.getValue()) {
					System.out.println(count + "." + d.toString() + "\n");
					count++;
				}
			}
		}
		if(check == false)
			System.out.println("Data for key does not exist");
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
