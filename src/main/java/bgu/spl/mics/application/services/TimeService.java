package bgu.spl.mics.application.services;

import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.CRMSRunner;
import bgu.spl.mics.application.messages.TerminateBroadcast;
import bgu.spl.mics.application.messages.TickBroadcast;

import java.io.IOException;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

/**
 * TimeService is the global system timer There is only one instance of this micro-service.
 * It keeps track of the amount of ticks passed since initialization and notifies
 * all other micro-services about the current time tick using {@link TickBroadcast}.
 * This class may not hold references for objects which it is not responsible for.
 * 
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class TimeService extends MicroService{
	private int speed;
	private int duration;
	private Timer timer;


	private class Task extends TimerTask
	{
		public void run()
		{
			sendBroadcast(new TickBroadcast());
			duration--;
			if(duration<=1) {
				timer.cancel();
				timer.purge();
				System.out.println("terminated---------------------------------");
				sendBroadcast(new TerminateBroadcast());
				terminate();
				try {
					CRMSRunner.printOutput();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public TimeService(int speedRec, int durationRec) {
		super("Time service");
		speed = speedRec;
		duration = durationRec;
		timer=new Timer();
	}

	public void sendTicks(){
			TimerTask task = new Task();
			timer.scheduleAtFixedRate(task,0,speed);
	}
	@Override
	protected void initialize() {
		sendTicks();
	}

}
