package core.commands;

import core.Command;
import main.Main;

public class Exit 
	extends Command
{
	public Exit() 
	{
		super("exit", "Exits the program.");
	}

	@Override
	public void execute(String[] args) 
	{
		Main.println("exiting...");
		Main.exit(1);
	}
	
	@Override
	public String help()
	{
		return "exit";
	}
}
