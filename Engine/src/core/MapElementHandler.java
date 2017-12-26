package core;

/**
 * The <code>MapElementHandler</code> handles
 * all of the <code>MapElement</code>'s
 * 
 * @author Ethan Vrhel
 * @see MapElement
 */
public class MapElementHandler 
{
	private MapElement[] elements;
	
	public MapElementHandler(int maxElements)
	{
		elements = new MapElement[maxElements];
	}
	
	/**
	 * Adds an <code>MapElement</code>
	 * @param elem The <code>MapElement</code>
	 */
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
	
	/**
	 * Returns a <code>MapElement</code> by
	 * its name
	 * @param elem The name of the <code>MapElement</code>
	 * @return The respective <code>MapElement</code>, returns
	 * <code>null</code> if the <code>MapElement</code>
	 * is not found
	 */
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
