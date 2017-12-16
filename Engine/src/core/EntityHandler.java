/**
 * The EntityHandler class handles all of the Entities
 * inside the world.
 * 
 * @author Ethan Vrhel
 */

package core;

import java.awt.Color;
import java.awt.Image;
import java.util.Arrays;

import core.guiElements.GuiElement;
import main.Main;

public final class EntityHandler
{	
	/**
	 * Whether the engine will sort along the z plane
	 */
	public static final boolean Z_SORT = true;
	
	/**
	 * The current LOD value for rendering
	 */
	public int currLod = 3;
	
	private Entity[] entities;
	private int maxEntities;
	private int entitiesDyn = 0;
	private int numEntities = 0;
	
	/**
	 * How long the handler has spent calculating
	 */
	@Deprecated
	public long calculationTime = 0;
	
	/**
	 * The amount of skipped frames
	 */
	public long skippedFrames = 0;
	
	/**
	 * Whether the handler is updating
	 */
	public boolean updating = false;
	
	public EntityHandler(int maxEntities)
	{
		this.maxEntities = maxEntities;
		entities = new Entity[maxEntities];
	}
	
	/**
	 * Updates the renderer
	 */
	@SuppressWarnings({ "deprecation", "unused" })
	public synchronized void update()
	{
		calculationTime++;
		updating = true;
		//Main.println("updating...");
		
		if (Z_SORT)
		{
			Arrays.sort(entities, Entity.EntityZComparator);
		}
		
		Main.getGameWindow().getHandler().clearCache();
		
		for (int i = 0; i < entitiesDyn; i++)
		{
			if (entities[i] != null)
			{				
				if (entities[i].isAlive() && entities[i].isVisible() && ! entities[i].isInvinsible())
				{
					if ((entities[i].getLOD() <= currLod) || ! entities[i].isStatic())
					{
						if ((! occludedByAny(entities[i]) && entities[i].isOnScreen()) || ! Main.OCCLUSION)
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
								{
									Main.getGameWindow().addToCache((int) entities[i].getX(), (int) entities[i].getY(), entities[i].getImage(), true, c);
								}
								else if (entities[i].isText())
									Main.getGameWindow().addToCache((int) entities[i].getX(), (int) entities[i].getY(), entities[i].getText());
								else if (ResourceHandler.defaultTextureImage != null)
									Main.getGameWindow().addToCache((int) entities[i].getActualX(), (int) entities[i].getActualY(), 
											ResourceHandler.defaultTextureImage.getScaledInstance((int) entities[i].getWidth(), (int) entities[i].getHeight(), Image.SCALE_DEFAULT), true, c);
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
							Main.getGameWindow().addToCache((int) elements[i].getX(), (int) elements[i].getY(), elements[i].getImage(), false, Color.CYAN);
						else if (ResourceHandler.defaultTextureImage != null)
							Main.getGameWindow().addToCache((int) elements[i].getX(), (int) elements[i].getY(), 
									ResourceHandler.defaultTextureImage.getScaledInstance((int) elements[i].getWidth(), (int) elements[i].getHeight(), Image.SCALE_DEFAULT), false, Color.CYAN);
					}
				}
			}
		}
		Main.getGameWindow().paint();
		
		try
		{
			while (Main.getGameWindow().updating) 
			{
				Thread.yield();
			}
		}
		catch (Exception e)
		{}
	}
	
	/**
	 * Forces the array of Entities to be sorted
	 */
	public void forceSort()
	{
		Arrays.sort(entities, Entity.EntityZComparator);
	}
	
	/**
	 * Returns the small Entities
	 * 
	 * @return
	 */
	@Deprecated
	public Entity[] getSmallEntities()
	{
		if (entitiesDyn > 0)
			return entities;
		
		Entity[] temp = new Entity[entitiesDyn];
		for (int i = 0; i < temp.length; i++)
		{
			temp[i] = entities[i];
		}
		
		return temp;
	}
	
	/**
	 * Adds a static Entity and returns the added index
	 * -1 indicates a failure
	 * 
	 * @param entity
	 * @return
	 */
	public int addStaticEntity(Entity entity)
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
					entity.setAssociatedIndex(i);
					return i;
				}
			}
			Main.println("Entity cache not big enough", Color.RED);
			Main.exit(1);
		}
		else
			Main.println("Cannot add a non-static entity to the static entity cache.", Color.RED);
		
		return -1;
	}
	
	/**
	 * Adds a dynamic Entity and returns the added index
	 * -1 indicates a failure
	 * 
	 * @param entity
	 * @return
	 */
	public int addDynamicEntity(Entity entity)
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
					entity.setAssociatedIndex(i);
					//Main.println("adding...");
					return i;
				}
			}
			Main.println("Entity cache not big enough", Color.RED);
			Main.exit(1);
		}
		else
			Main.println("Cannot add a non-dynamic entity to the dynamic entity cache.", Color.RED);
		
		return -1;
	}
	
	/**
	 * Returns whether an Entity exists
	 * 
	 * @param e
	 * @return
	 */
	public boolean exists(Entity e)
	{
		if (e == null)
			return false;
		
		for (int i = 0; i < entitiesDyn; i++)
		{
			if (entities[i] != null)
			{
				if (entities[i].equals(e))
				{
					return true;
				}
			}
		}
		return false;
	}
	
	/**
	 * Removes an Entity from the cache
	 * 
	 * @param i
	 */
	public void removeEntity(int i)
	{
		entities[i] = null;
		numEntities--;
	}
	
	/**
	 * Removes an Entity from the cache
	 * 
	 * @param e
	 */
	public void removeEntity(Entity e)
	{
		if (e == null)
			return;
		for (int i = 0; i < entitiesDyn; i++)
		{
			if (entities[i] != null)
			{
				if (e.getID() == entities[i].getID())
				{
					entities[i] = null;
					numEntities--;
					if (i >= entitiesDyn)
						entitiesDyn--;
					return;
				}
			}
		}
	}
	
	/**
	 * Removes all Entities
	 */
	public void removeAllEntities()
	{
		for (int i = 0; i < entities.length; i++)
		{
			entities[i] = null;
		}
		
		numEntities = 0;
		entitiesDyn = 0;
	}
	
	/**
	 * Removes all Entities excluding an Entity
	 * 
	 * @param e
	 */
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
	
	/**
	 * Returns the dynamic array size of the cache
	 * 
	 * @return
	 */
	public int getDynEntities()
	{
		return entitiesDyn;
	}
	
	/**
	 * Returns the number of Entities
	 * 
	 * @return
	 */
	public int getNumEntities()
	{
		return numEntities;
	}
	
	/**
	 * Returns the array of Entities
	 * 
	 * @return
	 */
	public Entity[] getEntities()
	{
		return entities;
	}
	
	/**
	 * Returns the maximum Entities
	 * 
	 * @return
	 */
	public int getMaxEntities()
	{
		return maxEntities;
	}
	
	/**
	 * Returns an Entity by its name
	 * 
	 * @param name
	 * @return
	 */
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
	
	/**
	 * Gets an Entity from the cache
	 * 
	 * @param i
	 * @return
	 */
	public Entity getEntity(int i)
	{
		return entities[i];
	}
	
	/**
	 * Prints all Entities
	 */
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
	
	/**
	 * Returns if the Entity is occluded by another
	 * 
	 * @param entity
	 * @return
	 */
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
	
	/**
	 * Returns whether an Entity is occluded by another
	 * 
	 * @param e1
	 * @param e2
	 * @return
	 */
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
}
