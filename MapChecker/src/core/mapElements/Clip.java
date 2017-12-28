package core.mapElements;

import core.MapElement;
import core.MapReturn;

public class Clip
	extends MapElement
{
	public Clip()
	{
		super("Clip");
	}

	@Override
	public MapReturn check(String[] args) 
	{
		try
		{
			Double.parseDouble(args[0]);
			Double.parseDouble(args[1]);
			if (Integer.parseInt(args[2]) <= 0)
				throw new IllegalArgumentException("Width cannot be less than zero");
			if (Integer.parseInt(args[3]) <= 0)
				throw new IllegalArgumentException("Height cannot be less than zero");
			Boolean.parseBoolean(args[4]);
			try
			{
				addBrush(args[5]);
			}
			catch (Exception e) {}
			
			return new MapReturn(MapReturn.SUCCESS);
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
