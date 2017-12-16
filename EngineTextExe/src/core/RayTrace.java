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
			Entity debugEntity = new Entity(Entity.STATIC, new File("resources\\textures\\" + ResourceHandler.debugStart),
					64, 32, false, "collision", startX - 32, startY - 16, 100, 64, 32, 100, false);
			Main.getEntityHandler().addStaticEntity(debugEntity);
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
			if (collided)
				break;
			
			x += deltaX;
			y += deltaY;
		}
		
		if (DEBUG_COLLISIONS)
		{
			Entity debugEntity = new Entity(Entity.STATIC, new File("resources\\textures\\" + ResourceHandler.debugEnd),
					64, 32, false, "collision", collisionX - 32, collisionY - 16, 100, 64, 32, 100, false);
			Main.getEntityHandler().addStaticEntity(debugEntity);
		}
	}
	
	public Entity getCollisionEntity()
	{
		return collisionEntity;
	}
}
