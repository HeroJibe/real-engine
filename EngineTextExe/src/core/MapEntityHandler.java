package core;

import java.awt.Color;

import core.MapEntities.MapEntity;
import main.Main;

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
	
	public void addEntity(MapEntity entity)
	{
		for (int i = 0; i < maxEntities; i++)
		{
			if (entities[i] == null)
			{
				entities[i] = entity;
				return;
			}
		}
		
		Main.println("Map entity cache not big enough!", Color.RED);
	}
	
	public void onSpawn()
	{
		for (int i = 0; i < maxEntities; i++)
		{
			if (entities[i] != null)
			{
				if (entities[i].shouldStartOnSpawn())
				{
					if (! entities[i].isRunning())
						(new Thread(entities[i])).start();
				}
			}
		}
	}
	
	public void startAll()
	{
		for (int i = 0; i < maxEntities; i++)
		{
			if (entities[i] != null)
			{
				if (! entities[i].isRunning())
					(new Thread(entities[i])).start();
			}
		}
	}
	
	public void stopAll()
	{
		for (int i = 0; i < maxEntities; i++)
		{
			if (entities[i] != null)
			{
				if (entities[i].isRunning())
					entities[i].stop();
			}
		}
	}
	
	public int getNumEntities()
	{
		return numEntities;
	}
}
