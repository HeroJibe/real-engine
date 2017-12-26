package core;

import java.io.File;

import main.Main;

/**
 * The <code>Light</code> class casts
 * light across the map which is handled
 * by the <code>LightHandler</code>
 * 
 * @author Ethan Vrhel
 * @see LightHandler
 * @see Luxel
 */
public class Light 
{
	public static final boolean DEBUG_LIGHTS = false;
	
	public static final int CONSTANT = 1;
	public static final int LINEAR = 2;
	public static final int QUADRATIC = 3;
	
	private int type;
	private double x;
	private double y;
	private double brightness;
	private double dropOff100;
	private double dropOff50;
	
	public Light(int type, double x, double y, double brightness, double dropOff100, double dropOff50)
	{
		this.type = type;
		this.x = x;
		this.y = y;
		this.brightness = brightness;
		this.dropOff100 = dropOff100;
		this.dropOff50 = dropOff50;
		if (DEBUG_LIGHTS)
		{
			Entity lE = new Entity(Entity.STATIC, new File("resources\\textures\\" + ResourceHandler.light),
				64, 64, false, "light", x - 32, y - 32, 100, 64, 64, 100, false);
			Main.getEntityHandler().addStaticEntity(lE);
		}
	}
	
	/**
	 * Returns the x position
	 * @return The x position
	 */
	public double getX()
	{
		return x;
	}
	
	/**
	 * Returns the y position
	 * @return The y position
	 */
	public double getY()
	{
		return y;
	}
	
	/**
	 * Returns the brightness
	 * @return The brightness
	 */
	public double getBrightness()
	{
		return brightness;
	}
	
	/**
	 * Returns the type
	 * @return The type
	 */
	public int getType()
	{
		return type;
	}
	
	/**
	 * Returns the 100% drop-off
	 * @return The 100% drop-off
	 */
	public double getDropOff100()
	{
		return dropOff100;
	}
	
	/**
	 * Returns the 50% drop-off
	 * @return The 50% drop-off
	 */
	public double dropOff50()
	{
		return dropOff50;
	}
	
	/**
	 * Translates the <code>Light</code>
	 * @param x X translation
	 * @param y Y translation
	 */
	public void translate(double x, double y)
	{
		this.x += x;
		this.y += y;
	}
	
	/**
	 * Sets the position of the <code>Light</code>
	 * @param x The x position
	 * @param y The y position
	 */
	public void setPos(double x, double y)
	{
		this.x = x;
		this.y = y;
	}
}
