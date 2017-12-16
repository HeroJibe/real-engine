package core.commands;

import core.Command;
import main.Main;

public class Unload 
	extends Command
{

	public Unload()
	{
		super("unload", "Unloads the current map file.");
	}

	@Override
	public void execute(String[] args) 
	{
		if (Load.getLoadedMap() == null || ! Load.loaded())
		{
			Main.println("No map is loaded.");
			return;
		}
		
		Load.setLoaded(false);
		Main.println("Unloaded: " + Load.getLoadedMap().toString());
	}
	
	@Override
	public String help()
	{
		return "unload";
	}
}
