package core.commands;

import java.io.File;
import java.io.IOException;

import core.Command;
import core.Map;
import main.Main;

public class New 
	extends Command
{
	public New()
	{
		super("new");
	}

	@Override
	public void execute(String[] args) throws IllegalArgumentException 
	{
		if (args == null)
			throw new IllegalArgumentException("Invalid arguments!");
		
		if (args.length != 1)
			throw new IllegalArgumentException("Invalid arguments!");
		
		File f = new File("..\\maps\\" + args[0] + ".map");
		if (f.exists())
			throw new IllegalArgumentException("Map \"" + args[0] + ".map\" already exists!");
		
		try
		{
			f.createNewFile();
		} 
		catch (IOException e) 
		{
			throw new IllegalArgumentException("Failed to create map!");
		}
		
		System.out.println("Creating...");
		Map loadedMap = new Map();
		boolean succ = loadedMap.load(f);
		if (succ)
		{
			System.out.println("Created.");
			Main.setCurrentMap(loadedMap);
		}
		else
		{
			System.out.println("Failed!");
			Main.setCurrentMap(null);
		}
	}
	
	@Override
	public String getUsage()
	{
		return "new [map name]";
	}
}
