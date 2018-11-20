package InvertedIndex;
/*
 * @author ksonar
 */
public class Data implements Comparable{
	private Data data;
	private int frequency;
	
	public Data() {}
	
	public Data(Data d, int freq) {
		data = d;
		frequency = freq;
	}
	
	public int getFrequency() { return frequency; }
	@Override
	public String toString() {
		return data + ", frequency : " + frequency;
	}
	
	/*
	 * Sorts MetaData objects in descending order
	 * @param o
	 */
	@Override
	public int compareTo (Object o) {
		//Metadata m = (Metadata) o;
		int r1 = this.frequency;
		int r2 = ((Data) o).frequency;
		int result = (r1 < r2 ) ? 1 : (r1 == r2) ? 0 : -1;
		
		return result;
	}
	

}
