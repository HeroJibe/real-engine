package main;

import java.awt.Color;
import java.awt.DisplayMode;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Scanner;
import core.*;
import core.MapElements.Brush;
import core.guiElements.ButtonHandlers.ButtonAnimationHandler;
import game.GameMain;
import game.Player;
import gamegui.ApplySettings;
import gamegui.Menu;
import gamegui.Settings;
import gui.DebugWindow;
import gui.GameWindow;
import shaders.Death;
import shaders.Jitter;
import utilities.ClockTimer;
import utilities.ClockTimerHandler;
import utilities.FramesCounter;
import utilities.GraphicsConfig;
import utilities.KeyBinds;
import utilities.RenderMonitor;
import utilities.ResourceMonitor;

/**
 * This class is the startup class and
 * initializes the engine.
 * 
 * @author Ethan Vrhel
 * @see GameMain
 */
public final class Main
{
	/**
	 * The numerical version of the engine
	 */
	public static final int ENGINE_VERSION = 13303;
	
	/**
	 * The build of the engine
	 */
	public static final int ENGINE_BUILD = 47;
	
	/**
	 * The name of the engine
	 */
	public static final String ENGINE_NAME = "Real Engine";
	
	/**
	 * The String version of the engine
	 */
	public static final String ENGINE_VERSION_NAME = "1.3.3.03";
	
	/**
	 * Whether the engine is in debug mode
	 */
	public static boolean DEBUG = true;
	
	/**
	 * Toggles occlusion to optimize rendering
	 */
	public static final boolean OCCLUSION = true;
	
	/**
	 * Whether the engine should write to a log file
	 */
	public static boolean WRITE_TO_LOG = false;
	
	/**
	 * The game's update rate
	 */
	public static final int UPDATE_RATE = 1;
	
	/**
	 * Whether the engine should draw trigger brushes
	 */
	public static final boolean DRAW_TRIGGERS = false;
	
	/**
	 * Whether the engine should draw nodes
	 */
	public static final boolean DRAW_NODES = false;
	
	/**
	 * Whether the engine should draw the navigation mesh
	 */
	@Deprecated
	public static final boolean DRAW_NAVMESH = false;
	
	/**
	 * Whether the engine should draw only dynamic entities
	 */
	public static final boolean DRAW_ONLY_DYNAMIC = false;
	
	/**
	 * Toggles drawing in wireframe
	 */
	public static boolean drawWireframe = false;
	@Deprecated
	public static final double RENDER_REFRESH_RATE = 
								1000 / GameWindow.REFRESH_RATE;
	
	@Deprecated
	public static double renderRefreshBias = 0.25;
	@Deprecated
	public static boolean stablizeFPS = false;
	
	/**
	 * Whether lightmaps will be built
	 */
	public static final boolean BUILD_LIGHTING = false;
	
	/**
	 * Whether the engine will run in a window
	 * @deprecated
	 */
	@Deprecated
	public static boolean runInWindow = false;
	
	/**
	 * The window's x size
	 */
	public static int windowSizeX = 1280;
	
	/**
	 * The window's y size
	 */
	public static int windowSizeY = 720;
	public static boolean overrideScale = false;				// Toggles scaling override
	public static double resolutionScaleX;						// Scaling of the resolution for translating entities on the X axis
	public static double resolutionScaleY;						// Scaling of the resolution for translating entities on the Y axis
	public static boolean lighting = true;						// Toggles shading
	public static boolean useDynamicLighting = true;			// Toggles dynamic lighting
	public static boolean useBoth = false;						// Use dynamic and static lighting
	
	private static File logFile;
	private static PrintStream out;
	
	public static String debugMessage = "";						// The debug message
	public static String loadMessage = "";						// The load message
	public static boolean loadingMap = false;					// Whether the game is loading a map or not
	
	@Deprecated
	public static boolean lossyRendering = false;				// Experimental rendering mode that reduces unnecessary rendering
	@Deprecated
	public static int lossFactor = 1000;						// lossyRendering loss factor
	
