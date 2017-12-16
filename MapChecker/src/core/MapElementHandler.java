package core;

import main.Main;

public class MapElementHandler
{
	public static final int MAX_ELEMENTS = 32;
	
	private MapElement[] elements;
	
	public MapElementHandler()
	{
		elements = new MapElement[MAX_ELEMENTS];
	}
	
	public void add(MapElement elem)
	{
		for (int i = 0; i < elements.length; i++)
		{
			if (elements[i] == null)
			{
				elements[i] = elem;
				return;
			}
		}
	}
	
	public MapElement getByName(String name)
	{
		if (name == null)
			return null;
		
		for (int i = 0; i < elements.length; i++)
		{
			if (elements[i] != null)
			{
				if (elements[i].hasName(name))
					return elements[i];
				else if (elements[i].getName() != null)
				{
					if (elements[i].getName().equals(name))
						return elements[i];
				}
			}
		}
		
		return null;
	}
}
