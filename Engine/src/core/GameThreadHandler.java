/**
 * The GameThreadHandler handles all game 
 * threads with no delays
 * 
 * @author Ethan Vrhel
 */

package core;

public class GameThreadHandler
	implements Runnable
{
	private GameThread[] threads;
	private int maxThreads;
	
	public GameThreadHandler(int maxThreads)
	{
		this.maxThreads = maxThreads;
		threads = new GameThread[maxThreads];
	}
	
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
	
	public void run()
	{
		while (true)
		{
			for (int i = 0; i < maxThreads; i++)
			{
				if (threads[i] != null)
				{
					if (threads[i].running())
						threads[i].getRunnable().onGameUpdate();
				}
			}
			//Thread.yield();
		}
	}
	
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
}
