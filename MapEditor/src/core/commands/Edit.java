package core.commands;

import core.Command;

public class Edit 
	extends Command
{
	public Edit()
	{
		super("edit");
	}

	@Override
	public void execute(String[] args) throws IllegalArgumentException 
	{
		if (args == null)
			throw new IllegalArgumentException("Invalid arguments!");
		
		if (args.length != 3)
			throw new IllegalArgumentException("Invalid arguments!");
		
		
	}

	@Override
	public String getUsage() 
	{
		return "edit [friendly name] [value name] [new value]";
	}
}
