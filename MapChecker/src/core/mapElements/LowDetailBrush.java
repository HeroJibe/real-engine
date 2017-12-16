package core.mapElements;

import core.MapElement;
import core.MapReturn;

public class LowDetailBrush 
	extends MapElement
{

	public LowDetailBrush() 
	{
		super("LowDetailBrush");
	}

	public MapReturn check(String[] args)
	{
		try
		{
			String type = args[0];
			if (! type.equals("STATIC") && ! type.equals("DYNAMIC"))
			{
				throw new IllegalArgumentException("Invalid entity type \"" + type + "\"");
			}
			Double.parseDouble(args[2]);
			Double.parseDouble(args[3]);
			Integer.parseInt(args[4]);
			if (Integer.parseInt(args[5]) <= 0)
				throw new IllegalArgumentException("Width cannot be less than zero");
			if (Integer.parseInt(args[6]) <= 0)
				throw new IllegalArgumentException("Height cannot be less than zero");
			Boolean.parseBoolean(args[7]);
			Integer.parseInt(args[8]);
			if (type.equals("DYNAMIC"))
			{
				Integer.parseInt(args[9]);
				Boolean.parseBoolean(args[10]);
				String name = args[11];
				return new MapReturn("Low detail dynamic entities are not affected", MapReturn.WARNING);
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
			return new MapReturn("Unhandled exception " + e, MapReturn.ERROR);
		}
	}
}
