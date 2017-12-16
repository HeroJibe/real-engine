package core;

import java.awt.Color;

import main.Main;

public class GameEventHandler 
{
	private GameEvent[] events;
	private int maxEvents;
	private int numEvents;
	public long eventsExecuted;
	
	public GameEventHandler(int maxEvents)
	{
		this.maxEvents = maxEvents;
		events = new GameEvent[maxEvents];
	}
	
	public void update()
	{
		for (int i = 0; i < maxEvents; i++)
		{
			if (events[i] != null)
			{
				if (events[i].shouldRun())
				{
					events[i].update();
					eventsExecuted++;
				}
			}
		}
	}
	
	public void addEvent(GameEvent event)
	{
		for (int i = 0; i < maxEvents; i++)
		{
			if (events[i] == null)
			{
				numEvents++;
				events[i] = event;
				return;
			}
		}
		Main.println("Game event cache not big enough!", Color.RED);
	}
	
	public GameEvent getByName(String name)
	{
		for (int i = 0; i < maxEvents; i++)
		{
			if (events[i] != null)
			{
				if (events[i].getName().equals(name))
				{
					return events[i];
				}
			}
		}
		return null;
	}
	
	public int getNumEvents()
	{
		return numEvents;
	}
}
