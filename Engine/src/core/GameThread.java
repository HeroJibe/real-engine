/**
 * The GameThread is a way of making virtual
 * threads in the game.
 * 
 * @author Ethan Vrhel
 */

package core;

import main.Main;

public class GameThread 
{
	private GameRunnable r;
	private boolean running;
	private int delay;
	private int tick;
	
	public GameThread(GameRunnable r, int delay)
	{
		this.r = r;
		running = false;
		this.delay = delay;
		if (delay == -1)
			Main.getGameThreadHandler().addThread(this);
		else
			Main.getDelayedGameThreadHandler().addThread(this);
	}
	
	/**
	 * Returns the GameRunnable
	 * 
	 * @return
	 */
	public GameRunnable getRunnable()
	{
		return r;
	}
	
	/**
	 * Returns the thread's delay
	 * 
	 * @return
	 */
	public int getDelay()
	{
		return delay;
	}
	
	/**
	 * Returns the thread's tick
	 * @return
	 */
	public int getTick()
	{
		return tick;
	}
	
	/**
	 * Increments the thread's tick
	 */
	public void incrementTick()
	{
		tick++;
	}
	
	/**
	 * Resets the thread's tick
	 */
	public void resetTick()
	{
		tick = 0;
	}
	
	/**
	 * Returns whether the thread is running
	 * 
	 * @return
	 */
	public boolean running()
	{
		return running;
	}
	
	/**
	 * Starts the thread
	 */
	public void start()
	{
		running = true;
	}
	
	/**
	 * Stops the thread
	 */
	public void stop()
	{
		running = false;
	}
}
