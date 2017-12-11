package edu.umbc.bft.util;

import java.util.TimerTask;

public abstract class Timeout extends TimerTask		{
	
	private volatile boolean started, done;
	private long timeoutInMilli;
	
	public Timeout(long timeoutInMillis) {
		super();
		this.started = this.done = false;
		this.timeoutInMilli = timeoutInMillis;
	}//end of constructor
	
	
	public long getTimeoutInMillis() {
		return this.timeoutInMilli;
	}
	public boolean hasStarted()		{
		return this.started;
	}
	public boolean isDone()		{
		return this.done;
	}
	
	/** 
	 * ~~~Executes this method when Timer Expires~~~
	 * In case the processing is heavy, do not use this implementation [Check `ScheduledThreadPoolExecutor`]
	 * Timer class is single threaded - if once thread is delayed, all other waiting threads will be delayed
	 **/
	public final void run()	{
		this.started = true;
		try	{
			this.onTimeout();
		}catch(Exception e)	{
			Logger.sysLog(LogValues.error, this.getClass().getName(), " Unable to start timer  \n"+ Logger.getStack(e) );
		}finally{
			this.done = true;
		}
	}//end of thread
	
	@Override
	public final boolean cancel() {
		Logger.sysLog(LogValues.info, this.getClass().getName(), this.getClass().getSimpleName() +" Timer cancelled " );
		this.done = true;
		return super.cancel();
	}//End Of Method

	public abstract void onTimeout();	
	
}
