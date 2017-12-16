package core.commands;

import core.Command;
import main.Main;

public class Clear 
	extends Command
{

	public Clear()
	{
		super("clear", "Clears the viewport.");
	}

	@Override
	public void execute(String[] args) 
	{
		Main.clear();
	}

	@Override
	public String help() 
	{
		return "clear";
	}

}
