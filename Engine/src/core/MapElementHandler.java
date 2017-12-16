package core;

public class MapElementHandler 
{
	private MapElement[] elements;
	
	public MapElementHandler(int maxElements)
	{
		elements = new MapElement[maxElements];
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
	
	public MapElement getMapElement(String elem)
	{
		if (elem == null)
			return null;
		
		for (int i = 0; i < elements.length; i++)
		{
			if (elements[i] != null)
			{
				if (elements[i].getName().equals(elem))
				{
					return elements[i];
				}
			}
		}
		return null;
	}
}
