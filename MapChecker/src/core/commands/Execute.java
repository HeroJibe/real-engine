package core.commands;

import core.Command;
import main.Main;

public class Execute 
	extends Command
{
	public Execute()
	{
		super("exec", "Executes a configuration file.");
	}

	@Override
	public void execute(String[] args) 
	{
		if (args == null)
		{
			printUsage();
			return;
		}
		Main.configLoader.load(args[0]);
	}

	@Override
	public String help() 
	{
		return "exec <file>";
	}
	
	
}
