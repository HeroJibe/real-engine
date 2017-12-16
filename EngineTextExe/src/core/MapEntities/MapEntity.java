package core.MapEntities;

import java.awt.Color;

import core.Trigger;
import main.Main;
import utilities.ClockTimer;

public abstract class MapEntity
	implements Runnable
{
	public static final int TIMER = 0;
	public static final int ON_TRIGGER = 1;
	public static final int ON_MAP_LOAD = 2;
	
	protected int type;
	protected boolean startOnSpawn = true;
	protected boolean isRunning = false;
	
	protected ClockTimer timer;
	protected long time;
	protected Trigger triggerRecieve;
	
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
			Main.println("waiting for " + triggerRecieve + " to be triggered");
			while (! triggerRecieve.beenTriggered() && isRunning) {System.out.print("");}
			function();
		}
		else if (type == ON_MAP_LOAD)
		{
			while (Main.loadingMap && isRunning) {System.out.print("");}
			function();
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
	
	public abstract void function();
}
