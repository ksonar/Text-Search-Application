package PubSub;
/*
 * Implementation of a BlockingQueue, takes care of pushing and pooping datsa elements in a systematic and synchronized manner.
 * @author ksonar
 */
public class PubSubBlockingQueue<T> {
	private T[] items;
	private int start;
	private int end;
	private int size;

	public PubSubBlockingQueue(int size) {
		this.items = (T[]) new Object[size];
		this.start = 0;
		this.end = -1;
		this.size = 0;
	}

	/*
	 * Inserts a data object into queue, checks if size is full and notifies appropriately
	 * @param item
	 */
	public synchronized void put(T item) {
		
		while(size == items.length) {
			try {
				this.wait();
			} catch (InterruptedException e) {
				
			}
		}				
		int next = (end+1)%items.length;
		items[next] = item;
		end = next;		
		size++;
		if(size == 1) {
			this.notifyAll();
		}
		
	}
	
	/*
	 * Checks for empty queue size and returns null with a timeout. If element present, then it will pop and return.
	 */
	public synchronized T poll(int timeout) {
		
		if(size == 0) {
			try {
				long time = System.currentTimeMillis();
				this.wait(timeout);
				long elapsedTime = System.currentTimeMillis() - time;
				while(size == 0 && elapsedTime < timeout) {
					System.out.println(elapsedTime);
					long time1 = System.currentTimeMillis();
					this.wait(timeout - elapsedTime);
					if(size != 0) {
						break;
					}
					elapsedTime += (System.currentTimeMillis() - time1);
					System.out.println(elapsedTime+ "\n......\n");
				}
				
				if(size == 0) {
					return null;
				}
				
				}
			 catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		T item = items[start];
		start = (start+1)%items.length;
		size--;
		if(size == items.length-1) {
			this.notifyAll();
		}
		return item;
	}
	
	/*
	 * Returns true if size is 0
	 */
	public synchronized boolean isEmpty() {
		return size == 0;
	}
	
	/*
	 * Return size of BlockingQueue
	 */
	public synchronized int getSize() {
		return size;
	}

}
