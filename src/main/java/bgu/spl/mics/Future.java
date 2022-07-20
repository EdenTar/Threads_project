package bgu.spl.mics;

import java.util.concurrent.TimeUnit;

/**
 * A Future object represents a promised result - an object that will
 * eventually be resolved to hold a result of some operation. The class allows
 * Retrieving the result once it is available.
 * 
 * Only private methods may be added to this class.
 * No public constructor is allowed except for the empty constructor.
 */
public class Future<T> {

	private T result;
	private boolean isResolve;
	/**
	 * This should be the the only public constructor in this class.
	 */
	public Future() {
		//TODO: implement this
		result=null;
		isResolve = false;
	}
	
	/**
     * retrieves the result the Future object holds if it has been resolved.
     * This is a blocking method! It waits for the computation in case it has
     * not been completed.
     * <p>
     * @return return the result of type T if it is available, if not wait until it is available.
     * @post future has resolved
     */
	public synchronized T get() {
		while (!isResolve)
		{
			try {
				this.wait();
			} catch (InterruptedException ignored) {}
		}
		System.out.println("finish get");
		return result;
	}
	
	/**
     * Resolves the result of this Future object.
	 * @pre result!=null
	 * @post the method sets the model result to the received argument
     */
	public synchronized void resolve (T result) {
		this.result=result;
		this.notifyAll();
		isResolve = true;

		System.out.println("--------resolve--------"+ Thread.currentThread());
	}
	
	/**
     * @return true if this object has been resolved, false otherwise
     */
	public boolean isDone() {
		return isResolve;
	}
	
	/**
     * retrieves the result the Future object holds if it has been resolved,
     * This method is non-blocking, it has a limited amount of time determined
     * by {@code timeout}
     * <p>
     * @param unit		the {@link TimeUnit} time units to wait.
     * @return return the result of type T if it is available, if not, 
     * 	       wait for {@code timeout} TimeUnits {@code unit}. If time has
     *         elapsed, return null.
	 * @pre timeout>0 & unit!=null
	 *
     */
	public T get(long timeout, TimeUnit unit) {
		TimeUnit time = TimeUnit.MILLISECONDS;
		while (!isResolve)
		{
			try {
				this.wait(time.convert(timeout, unit));
			} catch (InterruptedException ignored) {}
		}
		notifyAll();
		return result;
	}

}
