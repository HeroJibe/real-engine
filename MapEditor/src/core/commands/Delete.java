package core.commands;

import core.Command;
import core.MapElement;
import main.Main;

public class Delete 
	extends Command
{
	public Delete()
	{
		super("delete");
	}

	@Override
	public void execute(String[] args) 
		throws IllegalArgumentException
	{
		if (args == null)
			throw new IllegalArgumentException("Invalid arguments!");
		
		if (args.length != 1)
			throw new IllegalArgumentException("Invalid arguments!");
		
		if (Main.getCurrentMap() == null)
			throw new IllegalArgumentException("No map loaded!");
		
		String name = args[0];
		MapElement e = Main.getCurrentMap().getElementByName(name);
		e.clearValues();
		e = null;
	}

	@Override
	public String getUsage()
	{
		return null;
	}
}
