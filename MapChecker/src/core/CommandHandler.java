package core;

import main.Main;

public class CommandHandler 
{
	public static final int MAX_COMMANDS = 32;
	
	private Command[] commands;
	
	public CommandHandler()
	{
		commands = new Command[MAX_COMMANDS];
	}
	
	public void addCommand(Command command)
	{
		if (command == null)
		{
			Main.println("Failed adding command (Command is null)");
			return;
		}
		
		for (int i = 0; i < MAX_COMMANDS; i++)
		{
			if (commands[i] == null)
			{
				commands[i] = command;
				return;
			}
		}
		Main.println("Failed adding command: " + command.getName() + " (Not enough storage)");
	}
	
	public Command getCommandAt(int i)
	{
		return commands[i];
	}
	
	public Command getByName(String name)
	{
		if (name == null)
			return null;
		
		for (int i = 0; i < MAX_COMMANDS; i++)
		{
			if (commands[i] != null)
			{
				if (commands[i].getName().equalsIgnoreCase(name))
				{
					return commands[i];
				}
			}
		}
		
		return null;
	}
	
	public String[] getArgumentsFromString(String str)
	{
		String[] raw = str.split(" ");
		String[] args = null;
		if (raw.length > 1)
		{
			args = new String[raw.length - 1];
			for (int i = 0; i < args.length; i++)
			{
				args[i] = raw[i + 1];
			}
		}
		return args;
	}
	
	public String getCommandName(String str)
	{
		if (str == null)
			return null;
		String[] raw = str.split(" ");
		return raw[0];
	}
}
