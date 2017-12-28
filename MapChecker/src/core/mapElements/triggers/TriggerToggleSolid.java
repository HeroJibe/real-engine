package core.mapElements.triggers;

import core.MapElement;
import core.MapReturn;
import core.mapElements.Trigger;

public class TriggerToggleSolid 
	extends Trigger
{
	public TriggerToggleSolid()
	{
		super("TriggerToggleSolid");
	}

	@Override
	protected MapReturn checkTrigger(String[] args) 
	{
		if (! MapElement.brushExists(args[0]))
		{
			return new MapReturn("Unkown object: " + args[0], MapReturn.ERROR);
		}
		
		return new MapReturn(MapReturn.SUCCESS);
	}
}
