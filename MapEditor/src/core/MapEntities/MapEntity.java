package core.MapEntities;

import java.awt.Color;

import core.Entity;
import core.Trigger;
import main.Main;
import utilities.ClockTimer;

public abstract class MapEntity
	implements Runnable
{
	public static final int TIMER = 0;
	public static final int ON_TRIGGER = 1;
	public static final int ON_MAP_LOAD = 2;
	public static final int ON_PLAYER_COLLIDE = 3;
	public static final int CONTINUOUS = 4;
	
	protected int type;
	protected boolean startOnSpawn = true;
	protected boolean isRunning = false;
	
	protected ClockTimer timer;
	protected long time;
	protected Trigger triggerRecieve;
	protected int x;
	protected int y;
	protected int w;
	protected int h;
	protected Entity collisionEntity;
	
	public MapEntity(int type)
	{
		this.type = type;		
	}
	
	public MapEntity(int type, long time)
	{
		this.type = type;
		
		if (type == TIMER)
		{
			timer = new ClockTimer();
			this.time = time;
		}
	}
	
	public MapEntity(int type, Trigger triggerRecieve)
	{
		this.type = type;
		
		if (type == ON_TRIGGER)
			this.triggerRecieve = triggerRecieve;
	}
	
	public MapEntity(int type, int x, int y, int w, int h)
	{
		this.type = type;
		this.x = (int) (x * Main.resolutionScaleX);
		this.y = (int) (y * Main.resolutionScaleY);
	}
	
	public void run()
	{		
		isRunning = true;
		if (type == TIMER)
		{
			timer.startTimer();
			while (timer.getTimeMillis() <= time && isRunning) {System.out.print("");}
			timer.stopTimer();
			function();
		}
		else if (type == ON_TRIGGER)
		{
		}
		else if (type == ON_MAP_LOAD)
		{
		}
		else if (type == ON_PLAYER_COLLIDE)
		{
		}
		else if (type == CONTINUOUS)
		{
			while (Main.loadingMap == false)
			{
				function();
			}
		}
		
		isRunning = false;
	}
	
	public int getType()
	{
		return type;
	}
	
	public boolean shouldStartOnSpawn()
	{
		return startOnSpawn;
	}
	
	public boolean isRunning()
	{
		return isRunning;
	}
	
	public void stop()
	{
		isRunning = false;
	}
	
	public String toString()
	{
		return "Map Entity Type" + type;
	}
	
	public abstract void function();
}
