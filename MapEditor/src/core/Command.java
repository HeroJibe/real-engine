package core;

import main.Main;

public abstract class Command
{
	private String name;
	
	public Command(String name)
	{
		this.name = name;
		if (name == null)
			throw new IllegalArgumentException("Name cannot be null!");
		Main.getCommandHandler().add(this);
	}
	
	/**
	 * Returns the name of the command
	 * @return The name of the command
	 */
	public final String getName()
	{
		return name;
	}
	
	/**
	 * Prints the usage of the command to the console
	 */
	public final void printUsage()
	{
		System.out.println("Usage: " + getUsage());
	}
	
	/**
	 * Executes the command
	 * @param args The arguments for the command
	 * @throws IllegalArgumentException When the execution is invalid
	 */
	public abstract void execute(String[] args) throws IllegalArgumentException;
	
	/**
	 * Gets the correct usage of the command
	 * @return The correct usage of the command
	 */
	public abstract String getUsage();
}
