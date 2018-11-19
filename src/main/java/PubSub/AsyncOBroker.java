package PubSub; 

import java.util.concurrent.CopyOnWriteArrayList;

/*
 * AsynchronousOrderedBroker that will take in data without synchronization and push objects to susbcribers in an orderly manner
 * @author ksonar
 */

public class AsyncOBroker<T> implements Broker<T>, Runnable {
	private PubSubBlockingQueue<T> dataQueue;
	
	private CopyOnWriteArrayList<Subscriber<T>> subscriberList = new CopyOnWriteArrayList<>();

	private volatile boolean run = true;
	
	public AsyncOBroker(int queueSize) {
		dataQueue = new PubSubBlockingQueue<T>(queueSize);
	}

	/*
	 * Return running state of broker thread
	 */
	private boolean getRun() { return run; }
	
	/*
	 * Put an item into the BlockingQueue
	 * @see PubSub.Broker#publish(java.lang.Object)
	 */
	@Override
	public void publish(T item) {
		dataQueue.put(item);
	}

	/*
	 * Register incoming subscriber
	 * @see PubSub.Broker#subscribe(PubSub.Subscriber)
	 */
	@Override
	public void subscribe(Subscriber<T> subscriber) {
		subscriberList.add(subscriber);
	}
	
	/*
	 * Update running state to false, called after publishers have finished publishing
	 * @see PubSub.Broker#shutdown()
	 */
	@Override
	public void shutdown() {
		run = false;
	}

	/*
	 * Check running state, stop more s'onEvent' calls when run is false and size of queue is also 0
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		T item;
		//|| dataQueue.getSize() != 0)
		while((getRun())) {
			item = dataQueue.poll(1000);
			if (item != null) {
				//item = dataQueue.take();
				for(Subscriber<T> s : subscriberList) {
					s.onEvent(item);
				}
			}
		}
		//System.out.println(PubSubBlockingQueue.c);
	}
		

}
