package InvertedIndex;

/*
 * @author ksonar
 */
public class AmazonQA extends Data{
	private String asin;
	private String question;
	private String answer;
	

	
	@Override
	public String toString() {
		return "<td>" + asin + "</td>\t<td> " + question + " </td>\t<td> " + answer + " </td>"; 
	}
	
	public String getQuestion() { return question; }
	public String getAnswer() { return answer; }
}
