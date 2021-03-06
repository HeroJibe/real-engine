/**
 * The PhysicsHandler handles gravity and friction
 * in entities.
 * 
 * @author Ethan Vrhel
 */

package core;

import java.awt.Color;

import main.Main;

public class PhysicsHandler
	implements Runnable
{
	public static final double STOP_THRESHOLD_X = 0.1;
	
	private Entity[] entities;
	
	private long physicsTick = 12;
	private double gravity = 0.5;
	private double friction = 0.8;
	private double terminalVel = 40;
	
	/**
	 * Whether physics is enabled
	 */
	public boolean physicsEnabled = true;
	
	/**
	 * The last gravity that was used
	 */
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
			try
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
								entities[i].setYVel(entities[i].getYVel() + entities[i].getGravity());
								if (entities[i].getYVel() >= terminalVel)
									entities[i].setYVel(terminalVel);
								else if (entities[i].getYVel() <= -terminalVel)
									entities[i].setYVel(-terminalVel);
								
								entities[i].setXVel(entities[i].getXVel() * entities[i].getFriction());
								if (Math.abs(entities[i].getXVel()) < STOP_THRESHOLD_X)
									entities[i].setXVel(0);	
								
								entities[i].translate(entities[i].getXVel(), entities[i].getYVel());
							}
						}
					}
					Thread.sleep(physicsTick);
				}
				else
				{
					Thread.yield();
				}
			}
			catch (Exception e)
			{
				Main.println("error", Color.RED);
			}
		}
	}
	
	/**
	 * Sets the friction
	 * 
	 * @param friction
	 */
	public void setFriction(double friction)
	{
		Entity[] entities = Main.getEntityHandler().getEntities();
		this.friction = friction;
		for (int i = 0; i < entities.length; i++)
		{
			if (entities[i] != null)
			{
				if (entities[i].updateWithPhysics)
				{
					entities[i].setFriction(friction);
				}
			}
		}
	}
	
	/**
	 * Returns the friction
	 * 
	 * @return
	 */
	public double getFriction()
	{
		return friction;
	}
	
	/**
	 * Sets the gravity
	 * 
	 * @param gravity
	 */
	public void setGravity(double gravity)
	{
		Entity[] entities = Main.getEntityHandler().getEntities();
		this.gravity = gravity;
		for (int i = 0; i < entities.length; i++)
		{
			if (entities[i] != null)
			{
				if (entities[i].updateWithPhysics)
				{
					entities[i].setGravity(gravity);
				}
			}
		}
	}
	
	/**
	 * Returns the gravity
	 * 
	 * @return
	 */
	public double getGravity()
	{
		return gravity;
	}
}
