package core.commands;

import core.Command;
import main.Main;

public class FunCommand
	extends Command
{

	public FunCommand() 
	{
		super("bobby_shmurda", "Bobby bitch");
	}

	@Override
	public void execute(String[] args) 
	{
		Main.println("I've been selling crack since like the fifth grade.");
	}

	@Override
	public String help()
	{
		return "bobby_shmurda";
	}

}
