package core.mapElements.triggers;

import core.MapReturn;
import core.mapElements.Trigger;

public class TriggerToggleEntity 
	extends Trigger
{
	public TriggerToggleEntity()
	{
		super("TriggerToggleEntity");
	}

	@Override
	protected MapReturn checkTrigger(String[] args) 
	{
		String me = args[0];
		Boolean.parseBoolean(args[8]);
		return null;
	}
}
