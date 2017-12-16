package core;

import java.awt.Color;
import java.awt.Image;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Random;

import javax.management.timer.Timer;

import core.guiElements.GuiElement;
import gui.ImageHandler;
import main.Main;
import utilities.ClockTimer;

public class EntityHandler
{	
	ImageHandler imgHandle;
	private Entity[] entities;
	private GuiElement[] guiElements;
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
	
	public void update()
	{
		if (! needUpdate)
		{
			calculationTime++;
			return;
		}
		
		needUpdate = false;
		
		Main.getGameWindow().getHandler().clearCache();
		
		Arrays.sort(entities, Entity.EntityZComparator);
		
		for (int i = 0; i < entitiesDyn; i++)
		{
			if (entities[i] != null && entities[i].isAlive())
			{				
				if (entities[i].isVisible() && ! entities[i].isInvinsible())
				{
					if (entities[i].isOnScreen())
					{
						if (! occludedByAny(entities[i]))
						{
							if (entities[i].isStatic() && Main.DRAW_ONLY_DYNAMIC) {}
							else
							{
								Color c = Color.BLACK;
								if (entities[i].isDecal())
									c = Color.BLUE;
								else if (entities[i].isStatic())
									c = Color.BLACK;
								else
									c = Color.RED;
								
								if (entities[i].isImage())
									Main.getGameWindow().addToCache((int) entities[i].getX(), (int) entities[i].getY(), entities[i].getImage(), c);
								else if (entities[i].isText())
									Main.getGameWindow().addToCache((int) entities[i].getX(), (int) entities[i].getY(), entities[i].getText());
								else if (ResourceHandler.defaultTextureImage != null)
									Main.getGameWindow().addToCache((int) entities[i].getActualX(), (int) entities[i].getActualY(), 
											ResourceHandler.defaultTextureImage.getScaledInstance((int) entities[i].getWidth(), (int) entities[i].getHeight(), Image.SCALE_DEFAULT), c);
									//Main.getGameWindow().addToCache((int) entities[i].getX(), (int) entities[i].getY(), 10, 10, c);				
							}
						}
					}
				}
			}
		}
		if (Main.getGuiHandler().getNumElements() > 0)
		{
			GuiElement elements[] = Main.getGuiHandler().getGuiElements();
			Arrays.sort(elements, GuiElement.GuiElementZComparator);
			for (int i = 0; i < Main.getGuiHandler().getElementsDyn(); i++)
			{
				if (elements[i] != null)
				{				
					if (elements[i].isVisible() && elements[i].isAlive())
					{
						if (elements[i].getImage() != null)
							Main.getGameWindow().addToCache((int) elements[i].getX(), (int) elements[i].getY(), elements[i].getImage(), Color.CYAN);
						else if (ResourceHandler.defaultTextureImage != null)
							Main.getGameWindow().addToCache((int) elements[i].getX(), (int) elements[i].getY(), 
									ResourceHandler.defaultTextureImage.getScaledInstance((int) elements[i].getWidth(), (int) elements[i].getHeight(), Image.SCALE_DEFAULT), Color.CYAN);
					}
				}
			}
		}
		Main.getGameWindow().paint();
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
	
	public void invokeUpdate()
	{
		needUpdate = true;
	}
}
