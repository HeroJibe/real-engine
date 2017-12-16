package utilities;

import core.GameRunnable;
import core.GameThread;
import main.Main;

public class ClockTimer 
	implements GameRunnable, Runnable
{
	private long timeMillis;
	private GameThread myThread;
	private boolean running;
	
	public ClockTimer()
	{
		if (Main.getDelayedGameThreadHandler() != null)
			myThread = new GameThread(this, 1);
		timeMillis = 0;
		running = false;
	}
	
	public void onGameInit() {}
	
	public void onGameUpdate()
	{		
		if (! running)
		{
			myThread.stop();
			return;
		}
		timeMillis++;
	}
	
	public void run()
	{
		while (true)
		{
			timeMillis++;
			try 
			{
				Thread.sleep(1);
			}
			catch (InterruptedException e) 
			{
				e.printStackTrace();
			}
			if (! running)
			{
				return;
			}
		}
	}
	
	public void startTimer()
	{
		try
		{
			running = true;
			myThread.start();
		}
		catch (Exception e)
		{
			running = true;
			(new Thread(this)).start();
		}
	}
	
	public void stopTimer()
	{
		if (running)
			running = false;
	}
	
	public boolean running()
	{
		return running;
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
