package core.mapElements.triggers;

import core.MapElement;
import core.MapReturn;
import core.mapElements.Trigger;

public class TriggerStartTrigger 
	extends Trigger
{
	public TriggerStartTrigger()
	{
		super("TriggerStartTrigger");
	}

	@Override
	protected MapReturn checkTrigger(String[] args) 
	{
		if (! MapElement.triggerExists(args[0]))
		{
			return new MapReturn("Unkown trigger \"" + args[0] + "\"", MapReturn.ERROR);
		}
		return null;
	}
}
