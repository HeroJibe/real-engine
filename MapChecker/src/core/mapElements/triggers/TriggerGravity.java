package core.mapElements.triggers;

import core.MapReturn;
import core.mapElements.Trigger;

public class TriggerGravity 
	extends Trigger
{
	public TriggerGravity()
	{
		super("TriggerGravity");
	}

	@Override
	protected MapReturn checkTrigger(String[] args) 
	{
		Double.parseDouble(args[0]);
		return null;
	}
}
