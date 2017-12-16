package core.commands;

import java.io.File;

import core.Command;
import main.Main;

public class Path 
	extends Command
{
	private static File mapFolder;
	private static boolean valid = false;
	
	public Path()
	{
		super("path", "Sets the transfer path for maps.");
	}

	@Override
	public void execute(String[] args) 
	{
		valid = false;
		if (args == null)
		{
			printUsage();
			return;
		}
		String combined = "";
		for (int i = 0; i < args.length; i++)
		{
			combined = combined + " " + args[i];
		}
		
		File temp = new File(combined);
		if (temp.isDirectory())
		{
			mapFolder = temp;
			valid = true;
		}
		else
		{
			Main.println("Path cannot be a file");
		}
	}

	@Override
	public String help() 
	{
		return "path <file path>";
	}
	
	public static File getPath()
	{
		if (valid)
		{
			return mapFolder;
		}
		else
		{
			return null;
		}
	}
}
