package game.gameevents;

import core.GameEvent;
import core.NodeTranslator;
import main.Main;

public class GameEventPerformanceTest
	extends GameEvent
	implements Runnable
{
	private Thread myThread;
	
	private NodeTranslator first;
	private NodeTranslator second;
	private NodeTranslator third;
	private boolean started;
	
	public GameEventPerformanceTest()
	{
		super("performanceTest");
		started = false;
	}

	@Override
	public void update() 
	{
		if (! started)
		{
			started = true;
			
			Main.getPlayer().getPlayerEntity().updateWithPhysics(false);
			Main.getPlayer().getPlayerEntity().setGravity(0);
			Main.getPlayer().getPlayerEntity().setFriction(0);
			
			third = new NodeTranslator(Main.getPlayer().getPlayerEntity(), 0.5, NodeTranslator.X_PLANE, 5,
					Main.getNodeHandler().getNode("third"), Main.getNodeHandler().getNode("fourth"), null);
			
			second = new NodeTranslator(Main.getPlayer().getPlayerEntity(), 1, NodeTranslator.Y_PLANE, 5,
					Main.getNodeHandler().getNode("second"), Main.getNodeHandler().getNode("third"), third);
			
			first = new NodeTranslator(Main.getPlayer().getPlayerEntity(), 1, NodeTranslator.X_PLANE, 5,
					Main.getNodeHandler().getNode("first"), Main.getNodeHandler().getNode("second"), second);
			
			myThread = new Thread(this);
			myThread.start();
		}
		disable();
	}

	@Override
	public void run()
	{
		first.start();
	}
}
