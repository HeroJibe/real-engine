package core.commands;

import core.Command;
import core.CommandHandler;
import main.Main;

public class Help 
	extends Command
{

	public Help() 
	{
		super("help", "Displays all commands and their function.");
	}

	@Override
	public void execute(String[] args) 
	{
		if (args != null)
		{
			if (! args[0].equals("*"))
			{
				Command cmd = Main.commandHandler.getByName(args[0]);
				if (cmd != null)
				{
					cmd.printHelpMessage();
				}
				else
				{
					println("Could not find command: " + args[0]);
				}
				return;
			}
			else
			{
				println("All commands:");
				for (int i = 0; i < CommandHandler.MAX_COMMANDS; i++)
				{
					if (Main.commandHandler.getCommandAt(i) != null)
					{
						println("  " + Main.commandHandler.getCommandAt(i).getName());
					}
				}
			}
		}
		else
		{
			printUsage();
		}
	}
	
	@Override
	public String help()
	{
		return "help * or help <command name>";
	}
	
	private void println(String text)
	{
		Main.println(text);
	}
}
