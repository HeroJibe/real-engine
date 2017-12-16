// Copyright 2017 by Ethan Vrhel
// All Rights Reserved

package main;

import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

import javax.swing.JOptionPane;

import core.AnimationHandler;
import core.Entity;
import core.EntityHandler;
import core.GameEventHandler;
import core.GameSoundHandler;
import core.InputListener;
import core.Light;
import core.LightHandler;
import core.MapEntityHandler;
import core.MapLoader;
import core.MouseInputListener;
import core.PhysicsHandler;
import core.RenderLoop;
import core.ResourceHandler;
import core.ShaderHandler;
import core.TriggerHandler;
import core.TypingHandler;
import editor.EditorMain;
import editor.ElementEditor;
import editor.MapDialog;
import editor.Selector;
import gui.GameWindow;

public final class Main
{
	// Game Engine information
	public static final double ENGINE_VERSION = 1.0;
	public static final int ENGINE_BUILD = 1;
	public static final String ENGINE_NAME = "Real Engine Map Editor";
	public static final String ENGINE_VERSION_NAME = ENGINE_NAME + " 2017";
	
	public static boolean DEBUG = true;							// Toggles debug mode
	public static final boolean OCCLUSION = true;				// Toggles using occlusion to optimize rendering
	
	public static final int UPDATE_RATE = 1;					// Game update rate
	public static final boolean DRAW_TRIGGERS = true;			// Toggles drawing trigger brushes
	public static final boolean DRAW_NAVMESH = false;			// Toggles drawing navigation meshes
	public static final boolean DRAW_ONLY_DYNAMIC = false;		// Toggles drawing only dynamic brushes
	public static boolean  drawWireframe = false;				// Toggles drawing in wireframe
	public static final double RENDER_REFRESH_RATE = 			// Maximum frames drawn per second obselete
								1000 / GameWindow.REFRESH_RATE;
	public static double renderRefreshBias = 0.25;				// Scale for the RENDER_REFRESH_RATE (don't go below 0.5) obsolete
	public static boolean stablizeFPS = false;					// Toggles FPS stabilization
	
	public static final boolean BUILD_LIGHTING = false;			// Toggles lightmap construction
	public static boolean runInWindow = false;					// Toggles running in a window
	public static int windowSizeX = 1280;						// Window x size
	public static int windowSizeY = 720;						// Window y size
	public static boolean overrideScale = false;				// Toggles scaling override
	public static double resolutionScaleX;					// Scaling of the resolution for translating entities on the X axis
	public static double resolutionScaleY;					// Scaling of the resolution for translating entities on the Y axis
	public static boolean lighting = false;						// Toggles shading
	public static boolean useDynamicLighting = false;			// Toggles dynamic lighting
	public static boolean useBoth = false;						// Use dynamic and static lighting
	
	private static File logFile;
	private static PrintStream out;
	
	public static String debugMessage = "";						// The debug message
	public static String loadMessage = "";						// The load message
	public static boolean loadingMap = false;					// Whether the game is loading a map or not
	
	public static boolean lossyRendering = false;				// Experimental rendering mode that reduces unnecessary rendering
	public static int lossFactor = 1000;						// lossyRendering loss factor
	
	private static GameWindow window;							// The game window
	private static EntityHandler entityHandler;					// The entity handler
	private static InputListener listener;						// The key listener
	private static ResourceHandler resourceHandler;				// The resource handler
	private static MapLoader mapLoader;							// The map loader
	private static TriggerHandler triggerHandler;				// The trigger handler
	private static Thread playerThread;							// The player thread
	private static MouseInputListener mouseListener;			// The mouse listener
	private static MapEntityHandler mapEntityHandler;			// The map entity handler
	private static PhysicsHandler physicsHandler;				// The physics handler
	private static GameEventHandler gameEventHandler;			// The game event handler
	private static AnimationHandler animationHandler;			// The animation handler
	private static LightHandler lightHandler;					// The light handler
	private static GameSoundHandler gameSoundHandler;			// The game sound handler
	private static ShaderHandler shaderHandler;					// The shader handler
	private static MapDialog mapDialog;							// The map dialog
	private static EditorMain editorMain;						// The editor main
	private static ElementEditor elementEditor;					// The element editor
	private static Selector selector;							// The entity selector
	private static Thread mapDialogThread;						// The map dialog thread
	private static Thread editorMainThread;						// The editor main thread
	private static Thread elementEditorThread;					// The element editor thread
	
