package core.mapElements.triggers;

import core.MapReturn;
import core.mapElements.Trigger;

public class TriggerLoad 
	extends Trigger
{
	public TriggerLoad()
	{
		super("TriggerLoad");
	}

	@SuppressWarnings("unused")
	@Override
	protected MapReturn checkTrigger(String[] args) 
	{
		String mapname = args[0];
		Double.parseDouble(args[1]);
		Double.parseDouble(args[2]);
		return null;
	}
}
