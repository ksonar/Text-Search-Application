package InvertedIndex;

public class AmazonQA extends Data{
	private String questionType;
	private String asin;
	private String answerTime;
	private int unixTime;
	private String question;
	private String answerType;
	private String answer;
	

	
	@Override
	public String toString() {
		//return "reviewID : " + reviewerID + "\tunixReviewTime : " + unixReviewTime;
		return "{\"questionType\" : \"" + questionType + "\",\"asin\" : \"" + asin + "\",\"answerTime\" : \"" + answerTime + "\",\"unixTime\" : " + unixTime + ",\"question\" : \"" + question + "\",\"answerType\" : \"" + answerType + "\",\"answer\" : \"" + answer + "\"}";
	}
	
	public String getQuestion() { return question; }
	public String getAnswer() { return answer; }
}
