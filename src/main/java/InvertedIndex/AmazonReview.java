package InvertedIndex;

/*
 * @author ksonar
 */
public class AmazonReview extends Data{
		private String reviewerID;
		private String asin;
		private String reviewText;
		private float overall;

		
		@Override
		public String toString() {
			return "<td>" + asin + "</td>\t<td>" + reviewerID + "</td>\t<td> " + reviewText + " </td>\t<td>" + overall + "</td>"; 
		}
		
		public String getReviewText() { return reviewText; }


}
