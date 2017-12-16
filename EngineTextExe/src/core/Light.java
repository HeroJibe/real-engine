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
	
	
	public Light(int type, double x, double y, double brightness)
	{
		this.type = type;
		this.x = x;
		this.y = y;
		this.brightness = brightness;
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
}
