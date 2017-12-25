package core.commands;

import core.Command;

public class Exit 
	extends Command
{
	public Exit()
	{
		super("Exit");
	}

	@Override
	public void execute(String[] args) throws IllegalArgumentException  
	{
		System.exit(0);
	}
	
	@Override
	public String getUsage()
	{
		return "exit";
	}
}
