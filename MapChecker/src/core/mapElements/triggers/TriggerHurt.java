package core.mapElements.triggers;

import core.MapReturn;
import core.mapElements.Trigger;

public class TriggerHurt 
	extends Trigger
{
	public TriggerHurt()
	{
		super("TriggerHurt");
	}

	@Override
	protected MapReturn checkTrigger(String[] args) 
	{
		Integer.parseInt(args[0]);
		return null;
	}
}
