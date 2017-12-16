package core;

import main.Main;

public abstract class Command 
{
	private String name;
	private String desc;
	
	public Command(String name, String desc)
	{
		this.name = name;
		this.desc = desc;
		Main.commandHandler.addCommand(this);
	}
	
	public String getName() 
	{
		return name;
	}
	
	public String getDescription()
	{
		return desc;
	}
	
	public String toString()
	{
		return "command: " + name + " (" + desc + ")";
	}
	
	public void printHelpMessage()
	{
		Main.println(getHelpMessage());
	}
	
	public void printUsage()
	{
		Main.println("Usage: " + help());
	}
	
	private String getHelpMessage()
	{
		return "  Name: " + name + "\n  Description: " + desc + "\n  Usage: " + help();
	}

	public abstract void execute(String[] args);
	public abstract String help();
}
