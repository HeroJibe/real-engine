package core.commands;

import core.Command;
import core.MapElement;
import core.mapElements.*;
import main.Main;

public class Add 
	extends Command
{
	public Add()
	{
		super("add");
	}

	@Override
	public void execute(String[] args) 
			throws IllegalArgumentException 
	{		
		if (args == null)
			throw new IllegalArgumentException("Invalid arguments!");
		
		if (args.length != 2)
			throw new IllegalArgumentException("Invalid arguments!");
		
		if (Main.getCurrentMap() == null)
			throw new IllegalArgumentException("No map loaded!");
		
		MapElement elem = Main.getMapElementHandler().getElement(args[0]);
		MapElement toAdd = null;
		if (elem instanceof Brush)
		{
			toAdd = new Brush(args[1]);
		}
		
		if (elem == null)
			throw new IllegalArgumentException("Map element does not exist!");
		
		Main.getCurrentMap().add(toAdd, args[1]);
	}
	
	@Override
	public String getUsage()
	{
		return "add [map element] [friendly name]";
	}
}