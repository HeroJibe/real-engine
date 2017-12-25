package core;

public class MapElementHandler 
{
	public static final int MAX_ELEMENTS = 16;
	
	private MapElement[] elems;
	
	public MapElementHandler()
	{
		elems = new MapElement[MAX_ELEMENTS];
	}
	
	public void add(MapElement elem)
	{
		for (int i = 0; i < elems.length; i++)
		{
			if (elems[i] == null)
			{
				elems[i] = elem;
				return;
			}
		}
	}
	
	public MapElement getElement(String name)
	{
		if (name == null)
			return null;
		
		for (int i = 0; i < elems.length; i++)
		{
			if (elems[i] != null)
			{
				if (elems[i].getName().equalsIgnoreCase(name))
				{
					return elems[i];
				}
			}
		}
		return null;
	}
	
	public Class<?> getElementClass(String name)
	{
		if (name == null)
			return null;
		
		for (int i = 0; i < elems.length; i++)
		{
			if (elems[i] != null)
			{
				if (elems[i].getName().equalsIgnoreCase(name))
				{
					return elems[i].getClass();
				}
			}
		}
		return null;
	}
}
