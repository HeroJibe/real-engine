package core;

import java.io.File;

import main.Main;

public class RayTrace
{
	public static final double PERSICION = 1;
	public static final int MAX_DIST = 65535;
	public static final boolean DEBUG_COLLISIONS = false;
	
	private double startX;
	private double startY;
	private double x;
	private double y;
	private double maxXDist;
	private double maxYDist;
	private int direction;
	private Entity toExclude;
	private boolean ignoreDynamic;
	private boolean ignoreStatic;
	
	private Entity collisionEntity;
	private double collisionX;
	private double collisionY;
	
	public RayTrace()
	{
		startX = 0;
		startY = 0;
	}
	
	public RayTrace(Entity toExclude)
	{
		startX = 0;
		startY = 0;
		this.toExclude = toExclude;
	}
	
	public void setup(double x, double y)
	{
		startX = x;
		startY = y;
	}
	
	public void setDirection(int direction)
	{
		this.direction = direction;
	}
	
	// 0 deg   = right
	// 90 deg  = down
	// 180 deg = left
	// 270 deg = up
	
	
	// Starts the ray trace
	public void startTrace(int maxCollisions)
	{
		Entity entities[] = Main.getEntityHandler().getEntities();
		int dynEntities = Main.getEntityHandler().getDynEntities();
		x = startX;
		y = startY;
		double deltaX = Math.cos(Math.toRadians(direction));
		double deltaY = Math.sin(Math.toRadians(direction));
		if (DEBUG_COLLISIONS)
		{
		}
		
		boolean collided = false;
		for (int i = 0; i < maxCollisions; i++)
		{
			for (int j = 0; j < dynEntities; j++)
			{
				if (entities[j] != null)
		 		{
					if (entities[j].isSolid())
					{
						if (ignoreDynamic)
						{
							if (entities[j].hasPointCollidedNoDynamic(x, y))
							{
								collisionEntity = entities[j];
								collisionX = x;
								collisionY = y;
								collided = true;
							}
						}
						else if (ignoreStatic)
						{
							if (entities[j].hasPointCollidedNoStatic(x, y))
							{
								collisionEntity = entities[j];
								collisionX = x;
								collisionY = y;
								collided = true;
							}
						}
						else
						{
							if (entities[j].hasPointCollided(x, y))
							{
								collisionEntity = entities[j];
								collisionX = x;
								collisionY = y;
								collided = true;
							}
						}
					}
				}
			}
			if (collided)
				break;
			
			x += deltaX;
			y += deltaY;
		}
		
		if (DEBUG_COLLISIONS)
		{
		}
	}
	
	public Entity getCollisionEntity()
	{
		return collisionEntity;
	}
	
	public void ignoreDynamic(boolean ignoreDynamic)
	{
		this.ignoreDynamic = ignoreDynamic;
	}
	
	public void ignoreStatic(boolean ignoreStatic)
	{
		this.ignoreStatic = ignoreStatic;
	}
}
