package PubSub;

import java.util.ArrayList;

/*
 * Amazon Reviews data stored here
 * @author ksonar
 */

public class AmazonData {
	private String reviewerID;
	private String asin;
	private String reviewerName;
	private ArrayList<Integer> helpful;
	private String reviewText;
	private float overall;
	private String summary;
	private Double unixReviewTime;
	private String reviewTime;
	
	@Override
	public String toString() {
		//return "reviewID : " + reviewerID + "\tunixReviewTime : " + unixReviewTime;
		return "{'reviewID' : " + reviewerID + ",'asin' : " + asin + ",'reviewerName' : " + reviewerName + ",'helpful' : " + helpful + ",'reviewText' : " + reviewText + ",'overall' : " + overall + ",'summary' : " + summary + ",'unixReviewTime' : " + unixReviewTime + ",'reviewTime' : " + reviewTime + "}\n";
	}
	
	public Double getUnixReviewTime() { return unixReviewTime; }

}
