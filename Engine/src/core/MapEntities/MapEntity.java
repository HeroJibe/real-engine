/**
 * Entities which can be called in from the map
 * files.
 * 
 * @author Ethan Vrhel
 */

package core.MapEntities;

import java.awt.Color;

import core.Entity;
import core.Trigger;
import main.Main;
import utilities.ClockTimer;

public abstract class MapEntity
	implements Runnable
{
	/**
	 * Constant for a time-based triggered entity
	 */
	public static final int TIMER = 0;
	
	/**
	 * Constant for a trigger-based trigger entity
	 */
	public static final int ON_TRIGGER = 1;
	
	/**
	 * Constant for a map load triggered entity
	 */
	public static final int ON_MAP_LOAD = 2;
	
	/**
	 * Constant for a player collision triggered entity
	 */
	public static final int ON_PLAYER_COLLIDE = 3;
	
	/**
	 * Constant for a continuously updated entity
	 */
	public static final int CONTINUOUS = 4;
	
	/**
	 * The MapEntity's thread
	 */
	public Thread me;
	
	protected int type;
	protected String name;
	protected boolean startOnSpawn;
	protected boolean isRunning;
	protected boolean enabled;
	
	protected ClockTimer timer;
	protected long time;
	protected Trigger triggerRecieve;
	protected int x;
	protected int y;
	protected int w;
	protected int h;
	protected Entity collisionEntity;
	
	public MapEntity(String name, int type)
	{
		this.type = type;	
		startOnSpawn = true;
		isRunning = false;
		enabled = true;
	}
	
	public MapEntity(String name, int type, long time)
	{
		this.type = type;
		this.name = name;
		startOnSpawn = true;
		isRunning = false;
		enabled = true;
		if (type == TIMER)
		{
			timer = new ClockTimer();
			this.time = time;
		}
	}
	
	public MapEntity(String name, int type, Trigger triggerRecieve)
	{
		this.type = type;
		this.name = name;
		startOnSpawn = true;
		isRunning = false;
		enabled = true;
		if (type == ON_TRIGGER)
			this.triggerRecieve = triggerRecieve;
	}
	
	public MapEntity(String name, int type, int x, int y, int w, int h)
	{
		this.type = type;
		this.name = name;
		startOnSpawn = true;
		isRunning = false;
		enabled = true;
		this.x = (int) (x * Main.resolutionScaleX);
		this.y = (int) (y * Main.resolutionScaleY);
	}
	
	public void run()
	{		
		if (! shouldStartThread())
			return;
		
		try
		{
			while (Main.loadingMap)
			{
				Thread.yield();
			}
			
			isRunning = true;
			if (type == TIMER)
			{
				timer.startTimer();
				while (timer.getTimeMillis() <= time && isRunning) {Thread.yield();}
				timer.stopTimer();
				if (isRunning)
					function();
				else
					return;
			}
			else if (type == ON_TRIGGER)
			{
				//Main.println("waiting for " + triggerRecieve + " to be triggered");
				while (! triggerRecieve.beenTriggered() && isRunning) {Thread.yield();}
				if (isRunning)
					function();
				else
					return;
			}
			else if (type == ON_MAP_LOAD)
			{
				while (Main.loadingMap && isRunning && isRunning) {Thread.yield();}
				if (isRunning)
					function();
				else
					return;
			}
			else if (type == ON_PLAYER_COLLIDE)
			{
				boolean collided = false;
				Entity e = collisionEntity;
				//Main.println(e.toString());
				while (! collided && isRunning) 
				{
					try
					{
						collided = Main.getPlayer().getPlayerEntity().hasCollidedIgnoreSolid(e);
						//Main.println(Main.getPlayer().getPlayerEntity().gasCl + "");
					}
					catch (Exception e1) 
					{
					}
				}
				//Main.println("triggered");
				if (isRunning)
					function();
				else
					return;
			}
			else if (type == CONTINUOUS)
			{
				//Main.println("in");
				while (isRunning)
				{
					if (enabled)
						function();
					Thread.yield();
				}
			}
			isRunning = false;
		}
		catch (Exception e) {}
		Main.println("stopped", Color.BLUE);
	}
	
	protected boolean shouldStartThread()
	{
		return true;
	}
	
	/**
	 * Returns the MapEntity's type
	 * 
	 * @return The MapEntity's type
	 */
	public int getType()
	{
		return type;
	}
	
	/**
	 * Returns whether the MapEntity should start on spawn
	 * 
	 * @return Whether the MapEntity starts on spawn
	 */
	public boolean shouldStartOnSpawn()
	{
		return startOnSpawn;
	}
	
	/**
	 * Returns whether the MapEntity is running
	 * 
	 * @return Whether the MapEntity is running
	 */
	public boolean isRunning()
	{
		return isRunning;
	}
	
	/**
	 * Stops the map entity
	 */
	public void stop()
	{
		isRunning = false;
	}
	
	@Override
	public String toString()
	{
		return name + " type " + type;
	}
	
	/**
	 * Sets whether the MapEntity is enabled
	 * 
	 * @param enabled
	 */
	public void setEnabled(boolean enabled)
	{
		this.enabled = enabled;
	}
	
	/**
	 * Returns whether the MapEntity is enabled
	 * 
	 * @return
	 */
	public boolean isEnabled()
	{
		return enabled;
	}
	
	/**
	 * Returns the MapEntity's name
	 * 
	 * @return
	 */
	public String getName()
	{
		return name;
	}
	
	public abstract void function();
}
