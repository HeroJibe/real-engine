package core.commands;

import java.io.File;

import core.Command;
import main.Main;

public class Save
	extends Command
{
	public Save()
	{
		super("save");
	}

	@Override
	public void execute(String[] args)
		throws IllegalArgumentException 
	{
		if (Main.getCurrentMap() == null)
			throw new IllegalArgumentException("No map loaded!");
		
		boolean succ = Main.getCurrentMap().export(new File("..\\maps\\" + Main.getCurrentMap().getName()));
		if (succ)
			System.out.println("Saved.");
		else
			System.out.println("Failed.");
	}
	
	@Override
	public String getUsage()
	{
		return "save";
	}
}