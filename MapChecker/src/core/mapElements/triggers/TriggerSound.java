package core.mapElements.triggers;

import core.MapReturn;
import core.mapElements.Trigger;

public class TriggerSound 
	extends Trigger
{
	public TriggerSound()
	{
		super("TriggerSound");
	}

	@SuppressWarnings("unused")
	@Override
	protected MapReturn checkTrigger(String[] args) 
	{
		String sound = args[0];
		String type = args[1];
		if (type != null)
		{
			if (! type.equals("GENERIC") && ! type.equals("MUSIC") && ! type.equals("EFFECT"))
			{
				return new MapReturn("Unkown sound type \"" + args[1] + "\"", MapReturn.ERROR);
			}
			else if (type.equals("GENERIC"))
			{
				return new MapReturn("GENERIC sound type is dicouraged", MapReturn.WARNING);
			}
		}
		else
		{
			throw new NullPointerException("Bad sound type!");
		}
		return null;
	}
}
