/**
 * The game main class is the central for
 * executing game-specific events (not base
 * engine ones)
 * 
 * @author Ethan Vrhel
 */

package game;

import java.io.File;

import core.GameEventHandler;
import core.GameRunnable;
import core.GameSound;
import core.ParticleArguments;
import core.ParticleEffect;
import core.SoundSource;
import core.guiElements.GuiButton;
import game.gameevents.GameEventClose;
import game.gameevents.GameEventPerformanceTest;
import gamegui.Menu;
import gamegui.Settings;
import main.Main;

public class GameMain
	implements GameRunnable
{
	/**
	 * The name of the game
	 */
	public static final String NAME = "Richard The Gay Chicken";
	
	/**
	 * The version of the game
	 */
	public static final double GAME_VERSION = 1.0;
	
	/**
	 * Whether the player is transparent
	 */
	public static final boolean PLAYER_TRANSPARANCY = true;
	
	/**
	 * The player's width
	 */
	public static final int PLAYER_W = 64;
	
	/**
	 * The player's height
	 */
	public static final int PLAYER_H = 64;
	
	/**
	 * The player's mass
	 */
	public static final int PLAYER_MASS = 10;
	
	/**
	 * Whether the game should be running
	 */
	public static boolean gameRunning = true;
	
	private GameEventHandler gameEventHandler;
	private boolean paused;
	private Settings settingsWindow;
	
	private GuiButton resume;
	private GuiButton settings;
	private GuiButton exit;
	
	private Menu gameMenu;
	
	/**
	 * The number of collected gems
	 */
	public static int gems;
	
	/**
	 * The existing gems
	 */
	public static String[] existingGems = new String[256];
	
	/**
	 * The picked up gems
	 */
	public static boolean[] pickedUpGems = new boolean[256];
	
	public double lastGravity;
	
	/**
	 * The initialization of the game
	 */
	public GameMain()
	{
		gameEventHandler = Main.getGameEventHandler();
		GameEventClose event = new GameEventClose("closeGame");
		gameEventHandler.addEvent(event);
		
		GameEventPerformanceTest event2 = new GameEventPerformanceTest();
		gameEventHandler.addEvent(event2);
		gems = 0;
		lastGravity = Main.getPhysicsHandler().getGravity();
		paused = false;
		
		SoundSource p = new SoundSource(0, 0);
		GameSound sound = new GameSound("water", new File("resources\\sound\\water.wav"), GameSound.EFFECT, p, 500);
		ParticleArguments arg = new ParticleArguments(0, 0, 0, 50, 50, 15, 0, 0.9, -1, false, 10, 5000, null, 1, true, ParticleArguments.SpatialRelation.BOX, ParticleArguments.StopType.COLLISION);
		ParticleEffect effect = new ParticleEffect("WaterBottom", Main.getResourceHandler().getImage(Main.getResourceHandler().getIndexByName("waterParticle.png", true)), arg);
		Main.getParticleEffectHandler().addToBuffer(effect);
	}
	
	public void onGameInit()
	{
		//Main.println("init");
		resume = new GuiButton("resume", 1, Main.getResourceHandler().getByName("resumeunselected.png"), 0, 200, Integer.MAX_VALUE, 400, 64);
		settings = new GuiButton("settings", 1, Main.getResourceHandler().getByName("settingsunselected.png"), 0, 264, Integer.MAX_VALUE, 400, 64);
		exit = new GuiButton("exit", 1, Main.getResourceHandler().getByName("exitunselected.png"), 0, 328, Integer.MAX_VALUE, 400, 64);
		resume.setVisible(true);
		GuiButton[] buttons = {resume, settings, exit};
		Main.getGuiHandler().add(resume);
		Main.getGuiHandler().add(settings);
		Main.getGuiHandler().add(exit);
		gameMenu = new Menu(buttons, true);
		(new Thread(gameMenu)).start();
		settingsWindow = Main.getSettingsWindow();
		settingsWindow.setVisible(false);
	}
	
	public void onGameUpdate()
	{					
		gameEventHandler.update();
		
		if (! Main.getGameWindow().hasFocus())
		{
			if (! Main.gamePaused)
				gameMenu.triggerToggle();
		}
		
		if (resume.isHovered() && resume.isVisible())
		{
			resume.setImage(Main.getResourceHandler().getImage(Main.getResourceHandler().getIndexByName("resumeselected.png", true)));
			if (resume.isClicked())
			{
				gameMenu.triggerToggle();
				paused = ! paused;
				settingsWindow.dispose();
			}
		}
		else
		{
			resume.setImage(Main.getResourceHandler().getImage(Main.getResourceHandler().getIndexByName("resumeunselected.png", true)));
		}
		
		if (settings.isHovered() && resume.isVisible())
		{
			settings.setImage(Main.getResourceHandler().getImage(Main.getResourceHandler().getIndexByName("settingsselected.png", true)));
			if (settings.isClicked())
			{
				paused = ! paused;
				settingsWindow.pack();
				settingsWindow.showWindow();
			}
		}
		else
		{
			settings.setImage(Main.getResourceHandler().getImage(Main.getResourceHandler().getIndexByName("settingsunselected.png", true)));
		}
		
		if (exit.isHovered() && resume.isVisible())
		{
			exit.setImage(Main.getResourceHandler().getImage(Main.getResourceHandler().getIndexByName("exitselected.png", true)));
			if (exit.isClicked())
			{
				Main.exit(0);
			}
		}
		else
		{
			exit.setImage(Main.getResourceHandler().getImage(Main.getResourceHandler().getIndexByName("exitunselected.png", true)));
		}
		
		resume.resetClicked();
		settings.resetClicked();
		exit.resetClicked();
	}
	
	/**
	 * Returns whether the game is paused
	 * 
	 * @return
	 */
	public boolean paused()
	{
		return paused;
	}
}