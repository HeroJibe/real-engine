package main;

import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.URL;

import core.CommandHandler;
import core.ConfigLoader;
import core.MapElement;
import core.MapElementHandler;
import core.commands.*;
import core.mapElements.*;
import core.mapElements.triggers.*;
import gui.Console;
import gui.SystemConsole;

public class Main 
	implements Runnable
{	
	public static final String NAME = "Real Engine Map Checker";
	public static final String VERSION_NAME = "1.1.0.01";
	public static final boolean DEVELOPMENT_BUILD = true;
	
	public static boolean useGUI;
	
	public static String dir = "maps\\";
	
	private static Console window;
	public static CommandHandler commandHandler = new CommandHandler();
	public static MapElementHandler elementHandler = new MapElementHandler();
	public static ConfigLoader configLoader = new ConfigLoader();
	public static boolean lockScroll;
	
	public static void main(String[] args) 
	{		
		long millis = System.currentTimeMillis();
		lockScroll = true;
		
		useGUI = true;
		if (args.length > 0)
		{
			if (args[0].equals("-nogui"))
			{
				useGUI = false;
			}
		}
		
		window = new Console();
		window.setVisible(useGUI);
		
		String app = "";
		if (DEVELOPMENT_BUILD)
		{
			app = " (development build)";
		}
		window.println(NAME + " version " + VERSION_NAME + app);
		
		window.println("Initializing...");
		(new Thread(new Main())).start();
		(new Thread(new SystemConsole())).start();
		
		window.println("Setting up commands...");
		new Exit();
		new Help();
		new Load();
		new Unload();
		new LockScroll();
		new Clear();
		new Check();
		new FunCommand();
		new Execute();
		new Echo();
		new Configuration();
		new Clip();
		
		window.println("Done.");
		window.println("Setting up map elements...");
		MapElement.setup();
		new Brush();
		new LowDetailBrush();
		new ReflectiveBrush();
		
		new TriggerGameEvent();
		new TriggerGravity();
		new TriggerHurt();
		new TriggerLoad();
		new TriggerMove();
		new TriggerShader();
		new TriggerSound();
		new TriggerStartTrigger();
		new TriggerStopTrigger();
		new TriggerToggleSolid();
		
		window.println("Done.");
		window.println("Loading...");
		
		boolean succ = configLoader.load("autoexec");
		if (! succ)
		{
			Main.println("Creating configuration file...");
			File config = new File("config\\autoexec.config");
			try
			{
				config.createNewFile();
			} 
			catch (Exception e) 
			{
				println("Failed.");
				e.printStackTrace(System.out);
			}
		}
		window.println("Done.");
		
		long newTime = System.currentTimeMillis();
		
		window.println("Finished intialization in " + (newTime - millis) + "ms.");
	}
	
	@Override
	public void run() 
	{
		while (true)
		{
			if (lockScroll)
				window.win.outputScroll.getVerticalScrollBar().setValue(
						window.win.outputScroll.getVerticalScrollBar().getMaximum());
			Thread.yield();
		}
	}
	
	public static void print(String str)
	{
		window.print(str);
	}
	
	public static void println(String str)
	{
		window.println(str);
	}
	
	public static void clear()
	{
		window.win.output.setText("");
	}
	
	public static void execute(String cmd)
	{
		window.execute(cmd);
	}
	
	public static void useGUI(boolean useGUI)
	{
		Main.useGUI = useGUI;
		window.setVisible(useGUI);
		if (useGUI)
			window.requestFocus();
	}
	
	public static void exit(int code)
	{
		System.exit(code);
	}
}
