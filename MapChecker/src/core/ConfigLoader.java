package core;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import main.Main;

public class ConfigLoader 
{	
	private CommandHandler commandHandler;
	
	public ConfigLoader()
	{
		commandHandler = Main.commandHandler;
	}
	
	public boolean load(String configFileName)
	{
		if (configFileName == null)
		{
			Main.println("Invalid configuration file");
			return false;
		}
		
		File configFile = new File("config\\" + configFileName + ".config");
		
		try
		{
			Scanner in = new Scanner(configFile);
			int numCommands = 0;
			while (in.hasNextLine())
			{
				numCommands++;
				in.nextLine();
			}			
			in.close();
			
			in = new Scanner(configFile);
			String[] commands = new String[numCommands];
			for (int i = 0; i < numCommands; i++)
			{
				commands[i] = in.nextLine();
			}
			in.close();
			
			for (int i = 0; i < numCommands; i++)
			{
				commandHandler.getByName(commandHandler.getCommandName(commands[i])).execute(
						commandHandler.getArgumentsFromString(commands[i]));
			}
			return true;
		}
		catch (FileNotFoundException e) 
		{
			Main.println("Could not find configuration file: " + configFile.toString());
			e.printStackTrace(System.out);
			return false;
		}
		catch (Exception e)
		{
			e.printStackTrace(System.out);
			return false;
		}
	}
}
