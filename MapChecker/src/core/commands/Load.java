package core.commands;

import java.io.File;
import java.util.Scanner;

import core.Command;
import main.Main;

public class Load 
	extends Command
{
	private static File loadedMap;
	private static boolean loaded;
	private static String[] loadedData;
	
	public Load() 
	{
		super("load", "Loads a map file into memory.");
		loadedMap = null;
	}

	@Override
	public void execute(String[] args) 
	{
		if (args != null)
		{			
			File mapFile = new File("..\\maps\\" + args[0]);
			if (! mapFile.exists())
				mapFile = new File("maps\\" + args[0]);
			
			if (mapFile.equals(loadedMap) && loaded)
			{
				Main.println("This map is already loaded.");
				return;
			}
			
			if (loaded)
			{
				Main.println("A map is already loaded");
				return;
			}
			
			if (mapFile.exists())
			{
				loadedMap = mapFile;
				try
				{
					Scanner in = new Scanner(loadedMap);
					int lines = 0;
					while (in.hasNextLine())
					{
						in.nextLine();
						lines++;
					}
					in.close();
					
					loadedData = new String[lines];
					in = new Scanner(loadedMap);
					for (int i = 0; i < lines; i++)
					{
						loadedData[i] = in.nextLine();
					}
					in.close();
				}
				catch (Exception e) 
				{
					e.printStackTrace(System.out);
				}
				
				
				loaded = true;
				Main.println("Loaded: " + mapFile.toString() + " (" + (mapFile.length() / 1024) + "KB)");
			}
			else
			{
				Main.println("Map \"" + args[0] + "\" does not exist");
			}
		}
		else
		{
			printUsage();
		}
	}
	
	@Override
	public String help()
	{
		return "load <map name>";
	}
	
	public static File getLoadedMap()
	{
		return loadedMap;
	}
	
	public static String[] getLoadedData()
	{
		return loadedData;
	}
	
	public static void setLoaded(boolean loaded)
	{
		Load.loaded = loaded;
	}
	
	public static boolean loaded()
	{
		return loaded;
	}
}
