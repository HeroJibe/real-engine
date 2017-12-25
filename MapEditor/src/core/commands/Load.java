package core.commands;

import java.io.File;

import core.Command;
import core.Map;
import main.Main;

public class Load 
	extends Command
{
	public Load()
	{
		super("load");
	}

	@Override
	public void execute(String[] args) 
		throws IllegalArgumentException 
	{
		if (args == null)
			throw new IllegalArgumentException("Invalid arguments!");
		
		if (args.length != 1)
			throw new IllegalArgumentException("Invalid arguments!");
		
		File f = new File("..\\maps\\" + args[0] + ".map");
		
		if (! f.exists())
		{
			throw new IllegalArgumentException("Failed to find map!");
		}
		
		Main.setCurrentMap(new Map());
		Main.getCurrentMap().load(f);
		System.out.println(Main.getCurrentMap().getNumLines());
	}
	
	@Override
	public String getUsage()
	{
		return "load [map name]";
	}
}
