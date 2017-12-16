package game;

import java.awt.Color;
import java.io.File;

import core.Animation;
import core.AnimationHandler;
import core.AnimationLoader;
import core.Entity;
import core.GameEventHandler;
import core.GuiElementHandler;
import core.Material;
import core.ResourceHandler;
import core.guiElements.GuiAnimatedImage;
import core.guiElements.ButtonHandlers.ButtonAnimationHandler;
import game.gameevents.GameEventClose;
import main.Main;

public class GameMain
	implements Runnable
{
	public static final String NAME = "My Game";				// Game name
	public static final double GAME_VERSION = 0.0;				// Game version
	public static final boolean PLAYER_TRANSPARANCY = true;		// Toggles player transparency
	public static final int PLAYER_W = 64;						// Player width
	public static final int PLAYER_H = 64;						// Player height
	public static final int PLAYER_MASS = 10;					// Player mass
	public static final int TICK_RATE = 128;					// Game tick rate
	
	public static boolean gameRunning = true;					// Should the game be running

	private static int tickrate = 1000 / TICK_RATE;				// Milliseconds the game should update
	
	private GameEventHandler gameEventHandler;					// Game event handler
	
	
	
	// Initialization of game
	// Setup game events here
	public GameMain()
	{
		gameEventHandler = Main.getGameEventHandler();
		GameEventClose event = new GameEventClose("closeGame");
		gameEventHandler.addEvent(event);
	}
	
	// Game start
	public void run()
	{		
		while (gameRunning)
		{			
			gameEventHandler.update();
			
			if (Main.getInputListener().isKeyPressed("Escape"))
				System.exit(1);
			
			try
			{
				Thread.sleep(tickrate);
			}
			catch (Exception e)
			{
				Main.println("Fatal error!  Main game thread stopped!", Color.RED);
				System.exit(1);
			}
		}
		Main.println("exiting...", Color.YELLOW);
		System.exit(0);
	}
}
