package core;

import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.PointerInfo;

import core.guiElements.ButtonHandlers.ButtonAnimationHandler;
import game.Player;
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
	
	public RenderLoop()
	{
		entityHandler = Main.getEntityHandler();
		Main.getGameWindow();
		player = Main.getPlayer();
		guiHandler = Main.getGuiHandler();
		bAnimationHandler = Main.getButtonAnimationHandler();
		reflectionHandler = Main.getReflectionHandler();
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
					PointerInfo mouseInfo = MouseInfo.getPointerInfo();
					Point mousePos = mouseInfo.getLocation();
					//mousePos.getX() - Main.getGameWindow().getX(), mousePos.getY() - Main.getGameWindow().getY()
					numEntities = entityHandler.getNumEntities();
					Main.debugMessage = ("Player (" + Math.round(player.getPlayerEntity().getX() / Main.resolutionScaleX) 
							+ ", " + Math.round(player.getPlayerEntity().getY() / Main.resolutionScaleY) 
							+ ", " +  Math.round(player.getPlayerEntity().getXVel()) 
							+ ", " + Math.round(player.getPlayerEntity().getYVel()) + ")"
							+ "   Mouse (" + (mousePos.getX() - Main.getGameWindow().getX()) / Main.resolutionScaleX
							+ ", " + (mousePos.getY() - Main.getGameWindow().getY()) / Main.resolutionScaleY + ")"
							+ "   processing: " + Math.round(numEntities / (double) entityHandler.getDynEntities() * 100) + "%"
							//+ "   memory: " + Math.round((Runtime.getRuntime().maxMemory() - Runtime.getRuntime().freeMemory()) / (double) Runtime.getRuntime().maxMemory() * 100) + "% used"
							//+ "   threads: " + Main.getThreadsRunning()
							+ "   fps: " + Main.getFpsCounter().getFps()
							);
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
	
	public static int getSV()
	{
		return sv;
	}
}
