package main;

import core.*;
import core.commands.*;
import core.mapElements.*;

public class Main 
{
	private static CommandHandler cmdHandler;
	private static MapElementHandler elemHandler;
	private static InputListener inpListener;
	private static Map currMap;
	
	public static void main(String[] args) 
	{
		long start = System.currentTimeMillis();
		
		System.out.println("Initializing...");
		cmdHandler = new CommandHandler();
		elemHandler = new MapElementHandler();
		inpListener = new InputListener();
		(new Thread(inpListener)).start();
		
		System.out.println("Setting up map elements...");
		new Brush();
		
		System.out.println("Setting up commands...");
		new Exit();
		new Exists();
		new New();
		new Add();
		new Save();
		new Load();
		new Edit();
		new Delete();
		
		//Cloner.getClass(new Brush("XD"));
		
		long end = System.currentTimeMillis();
		System.out.println("Done. (" + (end - start) + "ms)");
		
		inpListener.enableInput();
	}
	
	public static void setCurrentMap(Map map)
	{
		currMap = map;
	}
	
	public static Map getCurrentMap()
	{
		return currMap;
	}
	
	public static CommandHandler getCommandHandler()
	{
		return cmdHandler;
	}
	
	public static MapElementHandler getMapElementHandler()
	{
		return elemHandler;
	}
	
	public static InputListener getInputListener()
	{
		return inpListener;
	}
}
