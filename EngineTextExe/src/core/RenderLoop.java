package core;

import java.awt.Color;

import core.guiElements.ButtonHandlers.ButtonAnimationHandler;
import game.GameMain;
import game.Player;
import gui.GameWindow;
import main.Main;

public class RenderLoop 
	implements Runnable
{
	private EntityHandler entityHandler;
	private GameWindow window;
	private Player player;
	private GameEventHandler gameEventHandler;
	private GuiElementHandler guiHandler;
	private ButtonAnimationHandler bAnimationHandler;
	private int numEntities;
	private static int sv;
	
	public RenderLoop()
	{
		entityHandler = Main.getEntityHandler();
		window = Main.getGameWindow();
		player = Main.getPlayer();
		gameEventHandler = Main.getGameEventHandler();
		guiHandler = Main.getGuiHandler();
		bAnimationHandler = Main.getButtonAnimationHandler();
	}
	
	public void run()
	{	
		Main.println("Render thread started");
		
		while (GameMain.gameRunning)
		{
			sv = (int) Main.getRenderMonitor().high - (int) Main.getRenderMonitor().low;
			
			numEntities = entityHandler.getNumEntities();
			Main.debugMessage = ("Player (" + Math.round(player.getPlayerEntity().getX()) + ", " + Math.round(player.getPlayerEntity().getY()) + 
					")   entities: " + numEntities + "/" + entityHandler.getMaxEntities() + 
					"   rendering: " + window.getImageHandler().getNumImage() + "/" + numEntities + "/" + window.getImageHandler().getCacheSize() + 
					"   fps: " + Main.getFpsCounter().getFps() + "   avg: " + Main.getFpsCounter().getAverage()
					+ "   sv: " + Main.getFpsCounter().getSv()
					+ "   game events: " + gameEventHandler.eventsExecuted
					+ "   threads: " + Main.getThreadsRunning()
					+ "   render tick: " + entityHandler.calculationTime);
			gameEventHandler.eventsExecuted = 0;
			
			bAnimationHandler.update();
			guiHandler.update();
			entityHandler.update();
		
			try
			{
				Thread.sleep((long) (Main.RENDER_REFRESH_RATE * Main.renderRefreshBias));
			} 
			catch (InterruptedException e) 
			{
				Main.println("Fatal error!  Render thread stopped!", Color.RED);
				System.exit(1);
			}
			
			Main.getFpsCounter().tick();
		}
	}
	
	public static int getSV()
	{
		return sv;
	}
}
