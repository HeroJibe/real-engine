/**
 * Stores the key bindings for the game
 * to use
 * 
 * @author Ethan Vrhel
 */

package utilities;

import java.awt.Color;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
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
	
	/**
	 * Sets the key binds from a File
	 * 
	 * @param keyConfig
	 */
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
				Main.println("Invalid configuration file.", Color.RED);
				setDefaultKeys();
				return;
			}
			
			in = new Scanner(keyConfig);
			for (int i = 0; i < lines; i++)
			{
				String line = in.nextLine();
				String delims = " = ";
				String tokens[] = line.split(delims);
				
				if (tokens[0].equals("UP"))
					UP = tokens[1];
				else if (tokens[0].equals("DOWN"))
					DOWN = tokens[1];
				else if (tokens[0].equals("LEFT"))
					LEFT = tokens[1];
				else if (tokens[0].equals("RIGHT"))			
					RIGHT = tokens[1];
				else if (tokens[0].equals("SPRINT"))
					SPRINT = tokens[1];
				else if (tokens[0].equals("PAUSE_MENU"))	
					PAUSE_MENU = tokens[1];
				else if (tokens[0].equals("CONSOLE"))		
					CONSOLE = tokens[1];
				else
					Main.println("error: " + keyConfig.toString() + ":" + (i + 1) + ": unknown binding");				

			}
			in.close();
		} 
		catch (FileNotFoundException e) 
		{
			Main.println("Invalid key config file.", Color.RED);
		}
	}
	
	/**
	 * Saves the configuration into a file
	 * 
	 * @throws FileNotFoundException
	 */
	public static void generateFile()
		throws IOException
	{
		File f = new File("config\\keyconfig.config");
		PrintStream out = new PrintStream(f);

		out.println("UP = " + UP);
		out.println("DOWN = " + DOWN);
		out.println("LEFT = " + LEFT);
		out.println("RIGHT = " + RIGHT);
		out.println("SPRINT = " + SPRINT);
		out.println("PAUSE_MENU = " + PAUSE_MENU);
		out.println("CONSOLE = " + CONSOLE);
		
		out.close();
	}
	
	/**
	 * Generates the default key bindings file
	 * 
	 * @throws FileNotFoundException
	 */
	public static void generateDefault()
		throws IOException
	{
		setDefaultKeys();
		generateFile();
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
		PAUSE_MENU = "Escape";
		CONSOLE = "Tilde";
	}
}