	private static GameWindow window;							// The game window
	private static EntityHandler entityHandler;					// The entity handler
	private static InputListener listener;						// The key listener
	private static Player player;								// The player
	private static ResourceHandler resourceHandler;				// The resource handler
	private static MapLoader mapLoader;							// The map loader
	private static TriggerHandler triggerHandler;				// The trigger handler
	private static GameThread playerThread;						// The player thread
	private static MouseInputListener mouseListener;			// The mouse listener
	private static GuiElementHandler guiHandler;				// The GUI handler
	private static Menu menu;									// The game menu
	private static RenderMonitor rMonitor;						// The render monitor
	private static ButtonAnimationHandler bAnimationHandler;	// The button animation handler
	private static MapEntityHandler mapEntityHandler;			// The map entity handler
	private static PhysicsHandler physicsHandler;				// The physics handler
	private static GameEventHandler gameEventHandler;			// The game event handler
	private static GameMain gameMain;							// The main game
	private static FramesCounter fpsCounter;					// The fps counter
	private static AnimationHandler animationHandler;			// The animation handler
	private static LightHandler lightHandler;					// The light handler
	private static GameSoundHandler gameSoundHandler;			// The game sound handler
	private static ShaderHandler shaderHandler;					// The shader handler
	private static ParticleEffectHandler particleEffectHandler;	// The particle effect handler
	private static ReflectiveEntityHandler reflectionHandler;	// The reflection handler
	private static GameThreadHandler gameThreadHandler;			// The game thread handler
	private static DelayedGameThreadHandler delayedGameThreadHandler; // The delayed game thread handler
	private static DebugWindow debugWin;						// The debug window
	private static ClockTimerHandler clockHandler;				// The clock handler
	private static MapElementHandler mapElementHandler;			// The map element handler
	private static NodeHandler nodeHandler;						// The node handler
	private static MaterialHandler materialHandler;				// The material handler
	
	private static ResourceMonitor currResourceMonitor;			// The current resource monitor
	
	private static Settings settingsWindow;						// The settings window
	
	private static int threadsRunning = 0;						// Number of threads running
	
	/**
	 * The player's start x
	 */
	public static double player_x;
	
	/**
	 * The player's start y
	 */
	public static double player_y;
	
	/**
	 * Whether the game is paused
	 */
	public static boolean gamePaused = false;
	
	/**
	 * Whether the game is multiplayer
	 */
	public static boolean isMultiplayer = false;
	
	private Main() {}	
	