	private static int threadsRunning = 0;						// Number of threads running
	
	public static boolean gamePaused = false;					// Whether the game is paused (implement where needed)
	public static boolean isMultiplayer = false;				// Whether the game is multiplayer (implement where needed)
	
	public static Light light3 = new Light(1, 1900, 1016, 1, 2000, 1500);
	
	public static void main(String[] args) throws InterruptedException, IOException
	{		
		//DEBUG = false;
		loadingMap = true;
		
		loadMessage = "Initializing engine...";
		
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm:ss");
		LocalDateTime now = LocalDateTime.now();
		println("Process started at: " + dtf.format(now) + " by " + GameWindow.USER_NAME);
		println("Running: " + ENGINE_VERSION_NAME + " (v" + ENGINE_VERSION + " build " + ENGINE_BUILD + ")");
		
		int imageCache;
		if (args.length >= 1)
		{
			try
			{
				imageCache = Integer.parseInt(args[0]);
			}
			catch (Exception e)
			{
				imageCache = 4096;
				if (args[0].equals("debug"))
					DEBUG = true;
			}
		}
		else
			imageCache = 4096;
		
		window = new GameWindow(ENGINE_NAME, GameWindow.XRES_GL, GameWindow.YRES_GL, true, imageCache);
		entityHandler = new EntityHandler(4096);
		resourceHandler = new ResourceHandler(256);
		listener = new InputListener(window);
		mouseListener = new MouseInputListener();
		triggerHandler = new TriggerHandler(256);
		mapEntityHandler = new MapEntityHandler(32);
		physicsHandler = new PhysicsHandler();
		gameEventHandler = new GameEventHandler(64);
		animationHandler = new AnimationHandler(256);
		lightHandler = new LightHandler(16, 32, 0.1);
		shaderHandler = new ShaderHandler(4);
		gameSoundHandler = new GameSoundHandler(128);
		elementEditor = new ElementEditor();
		editorMain = new EditorMain();
		mapDialog = new MapDialog();
		selector = new Selector();
		(new Thread(lightHandler)).start();
		(new Thread(animationHandler)).start();
		(new Thread(selector)).start();
		
		resolutionScaleX = (double) GameWindow.XRES_GL / 1920.0;
        resolutionScaleY = (double) GameWindow.YRES_GL / 1080.0;
		window.update();
		
		if (GameWindow.MEMORY_GB > 0)
			println("Avaliable Memory: " + GameWindow.MEMORY_GB + "GB");
		else if (GameWindow.MEMORY_MB > 0)
			println("Avaliable Memory: " + GameWindow.MEMORY_MB + "MB");
		else
			println("Avaliable Memory: " + GameWindow.MEMORY_KB + "KB");
		println("Monitor Resolution: " + GameWindow.XRES_GL + "x" + GameWindow.YRES_GL);
		println("Local Resolution: " + window.getScreenResX() + "x" + window.getScreenResY());
		println("Resolution Scale: " + resolutionScaleX + ", " + resolutionScaleY);
		println("Monitor Refresh Rate: " + GameWindow.REFRESH_RATE);
		println("Render FPS cap: " + GameWindow.REFRESH_RATE / renderRefreshBias);
		
		if (GameWindow.XRES_GL != 1920 || GameWindow.YRES_GL != 1080)
		{
			int test = JOptionPane.showConfirmDialog(window,
		             "Warning!  You are running a resolution that could cause problems with the engine. "
		             + "\nYour resolution: " + GameWindow.XRES_GL + "x" + GameWindow.YRES_GL
		             + "\nRecommended: 1920x1080"
		             + "\nContinue?", null,
		             JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE);
			if (test != 0)
				System.exit(0);
		}
		
		loadMessage = "Adding resources...";
		resourceHandler.autoImportResources();
		
		if (resourceHandler.getTriedResources() != resourceHandler.getLoadedResources())
		{
			Main.println("Failed to load " + (resourceHandler.getTriedResources() - resourceHandler.getLoadedResources()) + 
							" resources.", Color.RED);
		}
		else
		{
			Main.println("Loaded " + resourceHandler.getLoadedResources() + " resources!", Color.GREEN);
		}
		
		loadMessage = "Converting image resources...";
		Main.println("Converting image resources...");
		resourceHandler.autoConvertToImage();
		
		loadMessage = "Converting sound resources...";
		Main.println("Converting sound resources...");
		resourceHandler.autoConvertToSound();
		
		loadMessage = "Converting animation resources...";
		Main.println("Converting animation resources...");
		resourceHandler.autoConvertToAnimation();

		mapLoader = new MapLoader();
		mapLoader.shouldLoadSaved = false;
		
		elementEditorThread = new Thread(elementEditor);
		editorMainThread = new Thread(editorMain);	
		mapDialogThread = new Thread(mapDialog);
		
		mapDialogThread.start();
		
		RenderLoop renderLoop = new RenderLoop();
		(new Thread(renderLoop)).start();
		
		while (true)
		{			
			if (listener.isKeyPressed("N"))
			{
				exit(0);
			}
		}
	}
	
