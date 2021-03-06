package core.commands;

import core.Command;
import main.Main;

public class Configuration
	extends Command
{
	public Configuration()
	{
		super("config", "sets a program configuration setting");
	}

	@Override
	public void execute(String[] args) 
	{
		if (args == null)
		{
			printUsage();
			return;
		}
		
		if (args[0].equals("*"))
		{
			Main.println("Keyvalues:");
			Main.println("  usegui <true/false>");
		}
		else if (args[0].equals("usegui"))
		{
			if (args.length == 2)
			{
				Main.useGUI(Boolean.parseBoolean(args[1]));
			}
			else
			{
				Main.println("Invalid arguments!");
				return;
			}
		}
	}

	@Override
	public String help() 
	{
		return "config * or config <keyvalue>";
	}
}
