package core.commands;

import core.Command;
import core.MapChecker;
import main.Main;

public class Check 
	extends Command
{

	public Check() 
	{
		super("check", "Checks the currently loaded map for errors.");
	}

	@Override
	public void execute(String[] args) 
	{
		try
		{
			MapChecker.check();
		}
		catch (Exception e)
		{
			e.printStackTrace(System.out);
			Main.println("Failed to check map");
		}
	}

	@Override
	public String help() 
	{
		return "check";
	}

}
