package core;

import main.Main;

public class PhysicsHandler
	implements Runnable
{
	public static final double STOP_THRESHOLD_X = 0.1;
	
	private Entity[] entities;
	
	public long physicsTick = 12;
	public double gravity = 0.5;
	public double friction = 0.5;
	public boolean physicsEnabled = true;
	public double lastGravity;
	
	public PhysicsHandler() 
	{
		entities = Main.getEntityHandler().getEntities();
		lastGravity = gravity;
	}
	
	public void run()
	{
		while (physicsEnabled)
		{
			if (! Main.gamePaused || Main.isMultiplayer)
			{
				entities = Main.getEntityHandler().getEntities();
				for (int i = 0; i < entities.length; i++)
				{
					if (entities[i] != null)
					{
						if (! entities[i].isStatic() && entities[i].isKinetic())
						{
							entities[i].setYVel(entities[i].getYVel() + gravity);
							
							entities[i].setXVel(entities[i].getXVel() * friction);
							if (Math.abs(entities[i].getXVel()) < STOP_THRESHOLD_X)
								entities[i].setXVel(0);	
							
							entities[i].translate(entities[i].getXVel(), entities[i].getYVel());
						}
					}
				}
			}
			
			try
			{
				Thread.sleep(physicsTick);
			}
			catch (Exception e)
			{
				
			}
		}
	}
}
