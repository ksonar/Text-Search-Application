package PubSub;

import java.util.concurrent.CopyOnWriteArrayList;
/*
 * SynchronousBroker will take a single JSON object and send to all subscribers and only then take a new JSON object.
 * @author ksonar
 */
public class SyncBroker<T> implements Broker<T> {
	private CopyOnWriteArrayList<Subscriber<T>> subscriberList = new CopyOnWriteArrayList<>();
	
	/*
	 * Synchronously publish data to all subscribers
	 * @see PubSub.Broker#publish(java.lang.Object)
	 * @param item
	 */
	@Override
	public synchronized void  publish(T item) {
		for(Subscriber<T> s : subscriberList) {
			s.onEvent(item);
		}
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
	 * Shutdown broker
	 * @see PubSub.Broker#shutdown()
	 */
	@Override
	public void shutdown() {
	}
}
