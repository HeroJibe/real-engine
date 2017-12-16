package core.mapElements;

import core.MapElement;
import core.MapReturn;

public class Brush 
	extends MapElement
{
	private static String[] brushNames = {"Brush", "SeperateBrush"};
	
	public Brush()
	{
		super(brushNames);
	}
	
	@Override
	@SuppressWarnings("unused")
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
			if (type.equals("DYNAMIC"))
			{
				Integer.parseInt(args[8]);
				Boolean.parseBoolean(args[9]);
				String name = args[10];
				addBrush(name);
			}
			else
			{
				try 
				{
					String name = args[8];  
					addBrush(name);
				}
				catch (Exception e ) {}
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
