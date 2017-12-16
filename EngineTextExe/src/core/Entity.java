package core;

import java.awt.Color;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Random;

import javax.imageio.ImageIO;

import main.Main;

public class Entity
	implements Comparable<Entity>
{
	public static final int STATIC = 0;
	public static final int DYNAMIC = 1;
	public static final int PHYSICS_TRIES = 0;
	
	private static int entityCount = 0;
	public static final boolean SOLID = false;
	
	private String name;
	private double x;
	private double y;
	private double xvel = 0;
	private double yvel = 0;
	private int z;
	public int health;
	public int maxHealth;
	private boolean ignoreBounds = true;
	private boolean isInvinsible;
	private boolean isAlive;
	private boolean isVisible;
	private boolean isTransparent;
	private boolean isDecal;
	private int type;
	private boolean isSolid;
	private double width = 0;
	private double height = 0;
	private boolean isImage = false;
	private Image image;
	private File imageFile;
	private int mass = 1;
	private int xs = 1;
	private int ys = 1;
	private String text = "";
	private boolean isText = false;
	private int physicsTried = 1;
	private boolean tryingPhysics = false;
	private Entity collisionEntity;
	private boolean isKinetic = true;
	private boolean beingPushed = false;
	
	public static Comparator<Entity> EntityZComparator = new Comparator<Entity>()
	{
		public int compare(Entity entity1, Entity entity2)
		{
			if (entity1 != null && entity2 != null)
				return entity1.compareTo(entity2);
			else
				return 0;
		}
	};	
	
	public Entity(int type, boolean solid, double x, double y, int w, int h, int maxHealth)
	{
		this.type = type;
		isSolid = solid;
		if (type == STATIC)
			name = "Unnamed Static Entity";
		else
			name = "Unnamed Dynamic Entity";
		this.x = x;
		this.y = y;
		width = w;
		height = h;
		z = 0;
		this.maxHealth = maxHealth;
		health = maxHealth;
		isInvinsible = false;
		isAlive = true;
		isVisible = false;
		addedEntity(this);
	}
	
	public Entity(int type, double x, double y, String text)
	{
		this.type = type;
		this.x = x;
		this.y = y;
		this.text = text;
		isText = true;
	}
	
	public Entity(int type, boolean solid, String name, double x, double y, int w, int h, int maxHealth)
	{
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
		if (Main.DEBUG && Main.DRAW_TRIGGERS)
		{
			z = Integer.MAX_VALUE; //2147483647;
			isVisible = true;
			isImage = true;
			image = ResourceHandler.debugTextureImage.getScaledInstance((int) width, (int) height, Image.SCALE_FAST);
		}
		addedEntity(this);
	}
	
	public Entity(int type, boolean solid, String name, double x, double y, int maxHealth)
	{
		this.type = type;
		this.name = name;
		this.type = type;
		isSolid = solid;
		this.x = x;
		this.y = y;
		z = 0;
		this.maxHealth = maxHealth;
		health = maxHealth;
		isInvinsible = false;
		isAlive = true;
		isVisible = false;
		addedEntity(this);
	}
	
	public Entity(int type, boolean solid, String name, double x, double y, int z, int w, int h, int maxHealth, boolean isInvinsible)
	{
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
		addedEntity(this);
	}
	
	public Entity(int type, File image, int xs, int ys, boolean solid, double x, double y, int z, int maxHealth, boolean isInvinsible)
	{
		imageFile = image;
		
		isImage = false;
		if (image != null)
		{
			if (image.exists())
			{
				try 
				{
					this.image = ImageIO.read(image).getScaledInstance(xs, ys, Image.SCALE_SMOOTH);
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
		this.xs = xs;
		this.ys = ys;
		addedEntity(this);
	}
	
	public Entity(int type, File image, int xs, int ys, boolean solid, String name, double x, double y, int z, int w, int h, int maxHealth, boolean isInvinsible)
	{
		imageFile = image;
		
		isImage = false;
		if (image != null)
		{
			if (image.exists())
			{
				try
				{
					this.image = ImageIO.read(image).getScaledInstance((int) Math.ceil(xs * Main.resolutionScaleX), (int) Math.ceil(ys * Main.resolutionScaleY), Image.SCALE_SMOOTH);
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
		addedEntity(this);
	}
	
	private static void addedEntity(Entity entity)
	{
		
		String type = "";
		if (entity.isStatic())
			type = "Static ";
		else if (! entity.isStatic())
			type = "Dynamic";
		
		//Main.println("Added " + type + " Entity : " + entity.getName());
		entityCount++;
		
	}
	
	public static int getNumEntities()
	{
		return entityCount;
	}
	
	public String toString()
	{
		return name + ": (" + x + ", " + y + ", " + z + ")";
	}
	
	public boolean isInvinsible()
	{
		return isInvinsible;
	}
	
	public boolean isAlive()
	{
		return isAlive;
	}
	
	public boolean isVisible()
	{
		return isVisible;
	}
	
	public void setVisible(boolean state)
	{
		isVisible = state;
	}
	
	public boolean isStatic()
	{
		if (type == STATIC)
			return true;
		else if (type == DYNAMIC)
			return false;
		else
			return false;
				
	}
	
	public double getX()
	{
		return x;
	}
	
	public double getY()
	{
		return y;
	}
	
	public void setX(double x)
	{
		if (type == DYNAMIC)
		{
			this.x = x * Main.resolutionScaleX;
			Main.getEntityHandler().invokeUpdate();
		}
		else
			Main.println(name + ": Cannot move non-dynamic entities", Color.RED);
	}
	
	public void setY(double y)
	{
		if (type == DYNAMIC)
		{
			this.y = y * Main.resolutionScaleY;
			Main.getEntityHandler().invokeUpdate();
		}
		else
			Main.println(name + ": Cannot move non-dynamic entities", Color.RED);
	}
	
	public void translateX(double x)
	{
		if (! Main.isMultiplayer && Main.gamePaused)
			return;
		
		if (x == 0)
			return;
		
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
						Main.getEntityHandler().invokeUpdate();
					}
				}	
				else
					beingPushed = false;
			}
			else
			{
				beingPushed = false;
				if (this.isOnScreen())
					Main.getEntityHandler().invokeUpdate();
			}
		}
	}
	
	public void translateY(double y)
	{		
		if (! Main.isMultiplayer && Main.gamePaused)
			return;
		
		if (y == 0)
			return;
		
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
						Main.getEntityHandler().invokeUpdate();
					}
				}
				else
					beingPushed = false;
			}
			else
			{
				beingPushed = false;
				if (this.isOnScreen())
					Main.getEntityHandler().invokeUpdate();
			}
		}
	}
	
	public void setXVel(double xvel)
	{
		this.xvel = xvel;
	}
	
	public void setYVel(double yvel)
	{
		this.yvel = yvel;
	}
	
	public double getXVel()
	{
		return xvel;
	}
	
	public double getYVel()
	{
		return yvel;
	}
	
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
	
	public boolean hasCollided()
	{
		int dynEntities = Main.getEntityHandler().getDynEntities();
		Entity entities[] = Main.getEntityHandler().getEntities();
		if (! this.isSolid)
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
	
	public boolean hasCollidedOnlyStatic()
	{
		int dynEntities = Main.getEntityHandler().getDynEntities();
		Entity entities[] = Main.getEntityHandler().getEntities();
		if (! this.isSolid || ! this.isStatic())
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
	
	public boolean hasCollidedNav()
	{
		int dynEntities = Main.getEntityHandler().getDynEntities();
		Entity entities[] = Main.getEntityHandler().getEntities();
		if (! this.isSolid || ! this.isStatic())
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
	
	public boolean hasCollidedNav(Entity entity)
	{			
		if (! entity.isSolid)
			return false;
		
		if (this.getY2() < entity.getY() || y > entity.getY2())
			return false;
		else if (this.getX2() < entity.getX() || x > entity.getX2())
			return false;
		
		return true;
	}
	
	public boolean hasCollided(Entity entity)
	{			
		if (! entity.isSolid)
			return false;
		
		if (this.getY2() <= entity.getY() || y >= entity.getY2())
			return false;
		else if (this.getX2() <= entity.getX() || x >= entity.getX2())
			return false;
		
		return true;
	}
	
	public boolean hasCollidedIgnoreSolid(Entity entity)
	{		
		if (this.getY2() <= entity.getY() || y >= entity.getY2())
			return false;
		else if (this.getX2() <= entity.getX() || x >= entity.getX2())
			return false;
		
		return true;
	}
	
	public boolean hasPointCollided(double xCol, double yCol)
	{
		if (! isSolid)
			return false;
		
		if (getY2() <= yCol || y >= yCol)
			return false;
		else if (getX2() <= xCol || x >= xCol)
			return false;
		
		return true;
	}
	
	public String getName()
	{
		return name;
	}
	
	public int getZBuffer()
	{
		return z;
	}
	
	public void setZBuffer(int z)
	{
		this.z = z;
	}

	public int compareTo(Entity compareEntity) 
	{
		int compareZVal = compareEntity.getZBuffer();
		
		if (this.z == compareZVal)
		{
			if (this.x == compareEntity.getX())
			{
				if (this.y == compareEntity.getY())
				{
					return 0;
				}
				return (int) (compareEntity.getY() - this.y);
			}
			return (int) (this.x - compareEntity.getX());
		}
		
		
		return  this.z - compareZVal;
	}
	
	public boolean isImage() 
	{
		return isImage;
	}
	
	public Image getImage()
	{
		return image;
	}
	
	public double getHeight()
	{
		return height;
	}
	
	public double getWidth()
	{
		return width;
	}
	
	public double getX2()
	{
		return x + width - 1;
	}
	
	public double getY2()
	{
		return y + height;
	}
	
	public boolean isSolid()
	{
		return isSolid;
	}
	
	public void setSolid(boolean solid)
	{
		isSolid = solid;
	}
	
	public void ignoreBounds(boolean set)
	{
		ignoreBounds = set;
	}
	
	public boolean isOnScreen()
	{
		if (x > Main.getGameWindow().getRightBounds())
			return false;
		else if (x + width < Main.getGameWindow().getLeftBounds())
			return false;
		else if (y > Main.getGameWindow().getBottomBounds())
			return false;
		else if (y + height < Main.getGameWindow().getTopBounds())
			return false;
		else
			return true;
	}
	
	public void setTransparancy(boolean set)
	{
		isTransparent = set;
	}
	
	public boolean isTransparent()
	{
		return isTransparent;
	}
	
	public void setMass(int mass)
	{
		this.mass = mass;
	}
	
	public int getMass()
	{
		return mass;
	}
	
	public boolean isDecal()
	{
		return isDecal;
	}
	
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
	
	public void setImageFile(File file)
	{
		imageFile = file;
	}
	
	public File getImageFile()
	{
		return imageFile;
	}
	
	public void setImage(Image img)
	{
		image = img;
	}
	
	public String getAttributes()
	{
		String construct = type + " " + imageFile + " " + xs + " " + ys + " "+ isSolid + " \"" + name + "\" " + 
						   getActualX() + " " + getActualY() + " " + z + " " + width + " " + height + " " + maxHealth + " " + isInvinsible;
		String extra = isTransparent + " " + isDecal + " " + mass + " " + ignoreBounds;
		return construct + " " + extra;
	}
	
	//public Entity(int type, Image image, boolean solid, String name, double x, double y, int z, int w, int h, int maxHealth, boolean isInvinsible)
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
	
	public double getActualX()
	{
		return x / Main.resolutionScaleX;
	}
	
	public double getActualY()
	{
		return y / Main.resolutionScaleY;
	}
	
	public double getActualWidth()
	{
		return width / Main.resolutionScaleX;
	}
	
	public double getActualHeight()
	{
		return height / Main.resolutionScaleY;
	}
	
	public double getActualScaleX()
	{
		return ys / Main.resolutionScaleX;
	}
	
	public double getActualScaleY()
	{
		return ys / Main.resolutionScaleY;
	}
	
	public Image getActualImage()
	{
		return image.getScaledInstance((int) (xs / Main.resolutionScaleX), (int) (ys / Main.resolutionScaleY), Image.SCALE_DEFAULT);
	}
	
	public boolean isText()
	{
		return isText;
	}
	
	public String getText()
	{
		return text;
	}
	
	public boolean isKinetic()
	{
		return isKinetic;
	}
	
	public void setKinetic(boolean isKinetic)
	{
		this.isKinetic = isKinetic;
	}
	
	public boolean beingPushed()
	{
		return beingPushed;
	}
}