	// Handles map exit codes other than success
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
		return mapLoader.getMapName();
	}
	
	// Prints a line to the in-game console, log file, and the system console
	public static void println(String str)
	{
		System.out.println(str);
		if (window != null)
			window.addToConsole(str, Color.WHITE);
	}
	
	// Prints a line to the in-game console with a color, log file, and the system console
	public static void println(String str, Color c)
	{
		System.out.println(str);
		if (window != null)
			window.addToConsole(str, c);
	}
	
	// Gets prefix of a string
	public static String getPrefix(String str)
	{
		Scanner in = new Scanner(str);
		String nstr = in.next();
		in.close();
		return nstr;
	}
	
	// Gets the name of the next log file
	public static String getNextLogName()
	{
		int test = 0;
		while (new File("logs//log" + test + ".log").exists())
		{
			test++;
		}
		return "logs//log" + test + ".log";
	}
	
	// Loads a map from a file and the displacement of the player (xbias, ybias) on load
	public static int loadMap(String mapname, double xbias, double ybias)
	{
		loadMessage = "Loading map \"" + mapname + "\"...";
		loadingMap = true;
		mapLoader.printToTemp();
		mapLoader.readMap(resourceHandler.getByName(mapname), false);
		int exitCode = mapLoader.loadMap(false, mapLoader.getLoadSaved());
		if (exitCode == 1)
		{
			entityHandler.invokeUpdate();
			//lightHandler.updateBrightness(true);
			loadingMap = false;
		}
		else if (exitCode == 2)
		{
			entityHandler.invokeUpdate();
			loadingMap = false;
		}
		return exitCode;
	}
	
	// Returns the player's thread
	public static Thread getPlayerThread()
	{
		return playerThread;
	}
	
	// Returns the mouse listener
	public static MouseInputListener getMouseListener()
	{
		return mouseListener;
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
		Main.println(buffer);
		System.exit(code);
	}
	
	public static MapDialog getMapDialog()
	{
		return mapDialog;
	}
	
	public static EditorMain getEditorMain()
	{
		return editorMain;
	}
	
	public static ElementEditor getElementEditor()
	{
		return elementEditor;
	}
	
	public static Thread getMapDialogThread()
	{
		return mapDialogThread;
	}
	
	public static Thread getEditorMainThread()
	{
		return editorMainThread;
	}
	
	public static Thread getElementEditorThread()
	{
		return elementEditorThread;
	}
}