	/**
	 * The main method (duh.)
	 * 
	 * @param args The program arguments
	 */
	public static void main(String[] args)
	{
		ClockTimer t = new ClockTimer();
		t.startTimer();
		
		System.setProperty("sun.java2d.opengl", "true");		
		
		if (DEBUG)
			WRITE_TO_LOG = true;
		
		loadingMap = true;
		
		logFile = new File(getNextLogName());
		try 
		{
			logFile.createNewFile();
		}
		catch (IOException e1) 
		{
			Main.println("Failed to create log file!", Color.RED);
		}
		
		try 
		{
			out = new PrintStream(logFile);
		}
		catch (FileNotFoundException e1) 
		{
			Main.println("Failed to find log file!", Color.RED);
		}
		
		int imageCache = 512;
		window = new GameWindow(GameMain.NAME, GameWindow.XRES_GL, GameWindow.YRES_GL, true, imageCache);
		
		if (args.length > 0)
		{
			String[] tokens = args[0].split("/");
			if (tokens[0].equals("-prof"))
			{
				newResourceMonitor();
			}
		}
		
		try
		{
			//GameWindow.gd.setFullScreenWindow(window);
			DisplayMode[] dm = GameWindow.gd.getDisplayModes();
			System.out.println("found " + dm.length + " DisplayModes");
			System.out.println("Printing valid resolutions...");
			for (int i = 0; i < dm.length; i++)
			{
				if (dm[i].getRefreshRate() == 60)
				{
					if ((double) dm[i].getWidth() / dm[i].getHeight() == 16.0 / 9.0)
					System.out.println("num " + (i) + ": "
							+ dm[i].getWidth() + "x" + dm[i].getHeight() + 
							" depth: " + dm[i].getBitDepth() + 
							" refresh: " + dm[i].getRefreshRate());
				}
			}
			//GameWindow.gd.setDisplayMode(dm[61]);
			//GameWindow.XRES_GL = GameWindow.gd.getDisplayMode().getWidth();
			//GameWindow.YRES_GL = GameWindow.gd.getDisplayMode().getHeight();
			//GameWindow.gd.setFullScreenWindow(window);
		}
		catch (Exception e)
		{
			println("Failed to go fullscreen: " + e, Color.RED);
		}
		
		
		window.setVisible(true);
		
		loadMessage = "Initializing engine...";
		
		System.out.println(GameWindow.XRES_GL + "x" + GameWindow.YRES_GL);
		
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm:ss");
		LocalDateTime now = LocalDateTime.now();
		println("Process started at: " + dtf.format(now) + " by " + GameWindow.USER_NAME);
		println("Using: " + ENGINE_NAME + " " + ENGINE_VERSION_NAME + 
				" (v" + ENGINE_VERSION + " build " + ENGINE_BUILD + ")");
		println("Running: " + GameMain.NAME + " v." + GameMain.GAME_VERSION);
		
		if (args.length > 0)
		{
			Main.println("arguments: " + Arrays.toString(args));
		}
		
		println("Intializing...");
		
		println("Constructing handlers...");
		entityHandler = new EntityHandler(2048);
		resourceHandler = new ResourceHandler(256);
		listener = new InputListener(window);
		mouseListener = new MouseInputListener(window);
		triggerHandler = new TriggerHandler(256);
		guiHandler = new GuiElementHandler(16);
		rMonitor = new RenderMonitor(window);
		bAnimationHandler = new ButtonAnimationHandler(16);
		mapEntityHandler = new MapEntityHandler(32);
		physicsHandler = new PhysicsHandler();
		gameEventHandler = new GameEventHandler(64);
		fpsCounter = new FramesCounter();
		animationHandler = new AnimationHandler(64);
		//lightHandler = new LightHandler(16, 32, 0.1);
		shaderHandler = new ShaderHandler(4);
		gameSoundHandler = new GameSoundHandler(64);
		particleEffectHandler = new ParticleEffectHandler(8);
		reflectionHandler = new ReflectiveEntityHandler(8);
		gameThreadHandler = new GameThreadHandler(8);
		delayedGameThreadHandler = new DelayedGameThreadHandler(8);
		clockHandler = new ClockTimerHandler(8);
		mapElementHandler = new MapElementHandler(32);
		nodeHandler = new NodeHandler(32);
		materialHandler = new MaterialHandler();
		
		RenderLoop renderLoop = new RenderLoop();
		Thread renderThread = new Thread(renderLoop);
		//renderThread.setPriority(Thread.MAX_PRIORITY);
		renderThread.start();
		
		//(new Thread(lightHandler)).start();
		GameThread rMonitorThread = new GameThread(rMonitor, 1000 / RenderMonitor.R_UPDATE_FACTOR);
		rMonitorThread.start();
		GameThread triggerHandlerThread = new GameThread(triggerHandler, -1);

		Thread animationHandlerThread = new Thread(animationHandler);
		animationHandlerThread.setPriority(Thread.MIN_PRIORITY);
		animationHandlerThread.start();
		//animationHandlerThread.start();
		//(new Thread(animationHandler)).start();
		(new GameThread(particleEffectHandler, -1)).start();
		//(new Thread(reflectionHandler)).start();
		
		settingsWindow = new Settings();
		println("Done.");
		
		println("Setting up map loader...");
		
		new Brush();
		
		println("Done.");
		
		//Light ligh = new Light(Light.CONSTANT, 800, 800, 100, 1000, 1000);
		//lightHandler.addLight(ligh);
		
		if (! overrideScale)
		{
			resolutionScaleX = (double) GameWindow.XRES_GL / 1920.0;
	        resolutionScaleY = (double) GameWindow.YRES_GL / 1080.0;
		}
		//window.update();
		
		/*
		println("System information:");
		if (GameWindow.MEMORY_GB > 0)
			println("\tAvaliable Memory: " + GameWindow.MEMORY_GB + "GB");
		else if (GameWindow.MEMORY_MB > 0)
			println("\tAvaliable Memory: " + GameWindow.MEMORY_MB + "MB");
		else
			println("\tAvaliable Memory: " + GameWindow.MEMORY_KB + "KB");
		println("\tMonitor Resolution: " + GameWindow.XRES_GL + "x" + GameWindow.YRES_GL);
		println("\tLocal Resolution: " + window.getScreenResX() + "x" + window.getScreenResY());
		println("\tResolution Scale: " + resolutionScaleX + ", " + resolutionScaleY);
		println("\tMonitor Refresh Rate: " + GameWindow.REFRESH_RATE);
		//println("Render FPS cap: " + GameWindow.REFRESH_RATE / renderRefreshBias);
		 * 
		 */
		
		loadMessage = "Adding resources...";
		resourceHandler.autoImportResources();
		
		if (resourceHandler.getTriedResources() != resourceHandler.getLoadedResources())
		{
			println("Failed to load " + (resourceHandler.getTriedResources() - resourceHandler.getLoadedResources()) + 
							" resources.", Color.RED);
		}
		else
		{
			Main.println("Loaded " + resourceHandler.getLoadedResources() + " resources!");
		}
		
		//debugWin = new DebugWindow();
		
		loadMessage = "Converting resources...";
		println("Converting resources...");
		
		resourceHandler.autoConvertToImage();
		resourceHandler.autoConvertToSound();
		resourceHandler.autoConvertToAnimation();
		
		println("Done.");
		
		loadMessage = "Creating materials...";
		println("Creating materials...");
		new Material("Stone");
		new Material("Dirt");
		new Material("Grass");
		new Material("Water");
		
		println("Done.");
		
		loadMessage = "Loading configuration...";
		println("Loading configurations...");
		File keys = new File("config\\keyconfig.config");
		if (keys.exists())
		{
			KeyBinds.loadKeys(keys);
		}
		else
		{
			println("Error finding key binding configuration", Color.RED);
			println("Generating...");
			try 
			{
				KeyBinds.generateDefault();
			}
			catch (IOException e) 
			{
				Main.println("Failed to generate key binding configurations!", Color.RED);
			}
		}
		
		File video = new File("config\\graphics.config");
		if (video.exists())
		{
			GraphicsConfig.loadGraphics(video);
		}
		else
		{
			println("Error finding graphics configuration", Color.RED);
			println("Generating...");
			try
			{
				GraphicsConfig.generateFile();
			}
			catch (IOException e) 
			{
				Main.println("Failed to generate graphics configuration!", Color.RED);
			}
		}
		ApplySettings.loadSettings(settingsWindow);
		
		println("Done.");
		
		println("Initializing game loop...");
		gameMain = new GameMain();
		player = new Player(listener);
		mapLoader = new MapLoader();
		mapLoader.shouldLoadSaved = false;
		println("Done.");
		
		Main.println("Loading shaders...");
		Jitter jitter = new Jitter("jitter", 0, 2);
		Death death = new Death("death");
		shaderHandler.addShader(jitter);
		shaderHandler.addShader(death);
		Main.println("Done.");
		
		//Thread.sleep(1000);
		t.stopTimer();
		Main.println("Finished initialization. (" + t.getTimeMillis() + "ms)");
		
		window.setLocationRelativeTo(null);
		Thread.yield();
		
		//mapname = JOptionPane.showInputDialog(window, "Map name:");
		//String mapname = "test.map";
		int exitCode = -1;
		/*
		try
		{
			exitCode = loadMap(mapname, 0, 0);
		}
		catch (Exception e)
		{
			e.printStackTrace(System.out);
			try
			{
				Thread.sleep(5000);
			}
			catch (InterruptedException e2)
			{
				e.printStackTrace(System.out);
			}
			Main.exit(1);
		}
		*/
		loadingMap = false;
		game.MainMenu mainMenu = new game.MainMenu();
		Thread mainMenuThread = new Thread(mainMenu);
		//mainMenuThread.setPriority(Thread.MIN_PRIORITY);
		mainMenuThread.start();
		mainMenu.show();
		playerThread = new GameThread(player, 1);
		loadingMap = false;
		
		while (! mainMenu.loaded())
			Thread.yield();
		
		exitCode = 1;
		
		//ApplySettings.applySettings(settingsWindow);
		//ApplySettings.loadSettings(settingsWindow);
		
		//window.setLocation((GameWindow.XRES_GL / 2) - (window.getWidth() / 4), 
		//		(GameWindow.YRES_GL / 2) - (window.getHeight() / 4));
		
		//ApplySettings.loadSettings(settingsWindow);
		
		if (exitCode == 1 || exitCode == 2)
		{
			loadingMap = false;
			
			loadMessage = "Creating player...";
			Entity playerEntity = new Entity(Entity.DYNAMIC, resourceHandler.getByName("Dance1.png"), 26, 44, 
					true, "Player", player_x, player_y, 10, 26, 44, 100, false);
			playerEntity.setName("Player Entity");
			
			playerEntity.setMass(GameMain.PLAYER_MASS);
			playerEntity.setTransparancy(GameMain.PLAYER_TRANSPARANCY);
			addPlayer(playerEntity);
			entityHandler.addDynamicEntity(playerEntity);
			player.getPlayerEntity().ignoreBounds(true);
			
			playerThread.start();
			loadMessage = "Done";
			triggerHandlerThread.start();
			
			(new Thread(physicsHandler)).start();
			GameThread gameMainThread = new GameThread(gameMain, -1);
			gameMainThread.start();
			gameThreadHandler.init();
			delayedGameThreadHandler.init();
			Thread gmThrdHndl = new Thread(gameThreadHandler);
			gmThrdHndl.setPriority(Thread.MAX_PRIORITY - 1);
			gmThrdHndl.start();
			Thread dlGmThrdHndl = new Thread(delayedGameThreadHandler);
			dlGmThrdHndl.setPriority(Thread.MAX_PRIORITY - 1);
			dlGmThrdHndl.start();
		}
		else
		{
			try
			{
				Thread.sleep(2500);
			}
			catch (InterruptedException e)
			{
				e.printStackTrace(System.out);
			}
			Main.exit(1);
		}
	}
	
