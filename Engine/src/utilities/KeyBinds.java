package utilities;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import main.Main;

public class KeyBinds 
{
	public static String UP = "W";
	public static String DOWN = "S";
	public static String LEFT = "A";
	public static String RIGHT = "D";
	public static String SPRINT = "Shift";
	public static String PAUSE_MENU = "Escape";
	public static String CONSOLE = "Tilde";
	
	// Loads key bindings from a config file
	public static void loadKeys(File keyConfig)
	{
		try 
		{
			Scanner in = new Scanner(keyConfig);
			int lines = 0;
			while (in.hasNextLine())
			{
				lines++;
				in.nextLine();
			}
			in.close();
			
			if (lines == 0)
			{
				setDefaultKeys();
				return;
			}
			
			for (int i = 0; i < lines; i++)
			{
				String line = in.nextLine();
				String delims = " = ";
				String tokens[] = line.split(delims);
				
				switch (tokens[0])
				{
					case "UP":			UP = tokens[1];
										break;
										
					case "DOWN":		DOWN = tokens[1];
										break;
									
					case "LEFT":		LEFT = tokens[1];
										break;
										
					case "RIGHT":		RIGHT = tokens[1];
										break;
									
					case "SPRINT":		SPRINT = tokens[1];
										break;
									
					case "PAUSE_MENU": 	PAUSE_MENU = tokens[1];
										break;
										
					case "CONSOLE":		CONSOLE = tokens[1];
										break;
									
					default:		Main.println("error: " + keyConfig.toString() + ":" + (i + 1) + ": unknown binding");
									break;
				}					
			}
			
			Main.println("Loaded key bindings.");
		} 
		catch (FileNotFoundException e) 
		{
			Main.println("Invalid key config file.  Defaulting...");
		}
	}
	
	/**
	 * Sets all key binds to their defaults
	 */
	public static void setDefaultKeys()
	{
		UP = "W";
		DOWN = "S";
		LEFT = "A";
		RIGHT = "D";
		SPRINT = "Shift";
	}
}
