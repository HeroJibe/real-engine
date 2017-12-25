package core.commands;

import java.io.File;

import core.Command;

public class Exists 
	extends Command
{
	public Exists()
	{
		super("exists");
	}
	
	@Override
	public void execute(String[] args) throws IllegalArgumentException 
	{
		if (args == null)
			throw new IllegalArgumentException("Invalid arguments!");
		
		if (args.length != 1)
			throw new IllegalArgumentException("Invalid arguments!");
		
		File f = new File("..\\" + args[0]);
		System.out.println(f.exists());
	}
	
	@Override
	public String getUsage()
	{
		return "exists [file]";
	}
}