	/**
	 * Handles map errors
	 * 
	 * @param exitCode The exit code
	 */
	public static void mapExitCode(int exitCode)
	{
		if (exitCode == 0)
		{
			loadMessage = ("Error: exception while loading map: no map loaded");
		}
		else if (exitCode == -1)
		{
			loadMessage = ("Error: unknown exception while loading map: check console");
		}
	}
	
	// Returns the number of threads running
	public static int getThreadsRunning()
	{
		threadsRunning = Thread.activeCount();
		return threadsRunning;
	}
	
	// Returns the game window
	public static GameWindow getGameWindow()
	{
		return window;
	}
	
	// Returns the input listener/key listener
	public static InputListener getInputListener()
	{
		return listener;
	}
	
	// Returns the entity handler
	public static EntityHandler getEntityHandler()
	{
		return entityHandler;
	}
	
	// Returns the resource handler
	public static ResourceHandler getResourceHandler()
	{
		return resourceHandler;
	}
	
	// Returns the trigger handler
	public static TriggerHandler getTriggerHandler()
	{
		return triggerHandler;
	}
	
	// Gets the name of the current loaded map
	public static String getMapName()
	{
		if (mapLoader == null)
			return null;
		return mapLoader.getMapName();
	}
	
	// Gets the player
	public static Player getPlayer()
	{
		return player;
	}
	
