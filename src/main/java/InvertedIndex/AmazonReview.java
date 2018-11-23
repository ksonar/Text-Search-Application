package InvertedIndex;

import java.util.ArrayList;
/*
 * @author ksonar
 */
public class AmazonReview extends Data{
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
			//return "ASIN : <td>" + asin + "</td>\tREVIEWER ID : <td>" + reviewerID + "</td>\tTEXT : <td>" + reviewText + "</td>\tSCORE : <td>" + overall + "</td>"; 
			return "<td>" + asin + "</td>\t<td>" + reviewerID + "</td>\t<td>" + reviewText + "</td>\t<td>" + overall + "</td>"; 
			//return "ASIN : " + asin + "\tREVIEWER ID : " + reviewerID + "\tTEXT : " + reviewText + "\tSCORE : " + overall; 
		}
		
		public Double getUnixReviewTime() { return unixReviewTime; }
		public String getReviewText() { return reviewText; }


}
