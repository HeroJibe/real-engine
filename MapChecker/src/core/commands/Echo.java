package core.commands;

import core.Command;
import main.Main;

public class Echo 
	extends Command
{
	private static boolean echo;
	
	public Echo()
	{
		super("echo", "prints a message to the console");
		echo = true;
	}

	@Override
	public void execute(String[] args)
	{
		String toPrint = "";
		try
		{
			if (args[0].equals("@off"))
			{
				echo = false;
				return;
			}
			else if (args[0].equals("@on"))
			{
				echo = true;
				return;
			}
		}
		catch (Exception e)
		{
			return;
		}
		
		for (int i = 0; i < args.length; i++)
		{
			toPrint = toPrint + args[i] + " ";
		}
		Main.println(toPrint);
	}

	@Override
	public String help() 
	{
		return "echo <message> or echo @off or echo @on";
	}
	
	public static boolean shouldEcho()
	{
		return echo;
	}
}
