package core.mapElements.triggers;

import core.MapReturn;
import core.mapElements.Trigger;

public class TriggerGameEvent 
	extends Trigger
{
	public TriggerGameEvent()
	{
		super("TriggerGameEvent");
	}

	@SuppressWarnings("unused")
	@Override
	protected MapReturn checkTrigger(String[] args) 
	{
		String event = args[4];
		return null;
	}
}
