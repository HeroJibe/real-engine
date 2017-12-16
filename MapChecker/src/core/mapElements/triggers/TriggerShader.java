package core.mapElements.triggers;

import core.MapReturn;
import core.mapElements.Trigger;

public class TriggerShader 
	extends Trigger
{
	public TriggerShader()
	{
		super("TriggerShader");
	}

	@SuppressWarnings("unused")
	@Override
	protected MapReturn checkTrigger(String[] args)
	{
		String shader = args[0];
		return null;
	}
	
}
