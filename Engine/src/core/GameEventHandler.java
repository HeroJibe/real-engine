/**
 * The GameEventHandler class store all of the
 * GameEvents.
 * 
 * @author Ethan Vrhel
 */

package core;

import java.awt.Color;

import main.Main;

public class GameEventHandler 
{
	private GameEvent[] events;
	private int maxEvents;
	private int numEvents;
	
	/**
	 * The amount of events executed
	 */
	public long eventsExecuted;
	
	public GameEventHandler(int maxEvents)
	{
		this.maxEvents = maxEvents;
		events = new GameEvent[maxEvents];
	}
	
	/**
	 * Updates all events
	 */
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
	
	/**
	 * Adds a GameEvent
	 * 
	 * @param event
	 */
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
	
	/**
	 * Returns a GameEvent by its name
	 * 
	 * @param name
	 * @return
	 */
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
	
	/**
	 * Returns the number of GameEvents
	 * 
	 * @return
	 */
	public int getNumEvents()
	{
		return numEvents;
	}
}
