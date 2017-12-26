package core;

import java.awt.Color;

import core.MapEntities.MapEntity;
import main.Main;

/**
 * The MapEntityHandler stores all map entities
 * and executes their threads.
 * 
 * @author Ethan Vrhel
 * @see MapEntity
 */
public class MapEntityHandler 
{
	private MapEntity[] entities;
	private int numEntities;
	private int maxEntities;
	
	public MapEntityHandler(int maxEntities)
	{
		entities = new MapEntity[maxEntities];
		numEntities = 0;
		this.maxEntities = maxEntities;
	}
	
	/**
	 * Adds an entity to the cache
	 * 
	 * @param entity A <code>MapEntity</code>
	 */
	public void addEntity(MapEntity entity)
	{
		for (int i = 0; i < maxEntities; i++)
		{
			if (entities[i] == null)
			{
				//Main.println("added " + entity);
				entities[i] = entity;
				return;
			}
		}
		
		Main.println("Map entity cache not big enough!", Color.RED);
	}
	
	/**
	 * Starts entities that should start on map spawn
	 */
	public void onSpawn()
	{
		for (int i = 0; i < maxEntities; i++)
		{
			if (entities[i] != null)
			{
				if (entities[i].shouldStartOnSpawn())
				{
					if (! entities[i].isRunning())
					{
						Thread thread = new Thread(entities[i]);
						thread.setDaemon(true);
						thread.start();
					}
				}
			}
		}
	}
	
	/**
	 * Starts all map entities
	 */
	public void startAll()
	{
		for (int i = 0; i < maxEntities; i++)
		{
			if (entities[i] != null)
			{
				if (! entities[i].isRunning())
				{
					Thread t = new Thread(entities[i]);
					entities[i].me = t;
					t.start();
				}
			}
		}
	}
	
	/**
	 * Returns a map entity by its name
	 * 
	 * @param name The name of the <code>MapEntity</code>
	 * @return The respective <code>MapEntity</code>, returns
	 * <code>null</code> if it does not exist
	 */
	public MapEntity getByName(String name)
	{
		if (name == null)
			return null;
		
		for (int i = 0; i < maxEntities; i++)
		{
			if (entities[i] != null)
			{
				if (entities[i].getName() == null)
					return null;
				
				if (entities[i].getName().equals(name))
				{
					return entities[i];
				}
			}
		}
		return null;
	}
	
	/**
	 * Stops all map entity
	 */
	public void stopAll()
	{
		for (int i = 0; i < maxEntities; i++)
		{
			if (entities[i] != null)
			{
				if (entities[i].isRunning())
				{
					entities[i].stop();
					Main.println("stopping 1", Color.BLUE);
				}
			}
		}
	}
	
	/**
	 * Removes all map entities
	 */
	public void removeAll()
	{
		for (int i = 0; i < maxEntities; i++)
		{
			entities[i] = null;
		}
	}
	
	/**
	 * Returns the number of
	 * <code>MapEntitiy</code>'s
	 * 
	 * @return The number of <code>MapEntity</code>'s
	 */
	public int getNumEntities()
	{
		return numEntities;
	}
}
