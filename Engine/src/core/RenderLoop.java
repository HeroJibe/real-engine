package core;

import core.guiElements.ButtonHandlers.ButtonAnimationHandler;
import game.Player;
import gui.GameWindow;
import main.Main;

public class RenderLoop 
	implements Runnable
{
	public static int maximumFPS = -1;
	public static int gCollectFreq = 100;
	
	private EntityHandler entityHandler;
	private Player player;
	private GuiElementHandler guiHandler;
	private ButtonAnimationHandler bAnimationHandler;
	private ReflectiveEntityHandler reflectionHandler;
	private int numEntities;
	private static int sv;
	private int tick;
	
	private static Entity cameraEntity;
	
	public RenderLoop()
	{
		entityHandler = Main.getEntityHandler();
		Main.getGameWindow();
		player = Main.getPlayer();
		guiHandler = Main.getGuiHandler();
		bAnimationHandler = Main.getButtonAnimationHandler();
		reflectionHandler = Main.getReflectionHandler();
		cameraEntity = null;
	}
	
	public void run()
	{	
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
					Main.debugMessage = ("Player (" + Math.round(player.getPlayerEntity().getX() / Main.resolutionScaleX) 
							+ ", " + Math.round(player.getPlayerEntity().getY() / Main.resolutionScaleY) 
							+ ", " +  Math.round(player.getPlayerEntity().getXVel()) 
							+ ", " + Math.round(player.getPlayerEntity().getYVel()) + ")"
							+ "   Camera (" + Main.getGameWindow().getCameraX()
							+ ", " + Main.getGameWindow().getCameraY() + ")"
							+ "   processing: " + Math.round(numEntities / (double) entityHandler.getDynEntities() * 100) + "%"
							//+ "   memory: " + Math.round((Runtime.getRuntime().maxMemory() - Runtime.getRuntime().freeMemory()) / (double) Runtime.getRuntime().maxMemory() * 100) + "% used"
							//+ "   threads: " + Main.getThreadsRunning()
							+ "   fps: " + Main.getFpsCounter().getFps()
							);
				}
				
				/*
				 * cameraX = -1 * (int) Math.ceil(focusEntity.getX() - (GameWindow.XRES_GL / 2));
				cameraY = -1 * (int) Math.ceil(focusEntity.getY() - (GameWindow.YRES_GL / 2));

				 */
				if (cameraEntity != null)
				{
					Main.getGameWindow().setCameraX(-1 * (int) Math.ceil(cameraEntity.getX() - (GameWindow.XRES_GL / 2)));
					Main.getGameWindow().setCameraY( -1 * (int) Math.ceil(cameraEntity.getY() - (GameWindow.YRES_GL / 2)));
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
				Main.println("Error!  Render thread stopped (unhandled excpetion): " + e);
				e.printStackTrace(System.out);
			}
		}
	}
	
	public static void setCameraEntity(Entity e)
	{
		cameraEntity = e;
	}
	
	public static int getSV()
	{
		return sv;
	}
}
