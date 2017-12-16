package core;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Random;

import javax.management.timer.Timer;

import gui.GameWindow;
import gui.ImageHandler;
import main.Main;

public class EntityHandler
{	
	public static final boolean Z_SORT = true;
	
	ImageHandler imgHandle;
	private Entity[] entities;
	private int maxEntities;
	private int entitiesDyn = 0;
	private int numEntities = 0;
	private boolean needUpdate = true;
	public long calculationTime = 0;
	
	public EntityHandler(int maxEntities)
	{
		this.maxEntities = maxEntities;
		entities = new Entity[maxEntities];
		imgHandle = Main.getGameWindow().getHandler();
	}
	
	@SuppressWarnings("unused")
	public void update()
	{
		if (! needUpdate)
		{
			calculationTime++;
			return;
		}
		
		needUpdate = false;
		
		Main.getGameWindow().getHandler().clearCache();
		
		if (Z_SORT)
		{
			try
			{
				Arrays.sort(entities, Entity.EntityZComparator);
			}
			catch (Exception e)
			{
				Main.println("error: failed to sort entities on z plane: " + e, Color.RED);
			}
		}
		
		for (int i = 0; i < entitiesDyn; i++)
		{
			if (entities[i] != null && entities[i].isAlive())
			{				
				if (entities[i].isVisible() && ! entities[i].isInvinsible())
				{
					if (entities[i].isOnScreen())
					{
						if (! occludedByAny(entities[i]) || ! Main.OCCLUSION)
						{
							if (entities[i].isStatic() && Main.DRAW_ONLY_DYNAMIC) {}
							else
							{
								Color c = Color.BLACK;
								if (entities[i].isDecal())
									c = Color.BLUE;
								else if (entities[i].isStatic())
								{
									if (entities[i].isSolid())
										c = Color.BLACK;
									else
										c = Color.GRAY;
								}
								else
									c = Color.RED;
								
								if (entities[i].isImage())
									Main.getGameWindow().addToCache((int) entities[i].getX(), (int) entities[i].getY(), entities[i].getImage(), c);
								else if (entities[i].isText())
									Main.getGameWindow().addToCache((int) entities[i].getX(), (int) entities[i].getY(), entities[i].getText());
									//Main.getGameWindow().addToCache((int) entities[i].getX(), (int) entities[i].getY(), 10, 10, c);				
							}
						}
					}
				}
			}
		}
		Main.getGameWindow().paint();
		
		while (Main.getGameWindow().updating) {System.out.print("");}
		
		calculationTime++;
	}
	
	public void addStaticEntity(Entity entity)
	{
		if (entity.isStatic())
		{
			for (int i = 0; i < maxEntities; i++)
			{
				if (entities[i] == null || ! entities[i].isAlive())
				{
					entities[i] = entity;
					if (i == entitiesDyn)
						entitiesDyn++;
					numEntities++;
					return;
				}
			}
			Main.println("Entity cache not big enough", Color.RED);
		}
		else
			Main.println("Cannot add a non-static entity to the static entity cache.", Color.RED);
	}
	
	public void addDynamicEntity(Entity entity)
	{
		if (! entity.isStatic())
		{
			for (int i = 0; i < maxEntities; i++)
			{
				if (entities[i] == null || ! entities[i].isAlive())
				{
					entities[i] = entity;
					if (i == entitiesDyn)
						entitiesDyn++;
					numEntities++;
					return;
				}
			}
			Main.println("Entity cache not big enough", Color.RED);
		}
		else
			Main.println("Cannot add a non-dynamic entity to the dynamic entity cache.", Color.RED);
	}
	
	public void removeEntity(int i)
	{
		entities[i] = null;
		numEntities--;
	}
	
	public void removeEntity(Entity e)
	{
		for (int i = 0; i < entitiesDyn; i++)
		{
			if (e == entities[i])
			{
				entities[i] = null;
				numEntities--;
				if (i >= i)
					entitiesDyn--;
			}
		}
	}
	
	public void removeAllEntities()
	{
		for (int i = 0; i < entities.length; i++)
		{
			entities[i] = null;
		}
		
		numEntities = 0;
		entitiesDyn = 0;
	}
	
	public void removeAllEntitiesExclude(Entity e)
	{
		int num = 0;
		int indexHigh = 0;
		for (int i = 0; i < entities.length; i++)
		{
			if (entities[i] != e)
			{
				entities[i] = null;
			}
			else
			{
				num = 1;
				indexHigh = i;
			}
		}
		
		numEntities = num;
		entitiesDyn = indexHigh;
	}
	
	public int getDynEntities()
	{
		return entitiesDyn;
	}
	
	public int getNumEntities()
	{
		return numEntities;
	}
	
	public Entity[] getEntities()
	{
		return entities;
	}
	
	public int getMaxEntities()
	{
		return maxEntities;
	}
	
	public Entity getEntityByName(String name)
	{
		for (int i = 0; i < entitiesDyn; i++)
		{
			if (entities[i] != null)
			{
				if (entities[i].getName().equals(name))
				{
					return entities[i];
				}
			}
		}
		return null;
	}
	
	public Entity getEntity(int i)
	{
		return entities[i];
	}
	
	public void printAllEntities()
	{
		Main.println("Num entities = " + numEntities);
		for (int i = 0; i < entitiesDyn; i++)
		{
			if (entities[i] != null)
			{
				Main.println(entities[i].toString());
			}
		}
	}
	
	public boolean occludedByAny(Entity entity)
	{
		if (! Main.OCCLUSION)
			return false;
		
		for (int i = 0; i < entitiesDyn; i++)
		{
			if (entities[i] != null)
			{
				if (! entities[i].equals(entity))
				{
					if (! entities[i].isTransparent())
					{
						if (occludedBy(entity, entities[i]))
						{
							return true;
						}
					}
				}
			}
		}
		return false;
	}
	
	private boolean occludedBy(Entity e1, Entity e2)
	{
		if (e1.getZBuffer() < e2.getZBuffer())
		{
			if (e1.getX() > e2.getX() && e1.getX2() < e2.getX2())
			{
				if (e1.getY() > e2.getY() && e1.getY2() < e2.getY2())
				{
					return true;
				}
			}
		}
		return false;
	}
	
	public BufferedImage buildStaticEntities()
	{
		BufferedImage buf = new BufferedImage(GameWindow.XRES_GL, GameWindow.YRES_GL, BufferedImage.TYPE_INT_ARGB);
		Graphics g = buf.getGraphics();
		for (int i = 0; i < entitiesDyn; i++)
		{
			
		}
		return null;
	}
	
	public void invokeUpdate()
	{
		needUpdate = true;
	}
}
