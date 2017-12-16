package utilities;

import java.awt.Color;

import main.Main;

public class ClockTimer 
	implements Runnable
{
	private long timeMillis;
	private Thread myThread;
	private boolean running = false;
	
	public ClockTimer()
	{
		myThread = new Thread(this);
		timeMillis = 0;
	}
	
	public void run()
	{
		running = true;
		
		while (running)
		{
			try 
			{
				Thread.sleep(1);
			}
			catch (InterruptedException e) 
			{
				Main.println("Timer error.");
				break;
			}
			
			timeMillis++;
		}
	}
	
	public void startTimer()
	{
		try
		{
			if (! running)
				myThread.start();
		}
		catch (Exception e)
		{
		}
	}
	
	public void stopTimer()
	{
		if (running)
			running = false;
	}
	
	public long getTimeMillis()
	{
		return timeMillis;
	}
	
	public double getTimeSeconds()
	{
		return timeMillis / 1000.0;
	}
}
