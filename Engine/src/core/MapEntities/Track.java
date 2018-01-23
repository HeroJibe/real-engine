package core.MapEntities;

import core.Entity;
import main.Main;

public class Track 
	extends MapEntity
{
	public static final int X_PLANE = 0;
	public static final int Y_PLANE = 1;
	public static final int CONTINUE = 0;
	public static final int REVERSE_ON_COLLIDE = 1;
	
	public static final int SIDE_BIAS = 4;
	
	private int plane;
	private double dest;
	private int delay;
	private Entity entity;
	private int dir;
	private boolean ignorePlayer;
	
	public Track(String name, int plane, double dest, int delay, Entity entity, boolean ignorePlayer) 
	{
		super(name, CONTINUOUS, 0, 0, 1, 1);
		this.plane = plane;
		this.dest = dest;
		this.delay = delay;
		this.entity = entity;
		this.entity.canPush(true);
		this.ignorePlayer = ignorePlayer;
		if (dest < 0)
			dir = 1;
		else
			dir = 0;
		setPriority(Thread.MAX_PRIORITY);
	}
	
	public String toString()
	{
		return "track tied to " + entity;
	}

	public void function() 
	{
		boolean left = false;
		boolean right = false;
		boolean top = false;
		boolean bottom = false;
		
		// Test if player is on left
		Entity testLeft = new Entity(Entity.STATIC, true, (entity.getX() - SIDE_BIAS) / Main.resolutionScaleX, 
				entity.getY() / Main.resolutionScaleY, 
				0, (int) entity.getHeight(), 100);
		if (testLeft.hasCollided() && ! testLeft.hasCollided(entity))
		{
			if (! (ignorePlayer && testLeft.hasCollided(Main.getPlayer().getPlayerEntity())))
				left = true;
		}
		
		// Test if player is on right
		Entity testRight = new Entity(Entity.STATIC, true, (entity.getX2() + SIDE_BIAS) / Main.resolutionScaleX, 
				entity.getY() / Main.resolutionScaleY, 
				0, (int) entity.getHeight(), 100);
		if (testRight.hasCollided() && ! testRight.hasCollided(entity))
		{
			if (! (ignorePlayer && testRight.hasCollided(Main.getPlayer().getPlayerEntity())))
				right = true;
		}	
		
		// Test bottom
		Entity testGround;
		testGround = new Entity(Entity.STATIC, true, entity.getX() / Main.resolutionScaleX, 
				(entity.getY2() + SIDE_BIAS) / Main.resolutionScaleY, 
			(int) entity.getWidth(), 0, 100);
		if (testGround.hasCollided() && ! testGround.hasCollided(entity))
		{
			if (! (ignorePlayer && testGround.hasCollided(Main.getPlayer().getPlayerEntity())))
				bottom = true;
		}
		
		// Test top
		testGround = new Entity(Entity.STATIC, true, entity.getX() / Main.resolutionScaleX, 
				(entity.getY() - SIDE_BIAS) / Main.resolutionScaleY, 
			(int) entity.getWidth(), 0, 100);
		if (testGround.hasCollided() && ! testGround.hasCollided(entity))
		{
			if (! (ignorePlayer && testGround.hasCollided(Main.getPlayer().getPlayerEntity())))
				top = true;
		}
		
		if (plane == X_PLANE)
		{
			if (dir == 0 && right)
			{
				dest = -1 * dest;
				dir = 1;
			}
			else if (dir == 1 && left)
			{
				dest = -1 * dest;
				dir = 0;
			}
			
			entity.translate(dest, 0);
		}
		else
		{
			if (top || bottom)
				dest = -1 * dest;
			
			entity.translate(0, dest);
		}
		
		try 
		{
			Thread.sleep(delay);
		}
		catch (InterruptedException e)
		{
			
		}
	}
}
