package core.mapElements.triggers;

import core.MapElement;
import core.MapReturn;
import core.mapElements.Trigger;

public class TriggerMove
	extends Trigger 
{
	public TriggerMove()
	{
		super("TriggerMove");
	}
	
	protected MapReturn checkTrigger(String[] args) 
	{
		try
		{
			if (! MapElement.brushExists(args[0]))
			{
				throw new NullPointerException(args[0]);
			}
			
			Integer.parseInt(args[1]);
			Integer.parseInt(args[2]);
			Integer.parseInt(args[3]);
			
			return null;
		}
		catch (NullPointerException e)
		{
			return new MapReturn("Unkown object: " + e.getMessage(), MapReturn.ERROR);
		}
	}
}
