/**
 * The DelayedGameThreadHandler handles all game
 * threads with delays.
 * 
 * @author 	Ethan Vrhel
 */

package core;

public class DelayedGameThreadHandler 
	implements Runnable
{
	private GameThread[] threads;	// The GameThreads
	private int maxThreads;			// The maximum number of Threads
	private long tick;				// The thread tick
	
	public DelayedGameThreadHandler(int maxThreads)
	{
		this.maxThreads = maxThreads;
		tick = 0;
		threads = new GameThread[maxThreads];
	}
	
	/**
	 * Initializes all GameThreads
	 */
	public void init()
	{
		for (int i = 0; i < maxThreads; i++)
		{
			if (threads[i] != null)
			{
				threads[i].getRunnable().onGameInit();
			}
		}
	}
	
	@Override
	public void run()
	{
		while (true)
		{
			for (int i = 0; i < maxThreads; i++)
			{
				if (threads[i] != null)
				{
					if (threads[i].running())
					{
						threads[i].incrementTick();
						if (threads[i].getTick() >= threads[i].getDelay())
						{
							threads[i].resetTick();
							threads[i].getRunnable().onGameUpdate();
						}
					}
				}
			}
			
			Thread.yield();
			try 
			{
				Thread.sleep(1);
			}
			catch (Exception e) {}
			
			tick++;
		}
	}
	
	/**
	 * Adds a GameThread to the cache
	 * 
	 * @param r
	 */
	public void addThread(GameThread r)
	{
		for (int i = 0; i < maxThreads; i++)
		{
			if (threads[i] == null)
			{
				threads[i] = r;
				return;
			}
		}
	}
	
	/**
	 * Returns the current tick
	 * 
	 * @return
	 */
	public long getTick()
	{
		return tick;
	}
}
