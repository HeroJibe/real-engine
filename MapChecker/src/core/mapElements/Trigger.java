package core.mapElements;

import core.MapElement;
import core.MapReturn;

public abstract class Trigger 
	extends MapElement
{
	public static final int MAX_TRIGGERS = 32;
	private static Trigger[] triggers = new Trigger[MAX_TRIGGERS];
	
	private String triggerType;
	
	public Trigger(String triggerType)
	{
		super("Trigger");
		this.triggerType = triggerType;
		addTriggerType(this);
	}
	
	@Override
	public MapReturn check(String[] args)
	{
		try
		{
			String type = args[0];
			if (! type.equals("STATIC") && ! type.equals("DYNAMIC"))
			{
				throw new IllegalArgumentException("Invalid entity type \"" + type + "\"");
			}
			String triggerType = args[1];
			Double.parseDouble(args[3]);
			Double.parseDouble(args[4]);
			if (Integer.parseInt(args[5]) <= 0)
				throw new IllegalArgumentException("Width cannot be less than zero");
			if (Integer.parseInt(args[6]) <= 0)
				throw new IllegalArgumentException("Height cannot be less than zero");
			String[] triggerArgs = new String[args.length - 6 - 1];
			for (int i = 0; i < triggerArgs.length; i++)
			{
				triggerArgs[i] = args[i + 7];
			}
			
			Trigger myTrigger = getByName(triggerType);
			if (myTrigger == null)
			{
				throw new IllegalArgumentException("Unknown trigger type \"" + triggerType + "\"");
			}
			else
			{
				MapReturn ret = myTrigger.checkTrigger(triggerArgs);
				if (ret != null)
				{
					if (ret.getMessageType() == MapReturn.ERROR)
					{
						return ret;
					}
				}
			}
			
			String name = args[args.length - 1];
			addTrigger(name);
			
			if (type.equals("DYNAMIC"))
			{
				return new MapReturn("Dynamic trigger brushes are discouraged", MapReturn.WARNING);
			}
			
			return null;
		}
		catch (ArrayIndexOutOfBoundsException e)
		{
			return new MapReturn("Invalid number of arguments", MapReturn.ERROR);
		}
		catch (NumberFormatException e)
		{
			return new MapReturn("Parsing error", MapReturn.ERROR);
		}
		catch (IllegalArgumentException e)
		{
			return new MapReturn(e.getMessage(), MapReturn.ERROR);
		}
		catch (Exception e)
		{
			e.printStackTrace(System.out);
			return new MapReturn("Unhandled exception " + e, MapReturn.ERROR);
		}
	}
	
	public String getTriggerType()
	{
		return triggerType;
	}
	
	protected abstract MapReturn checkTrigger(String[] args);
	
	private static void addTriggerType(Trigger trigger)
	{
		for (int i = 0; i < triggers.length; i++)
		{
			if (triggers[i] == null)
			{
				triggers[i] = trigger;
				return;
			}
		}
	}
	
	private static Trigger getByName(String name)
	{
		for (int i = 0; i < triggers.length; i++)
		{
			if (triggers[i] != null)
				if (triggers[i].getName() != null)
					if (triggers[i].getTriggerType().equals(name))
						return triggers[i];
		}
		return null;
	}
}
