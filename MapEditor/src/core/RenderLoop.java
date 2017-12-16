package core;

import java.awt.Color;

import gui.GameWindow;
import main.Main;

public class RenderLoop 
	implements Runnable
{
	private EntityHandler entityHandler;
	private GameWindow window;
	private GameEventHandler gameEventHandler;
	private TriggerHandler triggerHandler;
	private int numEntities;
	private static int sv;
	
	public RenderLoop()
	{
		entityHandler = Main.getEntityHandler();
		window = Main.getGameWindow();
		gameEventHandler = Main.getGameEventHandler();
		triggerHandler = Main.getTriggerHandler();
	}
	
	public void run()
	{	
		Main.println("Render thread started");
		
		try
		{
			while (true)
			{
				numEntities = entityHandler.getNumEntities();
				Main.debugMessage = ( 
						"entities: " + numEntities + "/" + entityHandler.getMaxEntities() + 
						"   rendering: " + window.getImageHandler().getNumImage() + "/" + numEntities + "/" + window.getImageHandler().getCacheSize() 
						+ "   threads: " + Main.getThreadsRunning()
						+ "   shaders: " + Main.getShaderHandler().getNumEnabled());
				
				entityHandler.update();
				triggerHandler.update();
			}
		}
		catch (Exception e)
		{
			Main.println("Fatal error!  Render thread stopped! (" + e + ")", Color.RED);
			Main.exit(1);
		}
	}
	
	public static int getSV()
	{
		return sv;
	}
}
