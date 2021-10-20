package bgu.spl.mics;

import java.util.*;
import java.util.concurrent.*;


/**
 * The {@link MessageBusImpl class is the implementation of the MessageBus interface.
 * Write your implementation here!
 * Only private fields and methods can be added to this class.
 */
public class MessageBusImpl implements MessageBus {

	private static MessageBusImpl instance = null; // Singleton
	private ConcurrentHashMap<MicroService, ConcurrentLinkedQueue<Message>> registerMap;
	private ConcurrentHashMap<Class<? extends Message>, ConcurrentLinkedQueue<MicroService>> subscribeMap;
	private ConcurrentHashMap<Event, Future> futureMap;
	private static Object eventLock= new Object(); // for complete, send event and subscribe event
	private static Object broadcastLock= new Object(); // for send broadcast and subscribe broadcast


	private static class MessageBusImplHolder {
		private static MessageBusImpl instance = new MessageBusImpl();
	}

	private MessageBusImpl() { // Our Private Constructor
		registerMap = new ConcurrentHashMap<>();
		subscribeMap = new ConcurrentHashMap<>();
		futureMap = new ConcurrentHashMap<>();
	}

	public static MessageBusImpl getInstance() { // Singleton "Constructor"
		return MessageBusImplHolder.instance;
	}


	@Override
	public <T> void subscribeEvent(Class<? extends Event<T>> type, MicroService m) {
		if (!registerMap.containsKey(m))
			throw new IllegalArgumentException("The MicroService is not registered to the MessageBus");

		synchronized (eventLock) {
			if (subscribeMap.containsKey(type)) { // The Map contains the type
				if (!subscribeMap.get(type).contains(m)) // the queue doesnt contain m
					subscribeMap.get(type).add(m);
			}
			else { // The Map doesnt contain the type
				subscribeMap.put(type, new ConcurrentLinkedQueue<MicroService>());
				subscribeMap.get(type).add(m);
			}
		}
	}

	@Override
	public void subscribeBroadcast(Class<? extends Broadcast> type, MicroService m) {
		if (!registerMap.containsKey(m))
			throw new IllegalArgumentException("The MicroService is not registered to the MessageBus");

		synchronized (broadcastLock) {
			if (subscribeMap.containsKey(type)) { // The Map contains the type
				if (!subscribeMap.get(type).contains(m)) // the queue doesnt contain m
					subscribeMap.get(type).add(m);
			}
			else { // The Map doesnt contain the type
				subscribeMap.put(type, new ConcurrentLinkedQueue<MicroService>());
				subscribeMap.get(type).add(m);
			}
		}
	}


	@Override
	@SuppressWarnings("unchecked")
	public <T> void complete(Event<T> e, T result) {
		synchronized (eventLock) {
			if (futureMap.containsKey(e))
				futureMap.get(e).resolve(result); // Resolve the event
		}
	}

	public void resetMsgBus() {
		instance = null;
		registerMap = new ConcurrentHashMap<>();
		subscribeMap = new ConcurrentHashMap<>();
		futureMap = new ConcurrentHashMap<>();
	}


	@Override
	public void sendBroadcast(Broadcast b) {
		synchronized (broadcastLock) {
			if (subscribeMap.containsKey(b.getClass()) && !subscribeMap.get(b.getClass()).isEmpty()) { // if the map contains the broadcast
				for (MicroService microService : subscribeMap.get(b.getClass())) {					   // and its queue is not empty
					registerMap.get(microService).add(b);
					synchronized (registerMap.get(microService)) { // shared lock for send event, send broadcast and await message - the queue of the microservice will become the lock
						registerMap.get(microService).notifyAll();
					}
				}
			}
		}
	}


	@Override
	public  <T> Future<T> sendEvent(Event<T> e) {
		synchronized (eventLock){

			if (subscribeMap.containsKey(e.getClass()) && !subscribeMap.get(e.getClass()).isEmpty()) { // if the map contains the Event
				MicroService tmpMicroService = subscribeMap.get(e.getClass()).poll();                   // and its queue is not empty
				subscribeMap.get(e.getClass()).add(tmpMicroService); // Remove and add - RoundRobin manner.
				Future<T> tmpFuture = new Future<>();
				if (!futureMap.containsKey(e))
					futureMap.put(e, tmpFuture);
				registerMap.get(tmpMicroService).add(e);
				synchronized (registerMap.get(tmpMicroService)) { // shared lock for send event, send broadcast and await message - the queue of the microservice will become the lock
					registerMap.get(tmpMicroService).notifyAll();
				}
				return tmpFuture;
			}
		}

		return null;
	}


	@Override
	public void register(MicroService m) {
		if (!registerMap.containsKey(m))
			registerMap.put(m, new ConcurrentLinkedQueue<>());
	}

	@Override
	public void unregister(MicroService m) {
			if (registerMap.containsKey(m)) {
				while (!registerMap.get(m).isEmpty())  // Remove the MicroService from its registerMap, and dequeue its queue
					registerMap.get(m).remove();
				registerMap.remove(m);
			}
			for (ConcurrentLinkedQueue<MicroService> queue : subscribeMap.values()) {
				if (queue.contains(m))
					queue.remove(m);
			}
	}

	@Override
	public Message awaitMessage(MicroService m) {
		if (!registerMap.containsKey(m))
			throw new IllegalStateException(" The MicroService is not registered ");
		synchronized (registerMap.get(m)) { // shared lock for send event, send broadcast and await message - the queue of the microservice will become the lock
			while (registerMap.get(m).isEmpty()) {
				try {
					registerMap.get(m).wait();
				} catch (InterruptedException e) {}
			}
			return registerMap.get(m).poll();
		}
	}

	public boolean isEventcompleted(Event e) {
		futureMap.get(e).get();
		return true;
	}

}
