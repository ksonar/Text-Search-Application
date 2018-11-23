package InvertedIndex;

/*
 * @author ksonar
 */
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
		//return "ASIN: <td>" + asin + "</td>\tQUESTION : <td>" + question + "</td>\tANSWER : <td>" + answer + "</td>"; 
		return "<td>" + asin + "</td>\t<td>" + question + "</td>\t<td>" + answer + "</td>"; 
		//return "ASIN: " + asin + "\tQUESTION : " + question + "\tANSWER : " + answer; 
	}
	
	public String getQuestion() { return question; }
	public String getAnswer() { return answer; }
}
