package game;

import java.awt.Color;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

import core.GameEventHandler;
import core.GameRunnable;
import core.ParticleArguments;
import core.ParticleEffect;
import core.guiElements.GuiButton;
import game.gameevents.GameEventClose;
import game.gameevents.GameEventInstruct;
import game.gameevents.GameEventPerformanceTest;
import gamegui.Menu;
import gamegui.Settings;
import main.Main;

/**
 * The <code>GameMain</code> class is the central for
 * executing game-specific events (not base
 * engine ones)
 * 
 * @author Ethan Vrhel
 * @see Player
 */
public class GameMain
	implements GameRunnable
{
	/**
	 * The name of the game
	 */
	public static final String NAME = "Chicken Run";
	
	/**
	 * The version of the game
	 */
	public static final String GAME_VERSION = "1.0a";
	
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
	
	private String initialMap;
	private Chapter initialChapter;
	private ArrayList<Chapter> chapters;
	
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
		
		GameEventInstruct event3 = new GameEventInstruct();
		gameEventHandler.addEvent(event3);
		
		ParticleArguments arg = new ParticleArguments(0, 0, 0, 50, 50, 15, 0, 0.9, -1, false, 10, 5000, null, 1, true, ParticleArguments.SpatialRelation.BOX, ParticleArguments.StopType.COLLISION);
		ParticleEffect effect = new ParticleEffect("WaterBottom", Main.getResourceHandler().getImage(Main.getResourceHandler().getIndexByName("waterParticle.png", true)), arg);
		Main.getParticleEffectHandler().addToBuffer(effect);
		
		chapters = new ArrayList<Chapter>();
		
		try 
		{
			readGameDataFile(new File("game\\game.dat"));
			initialMap = initialChapter.getMap();
		} 
		catch (FileNotFoundException e)
		{
			Main.println("Failed to read game data file!", Color.RED);
		}
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
	 * @return Whether the game is paused
	 */
	public boolean paused()
	{
		return paused;
	}
	
	/**
	 * Returns the initial map when the "new" button
	 * is clicked in the main menu
	 * 
	 * @return The initial map
	 */
	public String getInitialMap()
	{
		return initialMap;
	}
	
	private void readGameDataFile(File file) 
		throws FileNotFoundException
	{
		if (! file.exists())
			throw new FileNotFoundException();
		
		Scanner in = new Scanner(file);
		System.out.println("reading...");
		while (in.hasNextLine())
		{
			String line = in.nextLine();
			String[] tokens = line.split(" ");
			if (tokens[0].equals("Chapter"))
			{
				Chapter chapter;
				String name = "";
				for (int i = 1; i < tokens.length; i++)
				{
					name += tokens[i];
				}
				String[] tokens2 = in.nextLine().split(" ");
				String map = tokens2[1];
				chapter = new Chapter(name, map);
				chapters.add(chapter);
			}
			else if (tokens[0].equals("InitialChapter"))
			{
				String[] tokens2 = in.nextLine().split(" ");
				String chapterName = "";
				for (int i = 1; i < tokens2.length; i++)
				{
					chapterName += tokens2[i];
				}
				//System.out.println("chapter name: " + chapterNa);
				
				for (int i = 0; i < chapters.size(); i++)
				{
					if (chapters.get(i).getName().equals(chapterName))
					{
						initialChapter = chapters.get(i);
						break;
					}
				}
			}
		}
		in.close();
	}
}
