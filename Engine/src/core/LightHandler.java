package core;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Random;

import javax.imageio.ImageIO;

import game.GameMain;
import gui.GameWindow;
import main.Main;
import utilities.ClockTimer;
	
@Deprecated
public class LightHandler 
	implements Runnable
{
	public static final boolean DEBUG_LUXELS = false;
	public static final int LIGHT_QUAL = 1;
	
	public static final int CALCULATE_FRL = 0;
	public static final int CALCULATE_LRL = 1;
	public static final int CALCULATE_FXDL = 2;
	public static final int CALCULATE_FDDL = 3;
	
	private int maxLights;
	private int numLights;
	private Light[] lights;
	private double lightMap[][];
	private boolean dispProgress = false;
	private int luxels = 0;
	private int illuminated = 0;
	private double ambient;
	
	public static int maxShadowsDist = -1;						// Maximum shadows distance
	
	// FRL (Full Resolution Lighting)	
	// LRL (Low Resolution Lighting)
	public static int qualityScale = 64;						// Quality scale for lighting
	
	// FXDL (Fast Approximate Dynamic Lighting)
	public static int dynamicLightingUpdateFreq = 2;			// Value for determining dynamic lighting updates
	
	// FDDL (Fast Distance-Based Dynamic Lighting)
	public static double dist = 1000;							// Value for determining shadow distances
	
	public LightHandler(int maxLights, double luxelSize, double ambient)
	{
		this.maxLights = maxLights;
		lights = new Light[maxLights];
		lightMap = new double[1920 / LIGHT_QUAL][1080 / LIGHT_QUAL];
		luxels = (1920 / LIGHT_QUAL) * (1080 / LIGHT_QUAL);
		this.ambient = ambient;
	}
	
	public void run()
	{
		while (GameMain.gameRunning)
		{
			if ((Main.lighting && Main.useDynamicLighting) || (Main.lighting && Main.useBoth));
			{
				updateBrightness(false, CALCULATE_LRL);
			}
			try 
			{
				Thread.sleep(1);
			} 
			catch (InterruptedException e)
			{
				e.printStackTrace();
			}
		}
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
	
	public double getBrightnessAt(int x, int y)
	{
		return lightMap[x][y];
	}
	
	public void updateBrightness(boolean baked, int calculationType)
	{
		int scale = 1;
		if (calculationType != CALCULATE_FRL)
			scale = qualityScale;
		illuminated = 0;
		
		for (int x = 0; x < 1920 / (LIGHT_QUAL * scale); x++)
		{
			for (int y = 0; y < 1080 / (LIGHT_QUAL * scale); y++)
			{
				int num = 0;
				if (calculationType == CALCULATE_FXDL)
					num = (new Random()).nextInt(dynamicLightingUpdateFreq);
				
				if (num == 0)
				{
					double brightness = updateBrightnessAt(x * (LIGHT_QUAL * scale), y * (LIGHT_QUAL * scale), baked, calculationType);
					//double brightness = calculateAt(x * (LIGHT_QUAL * scale), y * (LIGHT_QUAL * scale));
					if (brightness != 0)
						illuminated++;
					
					lightMap[x][y] = brightness;
				}
			}
			
			if (dispProgress)
			{
				if (x == 1920 / 4)
				{
					Main.println("25%");
				}
				else if (x == 1920 / 2)
				{
					Main.println("50%");
				}
				else if (x == 1920 / 1.5)
				{
					Main.println("75%");
				}
				else if (x == 1920 - 1)
				{
					Main.println("100%");
				}
			}
		}
		dispProgress = false;
	}
	
	public double updateBrightnessAt(int x, int y, boolean baked, int calculationType)
	{		
		double brightness = ambient;
		for (int i = 0; i < numLights; i++)
		{
			if (lights[i] != null)
			{
				//if ((new Entity(Entity.STATIC, true, x, y, 1, 1, 100).hasCollidedOnlyStatic()))
					//return 0;
				
				RayTrace trace = new RayTrace();
				trace.setup(x, y);	
				trace.ignoreDynamic(baked);
				if (! baked && Main.useBoth)
				{
					trace.ignoreStatic(true);
				}
				else
				{
					trace.ignoreStatic(false);
				}
				double a;
				double b;
				a = lights[i].getX() - x;
				b = lights[i].getY() - y;

				double c1 = (a * a) + (b * b);
				double c = Math.sqrt(c1);
				
				if (Math.abs(c) > maxShadowsDist && maxShadowsDist != -1)
					return ambient;
				
				boolean calc = true;
				if (Math.abs(c) > lights[i].getDropOff100() && lights[i].getDropOff100() != -1)
					brightness += 0;
				else
				{
				 	calc = true;
					if (calc)
					{
						double angle = b / c;
						
						if (x > lights[i].getX())
							angle = (Math.toRadians(360) - angle);
						
						trace.setDirection((int) Math.toDegrees(angle));
						trace.startTrace((int) c);
						if (trace.getCollisionEntity() == null)
						{
							
							if (Math.abs(c) > lights[i].getDropOff100() && lights[i].getDropOff100() != -1)
								brightness += 0;
							else if (Math.abs(c) > lights[i].dropOff50() && lights[i].getDropOff100() != -1)
								brightness += lights[i].getBrightness() / 2;
							else			
								brightness += lights[i].getBrightness();
							
						}
					}
					else
					{
						brightness = 1;
					}
				}
			}
		}
		if (brightness > 1)
			brightness = 1;
		
		return brightness;
	}
	
	private Entity[] entities = Main.getEntityHandler().getEntities();
	public double calculateAt(int x, int y)
	{
		double brightness = ambient;
		int dynEntities = Main.getEntityHandler().getDynEntities();
		for (int i = 0; i < numLights; i++)
		{
			double a = lights[i].getX() - x;
			double b = lights[i].getY() - y;
			double m;
			if (a != 0)
				m = b / a;
			else
				m = 0;
			int y2 = y;
			boolean collided = false;
			int inc = 1;
			for (int x2 = x; (int) x2 != (int) lights[i].getX() && (x2 >= 0 && x2 <= GameWindow.XRES_GL); x2 += inc)
			{
				y2 = (int) ((x2 * m) + b);
				for (int j = 0; j < dynEntities; j++)
				{
					if (entities[j] != null)
					{
						if (entities[j].hasPointCollided(x2, y2))
						{
							collided = true;
							break;
						}
					}
				}
				if (collided)
					break;
			}
			
			if (! collided)
				brightness += lights[i].getBrightness();
		}
		if (brightness > 1)
			brightness = 1;
		return brightness;
	}
	
	public void buildLighting()
	{
		Main.println("Building lighting...");
		ClockTimer timer = new ClockTimer();
		timer.startTimer();
		BufferedImage lighting = new BufferedImage(GameWindow.XRES_GL, GameWindow.YRES_GL, BufferedImage.TYPE_INT_ARGB);
		Graphics g = lighting.getGraphics();
		dispProgress = true;
		updateBrightness(true, CALCULATE_FRL);
		timer.stopTimer();
		Main.println("Done.  (" + timer.getTimeSeconds() + " seconds)");
		Main.println("Built " + luxels + " luxels.  (" + illuminated + " illuminated)");
		
		timer = new ClockTimer();
		timer.startTimer();
		Main.println("Exporting lighting...");
		RenderingHints rh = new RenderingHints(
	             RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
		rh.add(new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON));
		rh.add(new RenderingHints(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC));
		rh.add(new RenderingHints(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY));
		rh.add(new RenderingHints(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON));
		rh.add(new RenderingHints(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY));
		rh.add(new RenderingHints(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_ENABLE));
		Graphics2D g2 = (Graphics2D)g;
		g2.setRenderingHints(rh);
		try
		{    			
			int fac = LightHandler.LIGHT_QUAL;
			for (int x = 0; x < lighting.getWidth(null); x++)
			{
				for (int y = 0; y < lighting.getHeight(null); y++)
				{
					double brightness = getBrightnessAt(x, y);
					if (brightness != 0)
					{
						g.setColor((new Color(0, 0, 0, (int) (255 - (brightness * 255)))));
						g.fillRect(x, y, fac, fac);
					}
				}
			}
		}
		catch (Exception e) {Main.println("error building lighting: " + e, Color.RED);}
		
	
		try
		{
			ImageIO.write(lighting, "png", (new File("maps\\lighting\\" + Main.getMapName() + ".lighting")));
			timer.stopTimer();
			Main.println("Done.  (" + timer.getTimeSeconds() + " seconds)");
			Main.println("Wrote to " + (new File("maps\\lighting\\" + Main.getMapName() + ".lighting").toString()));
			Main.println("Successfully built lightmap for map " + Main.getMapName(), Color.GREEN);
		}
		catch (Exception e)
		{
			Main.println("Error building lightmap for map " + Main.getMapName(), Color.RED);
		}
	}
}
