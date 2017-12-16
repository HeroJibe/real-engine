package core.mapElements;

import core.MapElement;
import core.MapReturn;

public class ReflectiveBrush 
	extends MapElement
{
	public ReflectiveBrush()
	{
		super("ReflectiveBrush");
	}
	
	@Override
	@SuppressWarnings("unused")
	public MapReturn check(String[] args)
	{
		try
		{
			Double.parseDouble(args[1]);
			Double.parseDouble(args[2]);
			Integer.parseInt(args[3]);
			if (Integer.parseInt(args[4]) <= 0)
				throw new IllegalArgumentException("Width cannot be less than zero");
			if (Integer.parseInt(args[5]) <= 0)
				throw new IllegalArgumentException("Height cannot be less than zero");
			Boolean.parseBoolean(args[6]);
			Double.parseDouble(args[7]);
			Integer.parseInt(args[8]);
			Integer.parseInt(args[9]);
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