	/**
	 * Prints a line to the in-game console
	 * 
	 * @param str A message
	 */
	public static void println(String str)
	{
		//System.out.println(str);
		if (window != null)
			window.addToConsole(str, Color.WHITE);
		if (WRITE_TO_LOG)
			out.println(str);
	}
	
	/**
	 * Prints a line to the in-game console
	 * 
	 * @param str A message
	 * @param c The message's color
	 */
	public static void println(String str, Color c)
	{
		//System.out.println(str);
		if (window != null)
			window.addToConsole(str, c);
		if (WRITE_TO_LOG)
			out.println(str);
	}
	
	/**
	 * Returns the prefix of a String
	 * 
	 * @param str A <code>String</code>
	 * @return Its prefix
	 */
	public static String getPrefix(String str)
	{
		Scanner in = new Scanner(str);
		String nstr = in.next();
		in.close();
		return nstr;
	}
	
	/**
	 * Gets the name of the next log
	 * 
	 * @return The name of the next log
	 */
	public static String getNextLogName()
	{
		int test = 0;
		while (new File("logs//log" + test + ".log").exists())
		{
			test++;
		}
		return "logs//log" + test + ".log";
	}
	
	/**
	 * Adds an <code>Entity</code> to the player
	 * 
	 * @param playerEntity An <code>Entity</code>
	 */
	public static void addPlayer(Entity playerEntity)
	{
		player.setPlayerEntity(playerEntity);
	}
	
