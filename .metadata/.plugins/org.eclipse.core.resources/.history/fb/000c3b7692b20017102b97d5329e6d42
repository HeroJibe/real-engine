package core;

import java.io.File;

import main.Main;

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
	
	public double getX()
	{
		return x;
	}
	
	public double getY()
	{
		return y;
	}
	
	public double getBrightness()
	{
		return brightness;
	}
	
	public int getType()
	{
		return type;
	}
	
	public double getDropOff100()
	{
		return dropOff100;
	}
	
	public double dropOff50()
	{
		return dropOff50;
	}
	
	public void translate(double x, double y)
	{
		this.x += x;
		this.y += y;
	}
	
	public void setPos(double x, double y)
	{
		this.x = x;
		this.y = y;
	}
}
