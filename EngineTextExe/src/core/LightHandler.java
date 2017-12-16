package core;

import java.awt.Color;
import java.io.File;

import gui.GameWindow;
import main.Main;

public class LightHandler 
{
	public static final boolean DEBUG_LUXELS = false;
	
	private int maxLights;
	private int numLights;
	private Light[] lights;
	private double luxelSize;
	private Luxel[] luxels;
	private int luxX;
	private int luxY;
	private double lightMap[][];
	
	public LightHandler(int maxLights, double luxelSize)
	{
		this.maxLights = maxLights;
		this.luxelSize = luxelSize;
		lights = new Light[maxLights];
		luxels = new Luxel[(int) Math.round(((GameWindow.XRES_GL + 1) / luxelSize) * ((GameWindow.YRES_GL + 1) / luxelSize))];
		lightMap = new double[GameWindow.XRES_GL][GameWindow.YRES_GL];
	}
	
	public void addLight(Light light)
	{
		for (int i = 0; i < maxLights; i++)
		{
			if (lights[i] == null)
			{
				numLights++;
				lights[i] = light;
				return;
			}
		}
		Main.println("Failed to add light.  Light cache not big enough.", Color.RED);
	}
	
	public double getBrightnessAtLuxel(int luxelID)
	{
		return luxels[luxelID].getBrightness();
	}
	
	
	
	/*
	public double getBrightnessAt(int x, int y)
	{
		double brightness = 0;
		for (int i = 0; i < numLights; i++)
		{
			if (lights[i] != null)
			{
				RayTrace trace = new RayTrace();
				trace.setup(x, y);				
				
				double a = lights[i].getX() - x;
				double b = lights[i].getY() - y;
				double c1 = (a * a) + (b * b);
				double c = Math.sqrt(c1);
				
				double angle = b / c;
				
				
				if (x > lights[i].getX())
					angle = -1 * angle;
				
				
				trace.setDirection((int) Math.toDegrees(angle));
				trace.startTrace((int) c);
				if (trace.getCollisionEntity() == null)
				{
					brightness += lights[i].getBrightness();
				}
			}
		}
		if (DEBUG_LUXELS)
		{
			if (brightness == 1)
			{
				
				Entity lE = new Entity(Entity.STATIC, new File("resources\\textures\\" + ResourceHandler.luxel),
					64, 64, false, "light", x, y, 100, 64, 64, 100, false);
				Main.getEntityHandler().addStaticEntity(lE);
				
			}
			else
			{
				Entity lE = new Entity(Entity.STATIC, new File("resources\\textures\\" + ResourceHandler.luxel),
						64, 64, false, "light", x, y, 100, 64, 64, 100, false);
				Main.getEntityHandler().addStaticEntity(lE);
			}
		}
		
		
		return brightness;
	}
	*/
	
	public void updateBrightness()
	{
		for (int i = 0; i < numLights; i++)
		{
			for (int l = 0; l < luxels.length; l++)
			{
				if (luxels[l] != null && lights[i] != null)
				{
					
				}
			}
		}
	}
}