	/**
	 * Loads a map
	 * 
	 * @param mapname The name of the map
	 * @param xbias The player's x offset
	 * @param ybias The player's y offset
	 * @return The return status
	 */
	public static int loadMap(String mapname, double xbias, double ybias)
	{
		loadMessage = "Loading map \"" + mapname + "\"...";
		loadingMap = true;
		mapLoader.printToTemp();
		mapLoader.readMap(resourceHandler.getByName(mapname), true);
		int exitCode = mapLoader.loadMap(false, mapLoader.getLoadSaved());
		if (exitCode == 1)
		{
			if (player.getPlayerEntity() != null)
			{
				entityHandler.addDynamicEntity(player.getPlayerEntity());
				player.translate(xbias, ybias);
			}
			loadingMap = false;
		}
		else if (exitCode == 2)
		{
			loadingMap = false;
		}
		return exitCode;
	}
	
	// Returns the player's thread
	public static GameThread getPlayerThread()
	{
		return playerThread;
	}
	
	// Returns the GUI handler
	public static GuiElementHandler getGuiHandler()
	{
		return guiHandler;
	}
	
	// Returns the mouse listener
	public static MouseInputListener getMouseListener()
	{
		return mouseListener;
	}
	
	// Returns the menu
	public static Menu getMenu()
	{
		return menu;
	}
	
	// Returns the render monitor
	public static RenderMonitor getRenderMonitor()
	{
		return rMonitor;
	}
	
	// Returns the button animation handler
	public static ButtonAnimationHandler getButtonAnimationHandler()
	{
		return bAnimationHandler;
	}
	
	// Returns the map entity handler
	public static MapEntityHandler getMapEntityHandler()
	{
		return mapEntityHandler;
	}
	
	// Returns the physics handler
	public static PhysicsHandler getPhysicsHandler()
	{
		return physicsHandler;
	}
	
	// Returns the game event handler
	public static GameEventHandler getGameEventHandler()
	{
		return gameEventHandler;
	}
	
	// Returns the FPS counter
	public static FramesCounter getFpsCounter()
	{
		return fpsCounter;
	}
	
