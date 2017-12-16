package core.commands;

import core.Command;
import main.Main;

public class LockScroll
	extends Command
{

	public LockScroll() 
	{
		super("lockscroll", "Sets whether the scroll bar is locked to the bottom.");
	}

	@Override
	public void execute(String[] args) 
		{
			if (args != null)
		{
			try
			{
				Main.lockScroll = Boolean.parseBoolean(args[0]);
			}
			catch (Exception e)
			{
				e.printStackTrace(System.out);
				Main.println("Invalid argument: " + args[0]);
			}
		}
		else
		{
			Main.lockScroll = ! Main.lockScroll;
			if (Main.lockScroll)
				Main.println("Locked scroll");
			else
				Main.println("Unlocked scroll");
		}
	}

	@Override
	public String help()
	{
		return "lockscroll or lockscroll <true/false>";
	}
}
