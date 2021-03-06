package core;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Random;

import javax.imageio.ImageIO;

import gui.GameWindow;
import main.Main;
import utilities.AdvancedFilters;

/**
 * The Entity class is the base for all renderable
 * objects along with non-renderable ones.
 * 
 * @author Ethan Vrhel
 */
public class Entity
	implements Comparable<Entity>
{
	/**
	 * The constant for a static Entity
	 */
	public static final int STATIC = 0;
	
	/**
	 * The constant or a dynamic Entity
	 */
	public static final int DYNAMIC = 1;
	@Deprecated
	public static final int PHYSICS_TRIES = 0;
	
	private static int nextID = 0;					// The next Entity ID
	private static int entityCount = 0;				// The number of Entities
	@Deprecated
	public static final boolean SOLID = false;
	
	private int pubID;								// The Entity's public ID (used for server-client communications)
	private int id;									// The Entity's ID
	private String name;							// The Entity's name
	private double x;								// The Entity's x coordinate
	private double y;								// The Entity's y coordinate
	private double xvel = 0;						// The Entity's x velocity
	private double yvel = 0;						// The Entity's y velocity
	private int z;									// The Entity's z buffer
	private Entity parent;							// The Entity's parent
	private ArrayList<Entity> children = 
			new ArrayList<Entity>();				// The Entity's children
	private boolean obstinate = false;				// Whether the Entity can be moved by other Entities
	public int health;								// The Entity's health
	private String textureName;
	private Material material;
	@Deprecated
	public int maxHealth;							// The Entity's max health
	private boolean ignoreBounds = true;			// Whether the Entity should ignore the bounds of the screen
	@Deprecated
	private boolean isInvinsible;					// Whether the Entity is invisible
	private boolean isAlive;						// Whether the Entity is alive
	private boolean isVisible;						// Whether the Entity is visible
	private boolean isTransparent;					// Whether the Entity is transparent
	@Deprecated
	private boolean isDecal;						// Whether the Entity is a decal
	protected int type;								// The Entity's type
	private boolean isSolid;						// The Entity's solidity
	private double width = 0;						// The Entity's width
	private double height = 0;						// The Entity's height
	private boolean isImage = false;				// Whether the Entity is an Image
	private BufferedImage image;					// The Entity's Image
	private File imageFile;							// The Entity's image file
	private int mass = 1;							// The Entity's mass
	private int xs = 1;								// The Entity's x scale
	private int ys = 1;								// The Entity's y scale
	private String text = "";						// The Entity's text
	private boolean isText = false;					// Whether the Entity is text
	private Entity collisionEntity;					// The Entity's collided Entity
	private boolean isKinetic = true;				// Whether the Entity is kinetic
	private boolean canPush = false;				// Whether the Entity can push other entities
	@Deprecated
	protected boolean beingPushed = false;			// Whether the Entity is being pushed
	@Deprecated	
	protected int rot;								// The Entity's rotation
	protected boolean collisions;					// Whether the Entity has collision
	protected Animation animation;					// The Entity's tied Animation
	protected int associatedIndex = -1;				// The Entity's associated index
	protected double friction = 					// The Entity's personal friction (-1 is controlled by the PhysicsHandler)
			Main.getPhysicsHandler().getFriction();	
	protected double gravity = 						// The Entity's personal gravity (-1 is controlled by the PhysicsHandler)
			Main.getPhysicsHandler().getGravity();
	protected boolean updateWithPhysics = true;		// Whether the Entity should update with the PhysicsHandler
	protected boolean renderInReflections = true;	// Whether the Entity should render in reflections
	protected int LOD = 0;							// The Entity's LOD value
	
	public static final Comparator<Entity> EntityZComparator = new Comparator<Entity>()
	{
		public int compare(Entity entity1, Entity entity2)
		{
			if (entity1 != null && entity2 != null)
				return entity1.compareTo(entity2);
			else
				return 0;
		}
	};	
	
	public Entity()
	{
		id = nextID;
		pubID = -1;
	}
	
	public Entity(int type, boolean solid, double x, double y, int w, int h, int maxHealth)
	{
		id = nextID;
		pubID = -1;
		nextID++;
		this.type = type;
		isSolid = solid;
		if (type == STATIC)
			name = "Unnamed Static Entity";
		else
			name = "Unnamed Dynamic Entity";
		this.x = x * Main.resolutionScaleX;
		this.y = y * Main.resolutionScaleY;
		width = w * Main.resolutionScaleX;
		height = h * Main.resolutionScaleY;
		z = 0;
		this.maxHealth = maxHealth;
		health = maxHealth;
		isInvinsible = false;
		isAlive = true;
		isVisible = false;
		collisions = true;
		addedEntity(this);
	}
	
	public Entity(int type, double x, double y, String text)
	{
		id = nextID;
		pubID = -1;
		nextID++;
		this.type = type;
		this.x = x;
		this.y = y;
		this.text = text;
		collisions = true;
		isText = true;
	}
	
	public Entity(int type, boolean solid, String name, double x, double y, int w, int h, int maxHealth)
	{
		id = nextID;
		pubID = -1;
		nextID++;
		this.type = type;
		isSolid = solid;
		this.name = name;
		this.x = x * Main.resolutionScaleX;
		this.y = y * Main.resolutionScaleY;
		width = w * Main.resolutionScaleX;
		height = h * Main.resolutionScaleY;
		z = 0;
		this.maxHealth = maxHealth;
		health = maxHealth;
		isInvinsible = false;
		isAlive = true;
		collisions = true;
		/*
		if (Main.DEBUG && Main.DRAW_TRIGGERS)
		{
			z = Integer.MAX_VALUE;
			isVisible = true;
			isImage = true;
			BufferedImage img = new BufferedImage((int) width, (int) height, BufferedImage.TYPE_INT_ARGB);
			Graphics g = img.getGraphics();
			g.drawImage(ResourceHandler.debugTextureImage.getScaledInstance((int) width, (int) height, Image.SCALE_FAST), 
					0, 0, null);
			image = img;
		}
		*/
		addedEntity(this);
	}
	
	public Entity(int type, boolean solid, String name, double x, double y, int maxHealth)
	{
		id = nextID;
		pubID = -1;
		nextID++;
		this.type = type;
		this.name = name;
		this.type = type;
		isSolid = solid;
		this.x = x * Main.resolutionScaleX;
		this.y = y * Main.resolutionScaleY;
		z = 0;
		this.maxHealth = maxHealth;
		health = maxHealth;
		isInvinsible = false;
		isAlive = true;
		isVisible = false;
		collisions = true;
		addedEntity(this);
	}
	
	public Entity(int type, boolean solid, String name, double x, double y, int z, int w, int h, int maxHealth, boolean isInvinsible)
	{
		id = nextID;
		pubID = -1;
		nextID++;
		this.type = type;
		this.name = name;
		this.type = type;
		isSolid = solid;
		this.x = x;
		this.y = y;
		this.z = z;
		width = w;
		height = h;
		this.maxHealth = maxHealth;
		health = maxHealth;
		this.isInvinsible = isInvinsible;
		isAlive = true;
		isVisible = false;
		if (image == null)
			isImage = false;
		else
		{
			isImage = true;
		}
		collisions = true;
		addedEntity(this);
	}
	
	public Entity(int type, Image image, int xs, int ys, boolean solid, double x, double y, int z, int maxHealth, boolean isInvinsible)
	{
		id = nextID;
		pubID = -1;
		nextID++;
		isImage = true;
		BufferedImage img = new BufferedImage((int) xs, (int) ys, BufferedImage.TYPE_INT_ARGB);
		Graphics g = img.getGraphics();
		g.drawImage(image.getScaledInstance((int) xs, (int) ys, Image.SCALE_FAST), 
				0, 0, null);
		img = AdvancedFilters.toCompatibleImage(img);
		this.image = img;
		this.type = type;
		isSolid = solid;
		if (type == STATIC)
			name = "Unnamed Static Entity";
		else
			name = "Unnamed Dynamic Entity";
		this.x = x;
		this.y = y;
		this.z = z;
		this.maxHealth = maxHealth;
		health = maxHealth;
		this.isInvinsible = isInvinsible;
		isAlive = true;
		isVisible = true;
		this.xs = xs;
		this.ys = ys;
		this.width = xs;
		this.height = ys;
		collisions = true;
		addedEntity(this);
	}
	
	public Entity(int type, BufferedImage image, int xs, int ys, boolean solid, double x, double y, int z, int maxHealth, boolean isInvinsible)
	{
		id = nextID;
		pubID = -1;
		nextID++;
		isImage = true;
		this.image = image;
		this.type = type;
		isSolid = solid;
		if (type == STATIC)
			name = "Unnamed Static Entity";
		else
			name = "Unnamed Dynamic Entity";
		this.x = x * Main.resolutionScaleX;
		this.y = y * Main.resolutionScaleY;
		this.z = z;
		this.maxHealth = maxHealth;
		health = maxHealth;
		this.isInvinsible = isInvinsible;
		isAlive = true;
		isVisible = true;
		this.xs = (int) Math.ceil(xs * Main.resolutionScaleX);
		this.ys = (int) Math.ceil(ys * Main.resolutionScaleY);
		this.width = xs * Main.resolutionScaleX;
		this.height = ys * Main.resolutionScaleY;
		collisions = true;
		addedEntity(this);
	}
	
	public Entity(int type, File image, int xs, int ys, boolean solid, double x, double y, int z, int maxHealth, boolean isInvinsible)
	{
		id = nextID;
		pubID = -1;
		nextID++;
		imageFile = image;
		
		isImage = false;
		if (image != null)
		{
			if (image.exists())
			{
				try 
				{
					BufferedImage img = new BufferedImage((int) xs, (int) ys, BufferedImage.TYPE_INT_ARGB);
					Graphics g = img.getGraphics();
					g.drawImage((ImageIO.read(image).getScaledInstance((int) xs, (int) ys, Image.SCALE_FAST)), 
							0, 0, null);
					img = AdvancedFilters.toCompatibleImage(img);
					this.image = img;
					isImage = true;
				} 
				catch (IOException e)
				{
					e.printStackTrace();
				}
			}
		}
		this.type = type; 
		isSolid = solid;
		if (type == STATIC)
			name = "Unnamed Static Entity";
		else
			name = "Unnamed Dynamic Entity";
		this.x = x;
		this.y = y;
		this.z = z;
		this.maxHealth = maxHealth;
		health = maxHealth;
		this.isInvinsible = isInvinsible;
		isAlive = true;
		isVisible = true;
		this.xs = (int) (xs * Main.resolutionScaleX);
		this.ys = (int) (ys * Main.resolutionScaleY);
		collisions = true;
		addedEntity(this);
	}
	
	public Entity(int type, File image, int xs, int ys, boolean solid, String name, double x, double y, int z, int w, int h, int maxHealth, boolean isInvinsible)
	{
		id = nextID;
		pubID = -1; 
		nextID++;
		imageFile = image;
		
		isImage = false;
		if (image != null)
		{
			if (image.exists())
			{
				try
				{
					BufferedImage img = new BufferedImage((int) xs, (int) ys, BufferedImage.TYPE_INT_ARGB);
					Graphics g = img.getGraphics();
					g.drawImage((ImageIO.read(image).getScaledInstance((int) xs, (int) ys, Image.SCALE_FAST)), 
							0, 0, null);
					img = AdvancedFilters.toCompatibleImage(img);
					this.image = img;					
					isImage = true;
				} 
				catch (IOException e) 
				{
					e.printStackTrace();
				}
			}
		}
		this.type = type;
		this.name = name;
		this.type = type;
		isSolid = solid;
		this.x = x * Main.resolutionScaleX;
		this.y = y * Main.resolutionScaleY;
		this.z = z;
		width = w * Main.resolutionScaleX;
		height = h * Main.resolutionScaleY;
		this.maxHealth = maxHealth;
		health = maxHealth;
		this.isInvinsible = isInvinsible;
		isAlive = true;
		isVisible = true;
		if (image == null)
			isImage = false;
		else
		{
			isImage = true;
		}
		this.xs = (int) (xs * Main.resolutionScaleX);
		this.ys = (int) (ys * Main.resolutionScaleY);
		collisions = true;
		addedEntity(this);
	}
	
	/**
	 * Adds an Entity
	 * 
	 * @param entity
	 */
	@Deprecated
	private static void addedEntity(Entity entity)
	{
		entityCount++;	
	}
	
	/**
	 * Returns the number of Entities
	 * 
	 * @return
	 */
	public static int getNumEntities()
	{
		return entityCount;
	}
	
	@Override
	public String toString()
	{
		return name + ": (" + x + ", " + y + ", " + z + ")";
	}
	
	/**
	 * Whether the Entity is invisible
	 * 
	 * @return
	 */
	@Deprecated
	public boolean isInvinsible()
	{
		return isInvinsible;
	}
	
	/**
	 * Sets whether the Entity is alive
	 * 
	 * @param isAlive
	 */
	public void setAlive(boolean isAlive)
	{
		this.isAlive = isAlive;
	}
	
	/**
	 * Returns whether the Entity is alive or not
	 * @return
	 */
	public boolean isAlive()
	{
		return isAlive;
	}
	
	/**
	 * Returns whether the Entity is visible
	 * 
	 * @return
	 */
	public boolean isVisible()
	{
		return isVisible;
	}
	
	/**
	 * Sets the Entity's visiblity
	 * 
	 * @param state
	 */
	public void setVisible(boolean state)
	{
		isVisible = state;
	}
	
	/**
	 * Returns the Entity's static-ness
	 * 
	 * @return
	 */
	public boolean isStatic()
	{
		if (type == STATIC)
			return true;
		else if (type == DYNAMIC)
			return false;
		else
			return false;
				
	}
	
	/**
	 * Returns the Entity's x coordinate
	 * 
	 * @return
	 */
	public double getX()
	{
		return x;
	}
	
	/** 
	 * Returns the Entity's y coordinate
	 * 
	 * @return
	 */
	public double getY()
	{
		return y;
	}
	
	/**
	 * Sets the Entity's x coordinate
	 * 
	 * @param x
	 */
	public void setX(double x)
	{
		if (type == DYNAMIC)
		{
			this.x = x * Main.resolutionScaleX;
		}
		else
			Main.println(name + ": Cannot move non-dynamic entities", Color.RED);
	}
	
	/**
	 * Sets the Entity's y coordinate
	 * 
	 * @param y
	 */
	public void setY(double y)
	{
		if (type == DYNAMIC)
		{
			this.y = y * Main.resolutionScaleY;
		}
		else
			Main.println(name + ": Cannot move non-dynamic entities", Color.RED);
	}
	
	/**
	 * Translates the Entity on the x plane
	 * 
	 * @param x
	 */
	@SuppressWarnings("deprecation")
	public void translateX(double x)
	{
		if (! Main.isMultiplayer && Main.gamePaused)
			return;
		
		if (x == 0)
			return;
		
		if (children.size() > 0)
			translateChildren(x, 0);
		
		if (type == DYNAMIC)
		{
			this.x += x * Main.resolutionScaleX;
			if (! ignoreBounds)
			{
				if (this.x < Main.getGameWindow().getLeftBounds() || this.x + width > Main.getGameWindow().getRightBounds())
				{
					this.x -= x * Main.resolutionScaleX;
				}
			}
			
			if (canPush)
			{
				Entity e = null;
				if (gravity > 0)
					e = new Entity(DYNAMIC, true, this.x / Main.resolutionScaleX, 
							y / Main.resolutionScaleY - 2, (int) width, 2, 100);
				else
					e = new Entity(DYNAMIC, true, this.x / Main.resolutionScaleX, 
							(getY2() + 2) / Main.resolutionScaleY, (int) width, 2, 100);
				Entity coll = e.getCollided();
				if (coll != null)
				{
					if (coll.getMass() < mass && ! coll.obstinate)
						coll.translateX(x);
				}
			}
				
			//int type, boolean solid, double x, double y, int w, int h, int maxHealth
			if (hasCollided())
			{				
				xvel = 0;
				this.x -= x * Main.resolutionScaleX;
				
				this.x += x / 2;
				if (hasCollided())
				{
					this.x -= x / 2;
					this.x += x / 4;
					if (hasCollided())
					{
						this.x -= x / 4;
					}
				}

				if (this.mass > collisionEntity.getMass())
				{
					beingPushed = true;
					double force = this.mass * x / collisionEntity.getMass() * 1;
					collisionEntity.translateX(force);
					if (force != 0 && this.isOnScreen())
					{
						if (Main.lossyRendering && (new Random()).nextInt(Main.lossFactor) != 0)
							return;
					}
				}	
				else
					beingPushed = false;
			}
			else
			{
				beingPushed = false;
			}
		}
	}
	
	/**
	 * Translates the Entity on the y plane
	 * 
	 * @param y
	 */
	@SuppressWarnings("deprecation")
	public void translateY(double y)
	{		
		if (! Main.isMultiplayer && Main.gamePaused)
			return;
		
		if (y == 0)
			return;
		
		if (children.size() > 0)
			translateChildren(0, y);
		
		if (type == DYNAMIC)
		{			
			this.y += y * Main.resolutionScaleY;
			if (! ignoreBounds)
			{
				if (this.y < Main.getGameWindow().getTopBounds() || this.y + height > Main.getGameWindow().getBottomBounds())
				{
					this.y -= y * Main.resolutionScaleY;
				}
			}

			if (hasCollided())
			{
				this.y -= y * Main.resolutionScaleY;
				yvel = 0;
				
				this.y += y / 2;
				if (hasCollided())
				{
					this.y -= y / 2;
					this.y += y / 4;
					if (hasCollided())
					{
						this.y -= y / 4;
					}
				}
				
				if (this.mass > collisionEntity.getMass())
				{
					beingPushed = true;
					double force = this.mass * y / collisionEntity.getMass() * 1;
					collisionEntity.translateY(force);
					if (force != 0 && this.isOnScreen())
					{
						if (Main.lossyRendering && (new Random()).nextInt(Main.lossFactor) != 0)
							return;
					}
				}
				else
					beingPushed = false;
			}
			else
			{
				beingPushed = false;
			}
		}
	}
	
	/**
	 * Sets the Entity's x velocity
	 * 
	 * @param xvel
	 */
	public void setXVel(double xvel)
	{
		this.xvel = xvel;
	}
	
	/**
	 * Sets the Entity's y velocity
	 * @param yvel
	 */
	public void setYVel(double yvel)
	{
		this.yvel = yvel;
	}
	
	/**
	 * Returns the Entity's x velocity
	 * 
	 * @return
	 */
	public double getXVel()
	{
		return xvel;
	}
	
	/**
	 * Returns the Entity's y velocity
	 * 
	 * @return
	 */
	public double getYVel()
	{
		return yvel;
	}
	
	/**
	 * Translates the Entity
	 * 
	 * @param x
	 * @param y
	 */
	public void translate(double x, double y)
	{
		if (type == DYNAMIC)
		{
			translateX(x);
			translateY(y);
		}
		else
			Main.println(name +  ": Cannot move non-dynamic entities", Color.RED);
	}
	
	/** 
	 * Return whether the Entity has collided
	 * 
	 * @return
	 */
	public boolean hasCollided()
	{
		int dynEntities = Main.getEntityHandler().getDynEntities();
		Entity entities[] = Main.getEntityHandler().getEntities();
		if (! this.isSolid)
			return false;
		if (! this.collisions)
			return false;
		
		for (int i = 0; i < dynEntities; i++)
		{
			if (entities[i] != this)
			{
				if (entities[i] != null)
				{
					if (hasCollided(entities[i]))
					{
						collisionEntity = entities[i];
						return true;
					}
				}
			}
		}
		return false;
	}
	
	/**
	 * Returns the collided Entity, and returns null
	 * if there is no collisions.
	 * 
	 * @return
	 */
	public Entity getCollided()
	{
		int dynEntities = Main.getEntityHandler().getDynEntities();
		Entity entities[] = Main.getEntityHandler().getEntities();
		if (! this.isSolid)
			return null;
		if (! this.collisions)
			return null;
		
		for (int i = 0; i < dynEntities; i++)
		{
			if (entities[i] != this)
			{
				if (entities[i] != null)
				{
					if (hasCollided(entities[i]))
					{
						collisionEntity = entities[i];
						return entities[i];
					}
				}
			}
		}
		return null;
	}
	
	/**
	 * Returns the collisions of an Entity
	 * 
	 * @param ignore
	 * @return
	 */
	public Entity[] getCollisions(boolean ignore)
	{
		int dynEntities = Main.getEntityHandler().getDynEntities();
		Entity entities[] = Main.getEntityHandler().getEntities();
		/*
		if (! this.isSolid)
			return null;
		if (! this.collisions)
			return null;
		*/
		
		int collisions = 0;
		try
		{
			for (int i = 0; i < dynEntities; i++)
			{
				if (entities[i] != this)
				{
					if (entities[i] != null)
					{
						if (hasCollidedIgnoreSolid(entities[i]))
						{
							collisions++;
						}
					}
				}
			}
		}
		catch (Exception e)
		{
			return null;
		}
		if (collisions == 0)
			return null;
		Entity[] collided = new Entity[collisions];
		int j = 0;
		for (int i = 0; i < dynEntities; i++)
		{
			if (entities[i] != this)
			{
				if (entities[i] != null)
				{
					if (j >= collided.length)
						break;
					
					if (hasCollidedIgnoreSolid(entities[i]))
					{
						collided[j] = entities[i];
						j++;
					}
				}
			}
		}
		return collided;
	}
	
	/**
	 * Returns whether the Entity has collided with only
	 * a Static Entity
	 * @return
	 */
	public boolean hasCollidedOnlyStatic()
	{
		int dynEntities = Main.getEntityHandler().getDynEntities();
		Entity entities[] = Main.getEntityHandler().getEntities();
		if (! this.isSolid || ! this.isStatic())
			return false;
		if (! this.collisions)
			return false;
		
		for (int i = 0; i < dynEntities; i++)
		{
			if (entities[i] != this)
			{
				if (entities[i] != null)
				{
					if (hasCollided(entities[i]) && entities[i].isStatic())
					{
						collisionEntity = entities[i];
						return true;
					}
				}
			}
		}
		return false;
	}
	
	/**
	 * Returns whether the Entity has collided with only
	 * a Dynamic Entity
	 * @return
	 */
	public boolean hasCollidedOnlyDynamic()
	{
		int dynEntities = Main.getEntityHandler().getDynEntities();
		Entity entities[] = Main.getEntityHandler().getEntities();
		if (! this.isSolid || this.isStatic())
			return false;
		if (! this.collisions)
			return false;
		
		for (int i = 0; i < dynEntities; i++)
		{
			if (entities[i] != this)
			{
				if (entities[i] != null)
				{
					if (hasCollided(entities[i]) && ! entities[i].isStatic())
					{
						return true;
					}
				}
			}
		}
		return false;
	}
	
	/** 
	 * Returns whether the Entity has collided with
	 * the current navigation mesh
	 * 
	 * @return
	 */
	public boolean hasCollidedNav()
	{
		int dynEntities = Main.getEntityHandler().getDynEntities();
		Entity entities[] = Main.getEntityHandler().getEntities();
		if (! this.isSolid || ! this.isStatic())
			return false;
		if (! this.collisions)
			return false;
		
		for (int i = 0; i < dynEntities; i++)
		{
			if (entities[i] != this)
			{
				if (entities[i] != null)
				{
					if (hasCollidedNav(entities[i]) && entities[i].isStatic())
					{
						collisionEntity = entities[i];
						return true;
					}
				}
			}
		}
		return false;
	}
	
	/**
	 * Returns whether the Entity has collided with
	 * the current navigation mesh
	 * 
	 * @param entity
	 * @return
	 */
	public boolean hasCollidedNav(Entity entity)
	{			
		if (! entity.isSolid)
			return false;
		if (! this.collisions)
			return false;
		
		if (this.getY2() < entity.getY() || y > entity.getY2())
			return false;
		else if (this.getX2() < entity.getX() || x > entity.getX2())
			return false;
		
		return true;
	}
	
	/**
	 * Returns whether the Entity has collided
	 * 
	 * @param entity
	 * @return
	 */
	public boolean hasCollided(Entity entity)
	{			
		if (! entity.isSolid)
			return false;
		if (! this.collisions)
			return false;
		
		if (this.getY2() <= entity.getY() || y >= entity.getY2())
			return false;
		else if (this.getX2() <= entity.getX() || x >= entity.getX2())
			return false;
		
		return true;
	}
	
	/**
	 * Returns whether the Entity has collided with another
	 * ignoring solidity
	 * 
	 * @param entity
	 * @return
	 */
	public boolean hasCollidedIgnoreSolid(Entity entity)
	{		
		Thread.yield();
		if (this.getY2() <= entity.getY() || y >= entity.getY2())
			return false;
		else if (this.getX2() <= entity.getX() || x >= entity.getX2())
			return false;
		
		return true;
	}
	
	/**
	 * Returns whether a point has collided with an Entity
	 * 
	 * @param xCol
	 * @param yCol
	 * @return
	 */
	public boolean hasPointCollided(double xCol, double yCol)
	{
		if (! isSolid)
			return false;
		if (! this.collisions)
			return false;
		
		if (getY2() <= yCol || y >= yCol)
			return false;
		else if (getX2() <= xCol || x >= xCol)
			return false;
		
		return true;
	}
	
	/**
	 * Returns whether a point has collided, ignoring dynamic
	 * Entities
	 * 
	 * @param xCol
	 * @param yCol
	 * @return
	 */
	public boolean hasPointCollidedNoDynamic(double xCol, double yCol)
	{
		if (! isSolid)
			return false;
		if (! isStatic())
			return false;
		if (! this.collisions)
			return false;
		
		if (getY2() <= yCol || y >= yCol)
			return false;
		else if (getX2() <= xCol || x >= xCol)
			return false;
		
		return true;
	}
	
	/**
	 * Returns whether a point has collided, ignore static 
	 * Entities
	 * 
	 * @param xCol
	 * @param yCol
	 * @return
	 */
	public boolean hasPointCollidedNoStatic(double xCol, double yCol)
	{
		if (! isSolid)
			return false;
		if (isStatic())
			return false;
		if (! this.collisions)
			return false;
		
		if (getY2() <= yCol || y >= yCol)
			return false;
		else if (getX2() <= xCol || x >= xCol)
			return false;
		
		return true;
	}
	
	/**
	 * Returns the name of the Entity
	 * 
	 * @return
	 */
	public String getName()
	{
		return name;
	}
	
	/**
	 * Returns the z buffer of the Entity
	 * 
	 * @return
	 */
	public int getZBuffer()
	{
		return z;
	}
	
	/**
	 * Sets the z buffer of the Entity
	 * 
	 * @param z
	 */
	public void setZBuffer(int z)
	{
		this.z = z;
	}

	public int compareTo(Entity compareEntity) 
	{
		int compareZVal = compareEntity.getZBuffer();
		if (compareZVal == z)
		{
			if (compareEntity.isStatic() && ! isStatic())
			{
				return 1;
			}
			else
			{
				if (compareEntity.getName() != null && name != null)
					return compareEntity.getName().length() - name.length();
				else
					return 0;
			}
		}
		return this.z - compareZVal;
	}
	
	/**
	 * Returns whether the Entity is an Image
	 * 
	 * @return
	 */
	public boolean isImage() 
	{
		return isImage;
	}
	
	/**
	 * Returns the Entity's BufferedImage
	 * @return
	 */
	public BufferedImage getBufferedImage()
	{
		return image;
	}
	
	/**
	 * Returns the Image of the Entity
	 * 
	 * @return
	 */
	public Image getImage()
	{
		int newXs = (int) Math.ceil(xs);
		int newYs = (int) Math.ceil(ys);
		if (newXs > 0 && newYs > 0)
			return image.getScaledInstance(newXs, newYs, Image.SCALE_FAST);
		else
			return image.getScaledInstance(xs, ys, Image.SCALE_DEFAULT);
	}
	
	/**
	 * Returns the height of the Entity
	 * 
	 * @return
	 */
	public double getHeight()
	{
		return height;
	}
	
	/**
	 * Returns the width of the Entity
	 * 
	 * @return
	 */
	public double getWidth()
	{
		return width;
	}
	
	/**
	 * Returns the bottom left x coordinate of the Entity
	 * 
	 * @return
	 */
	public double getX2()
	{
		return x + width - 1;
	}
	
	/**
	 * Returns the top right y coordinate of the Entity
	 * 
	 * @return
	 */
	public double getY2()
	{
		return y + height;
	}
	
	/**
	 * Returns the Entity's solidity
	 * 
	 * @return
	 */
	public boolean isSolid()
	{
		return isSolid;
	}
	
	/**
	 * Sets the Entity's solidity
	 * 
	 * @param solid
	 */
	public void setSolid(boolean solid)
	{
		isSolid = solid;
	}
	
	/**
	 * Sets whether the Entity should ignore the
	 * screen bounds
	 * 
	 * @param set
	 */
	public void ignoreBounds(boolean set)
	{
		ignoreBounds = set;
	}
	
	/**
	 * Returns whether the Entity is on screen
	 * 
	 * @return
	 */
	public boolean isOnScreen()
	{
		if (x + Main.getGameWindow().getCameraX() - xs < GameWindow.XRES_GL - Main.getGameWindow().clipX / 2 &&
				x + Main.getGameWindow().getCameraX() + xs > Main.getGameWindow().clipX / 2)
		{
			if (y + Main.getGameWindow().getCameraY() - ys < GameWindow.YRES_GL - Main.getGameWindow().clipY / 2 && 
					y + Main.getGameWindow().getCameraY() + ys > Main.getGameWindow().clipY / 2)
			{
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Sets the Entity's transparency
	 * 
	 * @param set
	 */
	public void setTransparancy(boolean set)
	{
		isTransparent = set;
	}
	
	/**
	 * Returns whether the Entity is transparent
	 * 
	 * @return
	 */
	public boolean isTransparent()
	{
		return isTransparent;
	}
	
	/**
	 * Sets the Entity's mass
	 * 
	 * @param mass
	 */
	public void setMass(int mass)
	{
		this.mass = mass;
	}
	
	/**
	 * Returns the Entity's mass
	 * 
	 * @return
	 */
	public int getMass()
	{
		return mass;
	}
	
	/**
	 * Returns whether the Entity is a decal
	 * 
	 * @return
	 */
	@Deprecated
	public boolean isDecal()
	{
		return isDecal;
	}
	
	/**
	 * Sets whether the Entity is a decal
	 * 
	 * @param isDecal
	 */
	@Deprecated
	public void setDecal(boolean isDecal)
	{
		this.isDecal = isDecal;
		if (isDecal)
		{
			isSolid = false;
			isTransparent = true;
			z = -1;
		}
		else
		{
			z = 0;
		}
	}
	
	/**
	 * Sets the Entity's Image file
	 * 
	 * @param file
	 */
	public void setImageFile(File file)
	{
		imageFile = file;
	}
	
	/**
	 * Returns the Entity's Image file
	 * 
	 * @return
	 */
	public File getImageFile()
	{
		return imageFile;
	}
	
	/**
	 * Sets the Entity's Image
	 * 
	 * @param img
	 */
	public void setImage(Image img)
	{
		BufferedImage buf = new BufferedImage(xs, ys, BufferedImage.TYPE_INT_ARGB);
		Graphics g = buf.getGraphics();
		g.drawImage(img.getScaledInstance(xs, ys, Image.SCALE_FAST), 0, 0, null);
		image = buf;
	}
	
	/**
	 * Sets the Entity's Image
	 * 
	 * @param img
	 */
	public void setBufferedImage(BufferedImage img)
	{
		image = img;
	}
	
	/**
	 * Returns the Entity's attributes
	 * 
	 * @return
	 */
	public String getAttributes()
	{
		String construct = type + " " + imageFile + " " + xs + " " + ys + " "+ isSolid + " \"" + name + "\" " + 
						   getActualX() + " " + getActualY() + " " + z + " " + width + " " + height + " " + maxHealth + " " + isInvinsible;
		String extra = isTransparent + " " + isDecal + " " + mass + " " + ignoreBounds;
		return construct + " " + extra;
	}
	
	//public Entity(int type, Image image, boolean solid, String name, double x, double y, int z, int w, int h, int maxHealth, boolean isInvinsible)
	/**
	 * Creates a new Entity from a String
	 * 
	 * @param str
	 * @return
	 */
	public static Entity newFromString(String str)
	{
		String tokens[];
		String delims = " ";
		tokens = str.split(delims);
		
		int type = Integer.parseInt(tokens[0]);
		String name = tokens[5];
		int offset = 0;
		if (tokens[5].contains("\""))
		{
			int i = 0;
			int startChar;
			int endChar;
			int passedTokens = 0;
			while (str.charAt(i) != '"')
			{
				i++;
			}
			startChar = i;
			i++;
			while (str.charAt(i) != '"')
			{
				i++;
				if (str.charAt(i) == ' ')
					passedTokens++;
			}
			endChar = i;
			name = str.substring(startChar + 1, endChar);
			offset = passedTokens;
		}
		Entity ne = null;
		try
		{
		File image;
		if (! tokens[1].equals("null"))
			image = new File(tokens[1]);	
		else
			image = null;
		
		ne = new Entity(type, image, (int) Double.parseDouble(tokens[2]), (int) Double.parseDouble(tokens[3]),
				               Boolean.parseBoolean(tokens[4]), name, Double.parseDouble(tokens[6 + offset]), Double.parseDouble(tokens[7 + offset]), Integer.parseInt(tokens[8 + offset]),
				               (int) Double.parseDouble(tokens[9 + offset]), (int) Double.parseDouble(tokens[10 + offset]), Integer.parseInt(tokens[11 + offset]), Boolean.parseBoolean(tokens[12 + offset]));
		}
		catch (Exception e)
		{
			Main.println("read error " + e, Color.RED);
		}
		ne.setTransparancy(Boolean.parseBoolean(tokens[13 + offset]));
		ne.setDecal(Boolean.parseBoolean(tokens[14 + offset]));
		ne.setMass(Integer.parseInt(tokens[15 + offset]));
		ne.ignoreBounds(Boolean.parseBoolean(tokens[16 + offset]));		

		return ne;
	}
	
	/**
	 * Returns the actual x value of the Entity
	 * 
	 * @return
	 */
	public double getActualX()
	{
		return x / Main.resolutionScaleX;
	}
	
	/**
	 * Returns the actual y value of the Entity
	 * 
	 * @return
	 */
	public double getActualY()
	{
		return y / Main.resolutionScaleY;
	}
	
	/**
	 * Returns the actual width of the Entity
	 * 
	 * @return
	 */
	public double getActualWidth()
	{
		return width / Main.resolutionScaleX;
	}
	
	/**
	 * Returns the actual height of the Entity
	 * 
	 * @return
	 */
	public double getActualHeight()
	{
		return height / Main.resolutionScaleY;
	}
	
	/**
	 * Returns the Entity's x scale
	 * 
	 * @return
	 */
	public double getScaleX()
	{
		return xs;
	}
	
	/**
	 * Returns the Entity's y scale
	 * 
	 * @return
	 */
	public double getScaleY()
	{
		return ys;
	}
	
	/**
	 * Returns the Entity's actual x scale
	 * 
	 * @return
	 */
	public double getActualScaleX()
	{
		return xs / Main.resolutionScaleX;
	}
	
	/**
	 * Returns the Entity's actual y scale
	 * 
	 * @return
	 */
	public double getActualScaleY()
	{
		return ys / Main.resolutionScaleY;
	}
	
	/**
	 * Returns the Entity's actual Image
	 * 
	 * @return
	 */
	public Image getActualImage()
	{
		return image.getScaledInstance((int) (xs / Main.resolutionScaleX), (int) (ys / Main.resolutionScaleY), Image.SCALE_DEFAULT);
	}
	
	/**
	 * Returns whether the Entity is text
	 * 
	 * @return
	 */
	public boolean isText()
	{
		return isText;
	}
	
	/**
	 * Returns the Entity's text
	 * 
	 * @return
	 */
	public String getText()
	{
		return text;
	}
	
	/**
	 * Returns whether the Entity is kinetic
	 * 
	 * @return
	 */
	public boolean isKinetic()
	{
		return isKinetic;
	}
	
	/**
	 * Sets whether the Entity is kinetic
	 * @param isKinetic
	 */
	public void setKinetic(boolean isKinetic)
	{
		this.isKinetic = isKinetic;
	}
	
	/**
	 * Returns whether the Entity is being pushed
	 * 
	 * @return
	 */
	@Deprecated
	public boolean beingPushed()
	{
		return beingPushed;
	}
	
	/**
	 * Returns whether the Entity has collisions
	 * 
	 * @return
	 */
	public boolean hasCollisions()
	{
		return collisions;
	}
	
	/**
	 * Sets the Entity's collisions
	 * 
	 * @param collisions
	 */
	public void setCollisions(boolean collisions)
	{
		this.collisions = collisions;
	}
	
	/**
	 * Sets the Entity's name
	 * 
	 * @param name
	 */
	public void setName(String name)
	{
		this.name = name;
	}
	
	/**
	 * Sets the Entity's x scale
	 * 
	 * @param scaleX
	 */
	public void setScaleX(int scaleX)
	{
		xs = (int) (scaleX * Main.resolutionScaleX);
	}

	/**
	 * Sets the Entity's y scale
	 * 
	 * @param scaleY
	 */
	public void setScaleY(int scaleY)
	{
		ys = (int) (scaleY * Main.resolutionScaleY);
	}
	
	/**
	 * Sets the Entity's animation
	 * 
	 * @param animation
	 */
	public void setAnimation(Animation animation)
	{
		this.animation = animation;
	}
	
	/**
	 * Returns the Entity's animation
	 * 
	 * @return
	 */
	public Animation getAnimation()
	{
		return animation;
	}
	
	/**
	 * Sets the Entity's associated index
	 * 
	 * @param i
	 */
	public void setAssociatedIndex(int i)
	{
		associatedIndex = i;
	}
	
	/**
	 * Returns the Entity's associated index
	 * 
	 * @return
	 */
	public int getAssociatedIndex()
	{
		return associatedIndex;
	}
	
	/**
	 * Returns the Entity's ID
	 * 
	 * @return
	 */
	public int getID()
	{
		return id;
	}
	
	/**
	 * Sets the Entity's personal friction
	 * -1 denotes a default friction
	 * 
	 * @param friction
	 */
	public void setFriction(double friction)
	{
		this.friction = friction;
	}
	
	/**
	 * Returns the Entity's personal friction
	 * 
	 * @return
	 */
	public double getFriction()
	{
		return friction;
	}
	
	/**
	 * Sets the Entity's personal gravity
	 * -1 denotes a default gravity
	 * 
	 * @param gravity
	 */
	public void setGravity(double gravity)
	{
		this.gravity = gravity;
	}
	
	/**
	 * Returns the Entity's personal gravity
	 * 
	 * @return
	 */
	public double getGravity()
	{
		return gravity;
	}
	
	/**
	 * Sets whether the Entity will update with the PhysicsHandler
	 * 
	 * @param updateWithPhysics
	 */
	public void updateWithPhysics(boolean updateWithPhysics)
	{
		this.updateWithPhysics = updateWithPhysics;
	}
	
	/**
	 * Returns whether the Entity will update with the PhysicsHandler
	 * 
	 * @return
	 */
	public boolean willUpdateWithPhysics()
	{
		return updateWithPhysics;
	}
	
	/**
	 * Sets whether the Entity will render in reflections
	 * 
	 * @param render
	 */
	public void renderInReflections(boolean render)
	{
		renderInReflections = render;
	}
	
	/**
	 * Returns whether the Entity will render in reflections
	 * 
	 * @return
	 */
	public boolean shouldRenderInReflections()
	{
		return renderInReflections;
	}
	
	/**
	 * Sets the Entity's LOD value
	 * 
	 * @param lod
	 */
	public void setLOD(int lod)
	{
		this.LOD = lod;
	}
	
	/**
	 * Returns the Entity's LOD value
	 * 
	 * @return
	 */
	public int getLOD()
	{
		return LOD;
	}
	
	/**
	 * Sets whether the Entity can push other entities
	 * 
	 * @param canPush
	 */
	public void canPush(boolean canPush)
	{
		this.canPush = canPush;
	}
	
	/**
	 * Returns whether the Entity will push other entities
	 * 
	 * @return
	 */
	public boolean willPush()
	{
		return canPush;
	}
	
	/**
	 * Will return the string value for which an Entity can be reconstructed
	 * @return
	 */
	public CompressedEntity compress()
	{
		return new CompressedEntity(this);
	}
	
	/**
	 * Sets the Entity's public id
	 * @param id
	 */
	public void setPubID(int id)
	{
		pubID = id;
	}
	
	/**
	 * Get public ID
	 */
	public int getPublicID()
	{
		return pubID;
	}	
	
	/**
	 * Sets the Entity's type
	 * @param type
	 */
	public void setType(int type)
	{
		this.type = type;
	}
	
	/**
	 * Parent an Entity to another Entity
	 * 
	 * @param e
	 */
	public void parentTo(Entity e)
	{
		if (parent == null)
		{
			parent = e;
			e.addChild(this);
		}
	}
	
	/**
	 * Whether the Entity is obstinate
	 * 
	 * @return the obstinate state of the Entity
	 */
	public boolean isObstinate()
	{
		return obstinate;
	}
	
	/**
	 * Sets the obstinate state of the Entity
	 * 
	 * @param obstinate
	 */
	public void setObstinate(boolean obstinate)
	{
		this.obstinate = obstinate;
	}
	
	/**
	 * Adds a child to the Entity
	 * 
	 * @param e
	 */
	public void addChild(Entity e)
	{
		if (! children.contains(e))
		{
			children.add(e);
			e.updateWithPhysics(false);
			e.setYVel(0);
			e.setXVel(0);
		}
	}
	
	/**
	 * Gets the children of the Entity
	 * 
	 * @return The Entity's children
	 */
	public Entity[] getChildren()
	{
		return (Entity[]) children.toArray();
	}
	
	private void translateChildren(double x, double y)
	{
		for (int i = 0; i < children.size(); i++)
		{
			if (children.get(i) != null)
			{
				if (Math.abs(yvel) > 0)
				{
					children.get(i).translate(x, y);
				}
			}
		}
	}
	
	/**
	 * Sets the Entity's width
	 * @param width
	 */
	public void setWidth(double width)
	{
		this.width = width * Main.resolutionScaleX;
	}
	
	/**
	 * Sets the Entity's height
	 * @param height
	 */
	public void setHeight(double height)
	{
		this.height = height * Main.resolutionScaleY;
	}
	
	/**
	 * Sets the Entity's texture name
	 * @param texture
	 */
	public void setTexture(String texture)
	{
		this.textureName = texture;
	}
	
	/**
	 * Gets the Entity's texture name
	 * @return The Entity's texture name
	 */
	public String getTexture()
	{
		return textureName;
	}
	
	/**
	 * Sets the Entity's material
	 * @param mat
	 */
	public void setMaterial(Material mat)
	{
		this.material = mat;
	}
	
	/**
	 * Gets the Entity's material
	 * @return The Entity's material
	 */
	public Material getMaterial()
	{
		return material;
	}
}