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
			//return "reviewID : " + reviewerID + "\tunixReviewTime : " + unixReviewTime;
			return "reviewerID : " + reviewerID + ",asin : " + asin + ",reviewerName : " + reviewerName + ",helpful : " + helpful + ",reviewText : " + reviewText + ",overall : " + overall + ",summary : " + summary + ",unixReviewTime : " + unixReviewTime + ",reviewTime : " + reviewTime;
		}
		
		public Double getUnixReviewTime() { return unixReviewTime; }
		public String getReviewText() { return reviewText; }


}
