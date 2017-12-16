package core;

import java.io.File;

import core.commands.Load;
import main.Main;

public final class MapChecker
{
	private static File loaded;
	private static String[] loadedData;
	
	@SuppressWarnings("unused")
	public static void check() throws Exception
	{
		Main.println("Checking...");
		long start = System.currentTimeMillis();
		loaded = Load.getLoadedMap();
		loadedData = Load.getLoadedData();
		
		int numErrors = 0;
		String[] errors = new String[loadedData.length + 1];
		
		int numWarnings = 0;
		String[] warnings = new String[loadedData.length + 1];
		
		if (loaded == null || loadedData == null)
		{
			throw (new NullPointerException("File or map data cannot be null"));
		}
		
		boolean containsDefinition = false;
		boolean containsForceSeperate = false;
		boolean containsName = false;
		boolean containsVersion = false;
		
		for (int i = 0; i < loadedData.length; i++)
		{
			if (loadedData[i].equals("Map"))
			{
				if (containsDefinition)
				{
					numErrors++;
					errors[i] = "Map definition is defined twice.";
				}
				
				if (i != 0)
				{
					numErrors++;
					errors[i] = "Map definition is not the first line of the map file.";
				}
				
				containsDefinition = true;
			}
			else if (loadedData[i].equals("ForceSeperate"))
			{
				if (containsForceSeperate)
				{
					numErrors++;
					errors[i] = "Forced seperation is defined twice.";
				}
				
				if (i != 1)
				{
					numErrors++;
					errors[i] = "ForceSeperate is not the second line of the map file.";
				}
				
				containsForceSeperate = true;
			}
			
			String[] tokens = loadedData[i].split(" = ");
			
			if (tokens.length >= 1)
			{
				if (tokens[0].equals("Name"))
				{
					if (containsName)
					{
						numErrors++;
						errors[i] = "Map name is declared twice.";
					}
					
					containsName = true;
					if (containsForceSeperate && i == 2) {}
					else if (! containsForceSeperate && i == 1) {}
					else
					{
						numErrors++;
						errors[i] = "Map name decleration is not in correct location.";
					}
				}
				else if (tokens[0].equals("Version"))
				{
					try
					{
						int test = Integer.parseInt(tokens[1]);
						containsVersion = true;
					}
					catch (Exception e)
					{
						numErrors++;
						errors[i] = "Version is not correctly defined.";
					}
				}
				else if (tokens.length > 1)
				{
					String type = tokens[0];
					
					if (Main.elementHandler.getByName(type) != null)
					{
						tokens = tokens[1].split(" ");
						MapReturn ret = Main.elementHandler.getByName(type).check(tokens);
						
						if (ret != null)
						{
							if (ret.getMessageType() == MapReturn.ERROR)
							{
								numErrors++;
								errors[i] = ret.getMessage();
							}
							else if (ret.getMessageType() == MapReturn.WARNING)
							{
								numWarnings++;
								warnings[i] = ret.getMessage();
							}
							else if (ret.getMessageType() == MapReturn.OTHER)
							{
								String msg = "Unhandled return type (index " + i + " message " + ret.getMessage() + ")";
								Main.println(msg);
								System.out.println(msg);
							}
						}
					}
				}
			}
		}
		
		if (! containsDefinition)
		{
			numErrors++;
			errors[loadedData.length] = "Map file does not declare type Map.";
		}

		if (! containsName)
		{
			numErrors++;
			errors[loadedData.length] = "Map file does not declare its name.";
		}
		
		if (! containsVersion)
		{
			numWarnings++;
			warnings[loadedData.length] = "Map file does not contain version information.";
		}
		
		long end = System.currentTimeMillis();
		long time = end - start;
		Main.println("Done in " + time + "ms");
		
		if (numErrors == 0 && numWarnings == 0)
		{
			Main.println("No errors found.");
			String[] args = {"false"};
			Main.commandHandler.getByName("LockScroll").execute(args);
			return;
		}
		
		Main.println("");
		Main.println("Found:");
		Main.println(numErrors + " errors.");
		Main.println(numWarnings + " warnings.");
		Main.println("");
		
		for (int i = 0; i < errors.length; i++)
		{
			if (errors[i] != null)
			{
				if (! errors[i].equals(""))
				{
					Main.println("Error at " + Load.getLoadedMap().toString() + ":" + (i + 1));
					Main.println("  " + errors[i]);
					Main.println("");
				}
			}
		}
		
		for (int i = 0; i < warnings.length; i++)
		{
			if (warnings[i] != null)
			{
				if (! warnings[i].equals(""))
				{
					Main.println("Warning at " + Load.getLoadedMap().toString() + ":" + (i + 1));
					Main.println("  " + warnings[i]);
					Main.println("");
				}
			}
		}
		
		String[] args = {"false"};
		Main.commandHandler.getByName("LockScroll").execute(args);
	}
}
