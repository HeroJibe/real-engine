package core.mapElements;

import core.MapElement;
import core.MapReturn;

public abstract class MapEntity 
	extends MapElement
{		
	public MapEntity()
	{
		super("Entity");
	}
	
	@Override
	public MapReturn check(String[] args)
	{
		return checkEntity(args);
	}
	
	public abstract MapReturn checkEntity(String[] args);
}
