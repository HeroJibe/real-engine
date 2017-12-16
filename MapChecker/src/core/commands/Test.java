package core.commands;

import java.util.Arrays;

import core.Command;
import main.Main;

public class Test 
	extends Command
{

	public Test() 
	{
		super("test", "Tests argument recongnition.");
	}

	@Override
	public void execute(String[] args) 
	{
		Main.println("Arguments: " + Arrays.toString(args));
	}
	
	@Override
	public String help()
	{
		return "test or test <arg1> <arg2> ...";
	}
}
