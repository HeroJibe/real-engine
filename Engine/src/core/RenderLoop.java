package core;

import java.awt.Color;

import core.guiElements.ButtonHandlers.ButtonAnimationHandler;
import game.Player;
import gui.GameWindow;
import main.Main;

public class RenderLoop 
	implements Runnable
{
	/**
	 * The maximum FPS
	 */
	public static int maximumFPS = -1;
	
	/**
	 * The frequency of garbage collection
	 */
	public static int gCollectFreq = 100;
	
	private static boolean constructed = false;
	
	private EntityHandler entityHandler;
	private Player player;
	private GuiElementHandler guiHandler;
	private ButtonAnimationHandler bAnimationHandler;
	private ReflectiveEntityHandler reflectionHandler;
	private int numEntities;
	private static int sv;
	private int tick;
	
	private boolean valid;
	
	private static Entity cameraEntity;
	
	/**
	 * Creates a new <code>RenderLoop</code>.  This
	 * method will initialize the <code>cameraEntity</code>
	 * to <code>null</code>
	 */
	public RenderLoop()
	{
		if (constructed)
		{
			Main.println("Error!  The RenderLoop has been constructed twice!", Color.RED);
			valid = false;
			return;
		}
		
		valid = true;
		constructed = true;
		entityHandler = Main.getEntityHandler();
		player = Main.getPlayer();
		guiHandler = Main.getGuiHandler();
		bAnimationHandler = Main.getButtonAnimationHandler();
		reflectionHandler = Main.getReflectionHandler();
		cameraEntity = null;
	}
	
	@Override
	public void run()
	{
		if (! valid)
		{
			Main.println("Error!  A second RenderThread has been started!", Color.RED);
			System.out.println("Error!  A second RenderThread has been started!");
			Main.println("Stopping...", Color.RED);
			System.out.println("Stopping...");
			return;
		}
		
		Main.println("Render thread started");
		tick = 0;
		
		while (true)
		{
			try
			{
				if (Main.DEBUG)
				{
					//mousePos.getX() - Main.getGameWindow().getX(), mousePos.getY() - Main.getGameWindow().getY()
					numEntities = entityHandler.getNumEntities();
					
					boolean doPlayer = false;
					player = Main.getPlayer();
					if (Main.getPlayer() != null)
					{
						if (Main.getPlayer().getPlayerEntity() != null)
						{
							doPlayer = true;
						}
					}
					
					//boolean doDebug =
						//	(Main.getGameWindow() != null) &&
						//	(entityHandler != null) &&
						//	(Main.getFpsCounter() != null);
					//System.out.println("Window: " + Main.getGameWindow() + "\n"
						//	+ "Entity Handler: " + entityHandler + "\n"
						//	+ "FPS counter: " + Main.getFpsCounter() + "\n");
							
					//Thread.sleep(1000);
					
					try
					{
						if (doPlayer)
						{
							/*
							//System.out.println(Main.getPlayer().getPlayerEntity());
							//System.out.println(Math.round(10));
							//System.out.println(Main.debugMessage);
							//System.out.println(Main.resolutionScaleX);
							//if (player != null)
						//	{
								//String one = "Player (" + Math.round(player.getPlayerEntity().getX() / Main.resolutionScaleX);
						//	}
							//String two = ", " + Math.round(player.getPlayerEntity().getY() / Main.resolutionScaleY);
							String three = ", " +  Math.round(player.getPlayerEntity().getXVel());
							//System.out.println(player);
							///Thread.sleep(1000);
							//System.out.println(player.getPlayerEntity());
							String four = ", " +  Math.round(player.getPlayerEntity().getYVel()) + ")";
							String five = "   Camera (" + Main.getGameWindow().getCameraX();
							String six = ", " + Main.getGameWindow().getCameraY() + ")";
							String seven = "   rendering: " + Math.round(entityHandler.getVisibleEntities() / (double) numEntities * 100) + "%";
							String eight = "   fps: " + Main.getFpsCounter().getFps();
							*/
							
							Main.debugMessage = ("Player (" + Math.round(player.getPlayerEntity().getX() / Main.resolutionScaleX) 
									+ ", " + Math.round(player.getPlayerEntity().getY() / Main.resolutionScaleY) 
									+ ", " +  Math.round(player.getPlayerEntity().getXVel()) 
									+ ", " + Math.round(player.getPlayerEntity().getYVel()) + ")"
									+ "   Camera (" + Main.getGameWindow().getCameraX()
									+ ", " + Main.getGameWindow().getCameraY() + ")"
									+ "   rendering: " + Math.round(entityHandler.getVisibleEntities() / (double) numEntities * 100) + "%"
									//+ "   memory: " + Math.round((Runtime.getRuntime().maxMemory() - Runtime.getRuntime().freeMemory()) / (double) Runtime.getRuntime().maxMemory() * 100) + "% used"
									//+ "   threads: " + Main.getThreadsRunning()
									+ "   fps: " + Main.getFpsCounter().getFps()
									+ " on map \"" + Main.getMapName() + "\""
									);
									
						}
						else
							Main.debugMessage = ("fps: " + Main.getFpsCounter().getFps() + " (no map loaded)");
					}
					catch (Exception e) 
					{
						e.printStackTrace(System.out);
					}
				}
				
				/*
				 * cameraX = -1 * (int) Math.ceil(focusEntity.getX() - (GameWindow.XRES_GL / 2));
				cameraY = -1 * (int) Math.ceil(focusEntity.getY() - (GameWindow.YRES_GL / 2));

				 */
				if (cameraEntity != null)
				{
					Main.getGameWindow().setCameraX(-1 * (int) Math.ceil(cameraEntity.getX() - 
							((GameWindow.XRES_GL) / 2)));
					Main.getGameWindow().setCameraY( -1 * (int) Math.ceil(cameraEntity.getY() - 
							((GameWindow.YRES_GL) / 2)));
				}
				
				if (Main.getGameWindow().getCameraX() > Main.getGameWindow().cameraXBounds[1])
				{
					Main.getGameWindow().setCameraX(Main.getGameWindow().cameraXBounds[1]);
				}
				
				if (Main.getGameWindow().getCameraX() < Main.getGameWindow().cameraXBounds[0])
				{
					Main.getGameWindow().setCameraX(Main.getGameWindow().cameraXBounds[0]);
				}
				
				if (Main.getGameWindow().getCameraY() > Main.getGameWindow().cameraYBounds[1])
				{
					Main.getGameWindow().setCameraY(Main.getGameWindow().cameraYBounds[1]);
				}
				
				if (Main.getGameWindow().getCameraY() < Main.getGameWindow().cameraYBounds[0])
				{
					Main.getGameWindow().setCameraY(Main.getGameWindow().cameraYBounds[0]);
				}
				
				try
				{
					bAnimationHandler.update();
				}
				catch (Exception e) {}
				
				try
				{
					guiHandler.update();
				}
				catch (Exception e) {}
				
				try
				{
					entityHandler.update();
				}
				catch (Exception e) {}
				
				try
				{
					reflectionHandler.update(false);
				}
				catch (Exception e) {}
				
				
				Main.getFpsCounter().tick();
				
				if (maximumFPS != -1)
				{
					Thread.sleep(1000 / maximumFPS);
				}
				
				tick++;
				
				if (tick >= gCollectFreq)
				{
					System.gc();
					tick = 0;
				}
			}
			catch (Exception e) 
			{
				Main.println("Error!  Error in windiow thread stopped (unhandled excpetion): " + e);
				e.printStackTrace(System.out);
			}
		}
	}
	
	/**
	 * Sets the camera <code>Entity</code> that the camera
	 * in <code>GameWindow</code> will focus on.
	 * 
	 * @param e The <code>Entity</code>
	 */
	public static void setCameraEntity(Entity e)
	{
		cameraEntity = e;
	}
	
	public static int getSV()
	{
		return sv;
	}
}
