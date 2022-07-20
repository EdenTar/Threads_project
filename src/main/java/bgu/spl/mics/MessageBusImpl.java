package bgu.spl.mics;
import bgu.spl.mics.application.objects.Student;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * The {@link MessageBusImpl class is the implementation of the MessageBus interface.
 * Write your implementation here!
 * Only private fields and methods can be added to this class.
 */
public class MessageBusImpl implements MessageBus {

	private static class SingletonHolder{
		private static MessageBusImpl instance=new MessageBusImpl();
	}
	private ConcurrentHashMap<MicroService, LinkedBlockingQueue<Message>> microServices ;
	private ConcurrentHashMap<Class<? extends Message>, LinkedBlockingQueue<MicroService>> subscribersToMessages ;
	//private ConcurrentHashMap<Student,Future> futures ;


	private MessageBusImpl(){

		microServices= new ConcurrentHashMap<>();
		subscribersToMessages=new ConcurrentHashMap<>();
		//futures=new ConcurrentHashMap<>();
	}

	public static MessageBus getInstance() {
		return SingletonHolder.instance;
	}

	@Override
	public  <T> void subscribeEvent(Class<? extends Event<T>> type, MicroService m) {
		// TODO Auto-generated method stub
		//is register
		if (isMicroServiceRegistered(m)) {
			//if not exist - create with new linked list
			subscribersToMessages.computeIfAbsent(type, k -> new LinkedBlockingQueue<MicroService>());
			//add microservice to linked list
			subscribersToMessages.get(type).add(m);
		}
	}
	@Override
	public <T> boolean isSubscribeEvent(Class<? extends Event<T>> type, MicroService m){
		//needs to be implemented
		if (!isMicroServiceRegistered(m))
			return  false;
		LinkedBlockingQueue<MicroService> linkedList = subscribersToMessages.get(type);
		if(linkedList==null)
			return false;
		return linkedList.contains(m);
	}

	@Override
	public <T> boolean isSendEvent(Event<T> e) {
		return true;
	}

	@Override
	public void subscribeBroadcast(Class<? extends Broadcast> type, MicroService m) {
		if (isMicroServiceRegistered(m)) {
			subscribersToMessages.computeIfAbsent(type, k -> new LinkedBlockingQueue<MicroService>());
			subscribersToMessages.get(type).add(m);
		}
		
	}

	@Override
	public <T> void complete(Event<T> e, T result) {
		e.getFuture().resolve(result);
	}

	@Override
	public void sendBroadcast(Broadcast b) {
		if (subscribersToMessages==null )
			return;
		if (subscribersToMessages.get(b.getClass())!=null) {
			for (MicroService m : subscribersToMessages.get(b.getClass())) {
					microServices.get(m).add(b);
			}
		}

	}
	@Override
	public boolean IsBroadcastSent(Broadcast b){
		//implement this
		return true;
	}
	
	@Override
	public <T> Future<T> sendEvent(Event<T> e) {
		MicroService first=null;
			try{
			first = subscribersToMessages.get(e.getClass()).take();}
			catch (InterruptedException exception){}
			catch (Exception exception){
				subscribersToMessages.put(e.getClass(),new LinkedBlockingQueue<>());
				try{
					first = subscribersToMessages.get(e.getClass()).take();}
				catch (InterruptedException exception2){}
			}

		try{
			subscribersToMessages.get(e.getClass()).put(first);}catch (InterruptedException exception){}
		microServices.get(first).add(e);
		return e.getFuture();
	}

	@Override
	public  void register(MicroService m) {
		if(!microServices.containsKey(m)) {
			LinkedBlockingQueue<Message> queue=new LinkedBlockingQueue<>();
			microServices.put(m, queue);
		}
	}
	@Override
	public  boolean isMicroServiceRegistered(MicroService m){
		return microServices.get(m) != null;
	}

	@Override
	public  void unregister(MicroService m) {

		for(LinkedBlockingQueue microServicesLinkedQueue : subscribersToMessages.values()){
			for(Object microService : microServicesLinkedQueue)	{
				if (microService==m) {
					microServicesLinkedQueue.remove(m);
				}
			}
		}
		microServices.remove(m);
	}

	@Override
	public Message awaitMessage(MicroService m) throws InterruptedException {

		if (!microServices.containsKey(m))
			throw new IllegalStateException("micro service is not registered");
		Message message = microServices.get(m).take();
		return message;
	}

	@Override
	public boolean isServiceQueueEmpty(MicroService m){
		//needs to be implemented
		return true;

	}

	

}
