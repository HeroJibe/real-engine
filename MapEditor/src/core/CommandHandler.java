package core;

public class CommandHandler 
{
	public static final int MAX_COMMANDS = 16;
	
	private Command[] commands;
	
	public CommandHandler()
	{
		commands = new Command[16];
	}
	
	public void add(Command cmd)
	{
		for (int i = 0; i < MAX_COMMANDS; i++)
		{
			if (commands[i] == null)
			{
				commands[i] = cmd;
				return;
			}
		}
	}
	
	public void execute(String cmd, String[] args)
		throws IllegalArgumentException
	{
		if (cmd == null)
			throw new IllegalArgumentException("Command does not exist! (null)");
		
		for (int i = 0; i < MAX_COMMANDS; i++)
		{
			if (commands[i] != null)
			{
				if (commands[i].getName().equalsIgnoreCase(cmd))
				{
					commands[i].execute(args);
					return;
				}
			}
		}
		throw new IllegalArgumentException("Command does not exist!");
	}
	
	public void execute(String cmd)
			throws IllegalArgumentException
	{
		if (cmd == null)
			throw new IllegalArgumentException("Command does not exist! (null)");
		
		String[] tokens = cmd.split(" ");
		String cmdName = tokens[0];
		
		int numArgs = 0;
		for (int i = 1; i < tokens.length; i++)
		{
			numArgs++;
		}
			
		String[] args = null;		
		if (numArgs != 0)
			args = new String[numArgs];
		
		for (int i = 0; i < numArgs; i++)
		{
			args[i] = tokens[i + 1];
		}
	
		for (int i = 0; i < MAX_COMMANDS; i++)
		{
			if (commands[i] != null)
			{
				if (commands[i].getName().equalsIgnoreCase(cmdName))
				{
					commands[i].execute(args);
					return;
				}
			}
		}
		throw new IllegalArgumentException("Command does not exist!");
	}
}