	// Returns the animation handler
	public static AnimationHandler getAnimationHandler()
	{
		return animationHandler;
	}
	
	// Returns the light handler
	public static LightHandler getLightHandler()
	{
		return lightHandler;
	}
	
	// Returns the game sound handler
	public static GameSoundHandler getGameSoundHandler()
	{
		return gameSoundHandler;
	}
	
	// Returns the shader handler
	public static ShaderHandler getShaderHandler()
	{
		return shaderHandler;
	}
	
	// Returns the particle effect handler
	public static ParticleEffectHandler getParticleEffectHandler()
	{
		return particleEffectHandler;
	}
	
	// Returns the reflection handler
	public static ReflectiveEntityHandler getReflectionHandler()
	{
		return reflectionHandler;
	}
	
	// Returns the game main
	public static GameMain getGameMain()
	{
		return gameMain;
	}
	
	// Returns the game settings window
	public static Settings getSettingsWindow()
	{
		return settingsWindow;
	}
	
	// Returns the game thread handler
	public static GameThreadHandler getGameThreadHandler()
	{
		return gameThreadHandler;
	}
	
	// Returns the delayed game thread handler
	public static DelayedGameThreadHandler getDelayedGameThreadHandler()
	{
		return delayedGameThreadHandler;
	}
	
	// Returns the debug window
	public static DebugWindow getDebugWindow()
	{
		return debugWin;
	}
	
	// Returns the clock handler
	public static ClockTimerHandler getClockTimerHandler()
	{
		return clockHandler;
	}
	
	/**
	 * @return The MapElementHandler
	 */
	public static MapElementHandler getMapElementHandler()
	{
		return mapElementHandler;
	}
	
	/**
	 * Returns the node handler
	 * @return The NodeHandler
	 */
	public static NodeHandler getNodeHandler()
	{
		return nodeHandler;
	}
	
	/**
	 * Returns the material Handler
	 * @return The MaterialHandler
	 */
	public static MaterialHandler getMaterialHandler()
	{
		return materialHandler;
	}
	
	/**
	 * Reinitializes the <code>GameWindow</code>
	 * 
	 * @param width The width of the window
	 * @param height The height of the window
	 * @param bordered Whether the window is bordered
	 */
	public static synchronized void reinitalize(int w, int h, boolean bordered)
	{
		window.dispose();
		window = new GameWindow(GameMain.NAME, w, h, bordered, window.getHandler().getCacheSize());
		resolutionScaleX = (double) GameWindow.XRES_GL / 1920.0;
        resolutionScaleY = (double) GameWindow.YRES_GL / 1080.0;
		System.out.println("Reinitalized game window.");
		System.out.println("new window: " + window);
	}
	
	/**
	 * Creates and returns a new resource monitor
	 * along with printing the previous one, if
	 * any
	 * 
	 * @return The new resource monitor
	 */
	public static ResourceMonitor newResourceMonitor()
	{
		if (currResourceMonitor != null)
		{
			currResourceMonitor.print(System.out);
			currResourceMonitor.print(out);
		}
		currResourceMonitor = new ResourceMonitor();
		return currResourceMonitor;
	}
	
	/**
	 * Returns the current resource monitor
	 * 
	 * @return The current resource monitor
	 */
	public static ResourceMonitor getCurrentResourceMonitor()
	{
		return currResourceMonitor;
	}
	
	/**
	 * Exits the program with debug messages
	 * 
	 * @param code The exit code
	 */
	public static void exit(int code)
	{
		String buffer = "Program exited with code " + code;
		if (code == 0)
		{
			buffer = buffer + " (exited safely)";
		}
		else if (code == 1)
		{
			buffer =  buffer + " (exited with an error)";
		}
		else
		{
			buffer = buffer + " (unknown exit code)";
		}
		if (currResourceMonitor != null)
		{
			currResourceMonitor.print(System.out);
			currResourceMonitor.print(out);
		}
		Main.println(buffer);
		System.exit(code);
	}
}
