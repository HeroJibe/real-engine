package core;

import main.Main;

public abstract class MapElement 
{	
	private static String[] referencedBrushes;
	private static String[] referencedTriggers;
	private static String[] referencedEntities;
	
	private String name;
	private String[] names;
	
	public MapElement(String name)
	{
		this.name = name;
		Main.elementHandler.add(this);
	}
	
	public MapElement(String[] names)
	{
		this.names = names;
		Main.elementHandler.add(this);
	}
	
	public String getName()
	{
		return name;
	}
	
	public String[] getNames()
	{
		return names;
	}
	
	public boolean hasName(String name)
	{
		if (names == null || name == null)
			return false;
		
		for (int i = 0; i < names.length; i++)
		{
			if (name.equals(names[i]))
			{
				return true;
			}
		}
		return false;
	}
	
	public abstract MapReturn check(String[] args);
	
	public static void setup()
	{
		referencedBrushes = new String[32];
		referencedTriggers = new String[32];
		referencedEntities = new String[32];
	}
	
	public static void addBrush(String brush)
	{
		for (int i = 0; i < referencedBrushes.length; i++)
		{
			if (referencedBrushes[i] == null)
			{
				referencedBrushes[i] = brush;
				return;
			}
		}
	}
	
	public static boolean brushExists(String brush)
	{
		if (brush == null)
			return false;
		
		for (int i = 0; i < referencedBrushes.length; i++)
		{
			if (referencedBrushes[i] != null)
			{
				if (referencedBrushes[i].equals(brush));
					return true;
			}
		}
		return false;
	}
	
	public static void addTrigger(String trigger)
	{
		for (int i = 0; i < referencedTriggers.length; i++)
		{
			if (referencedTriggers[i] == null)
			{
				referencedTriggers[i] = trigger;
				return;
			}
		}
	}
	
	public static boolean triggerExists(String trigger)
	{
		if (trigger == null)
			return false;
		
		for (int i = 0; i < referencedTriggers.length; i++)
		{
			if (referencedTriggers[i] != null)
			{
				if (referencedTriggers[i].equals(trigger));
					return true;
			}
		}
		return false;
	}
	
	public static void addEntity(String entity)
	{
		for (int i = 0; i < referencedEntities.length; i++)
		{
			if (referencedEntities[i] == null)
			{
				referencedEntities[i] = entity;
				return;
			}
		}
	}
	
	public static boolean entityExists(String entity)
	{
		if (entity == null)
			return false;
		
		for (int i = 0; i < referencedEntities.length; i++)
		{
			if (referencedEntities[i] != null)
			{
				if (referencedEntities[i].equals(entity));
					return true;
			}
		}
		return false;
	}
}
