package core;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

import javax.imageio.ImageIO;

import core.MapEntities.Gem;
import core.MapEntities.InvokeTrigger;
import core.MapEntities.MapEntity;
import core.MapEntities.Track;
import core.Triggers.TriggerBreak;
import core.Triggers.TriggerGameEvent;
import core.Triggers.TriggerGravity;
import core.Triggers.TriggerGravityDecay;
import core.Triggers.TriggerHurt;
import core.Triggers.TriggerLoad;
import core.Triggers.TriggerMove;
import core.Triggers.TriggerShader;
import core.Triggers.TriggerSound;
import core.Triggers.TriggerStartTrigger;
import core.Triggers.TriggerStopTrigger;
import core.Triggers.TriggerToggleMapEntity;
import core.Triggers.TriggerToggleSolid;
import gui.GameWindow;
import main.Main;
import utilities.ResourceMonitor;

/**
 * The <code>MapLoader</code> class is
 * responsible for loading .map files into
 * the game to be played on.
 * 
 * @author Ethan Vrhel
 * @see Entity
 * @see Trigger
 * @see MapEntity
 * @see Node
 */
public class MapLoader
{
	/**
	 * Maximum amount of variables that can be referenced in the map
	 */
	public static final int MAX_VARIABLES = 16;
	
	/**
	 * Whether the map should load from a .entitydata file, which are not
	 * recommended for use
	 */
	public boolean shouldLoadSaved = false;
	
	private String[] map;
	private String[] prefab;
	private String mapname;
	private File mapFile;
	private File prefabFile;
	private int prefabOffsetX = 0;
	private int prefabOffsetY = 0;
	private boolean loadsaved = false;
	private File data;
	private File save;
	private int timesloaded;

	private Entity[] staticEntityCache;
	private int staticEntityIndex;
	private boolean forcedSeperate;
	private int variables[] = new int[MAX_VARIABLES];
	private String variableNames[] = new String[MAX_VARIABLES];
	private int numVariables = 0;
	
	public static double xSpawn = 0;
	public static double ySpawn = 0;
	
	/**
	 * Loads a map into memory
	 * 
	 * @param mapFile The map file
	 * @param ignoreDataFile Whether the .dat file
	 * should be ignored
	 */
	public void readMap(File mapFile, boolean ignoreDataFile)
	{		
		this.mapFile = mapFile;
		if (! mapFile.exists())
		{
			Main.println("error: non-existent map at " + mapFile.toString(), Color.RED);
			return;
		}
		
		if (! ignoreDataFile)
		{
			data = new File(mapFile.toString() 	+ ".dat");
			if (data.exists())
			{
				try 
				{
					Scanner in = new Scanner(data);
					while (in.hasNextLine())
					{
						String line = in.nextLine();
						String tokens[];
						String delims = " = ";
						tokens = line.split(delims);
						if (tokens[0].equals("loadsaved"))
						{
							loadsaved = Boolean.parseBoolean(tokens[1]);
							if (loadsaved && ! (new File(mapFile.toString() + ".entitydata")).exists())
							{
								loadsaved = false;
							}
						}
						else if (tokens[0].equals("timesloaded"))
						{
							timesloaded = Integer.parseInt(tokens[1]);
						}

						updateState(loadsaved);
					}
					in.close();
				} 
				catch (Exception e) 
				{
					e.printStackTrace(System.out);
				}
			}
			else
			{
				try 
				{
					data.createNewFile();
					PrintStream out = new PrintStream(data);
					out.println("loadsaved = false");
					out.println("timesloaded = 1");
					out.println("lightmapbuilt = false");
					out.close();
				} 
				catch (Exception e) 
				{
					e.printStackTrace(System.out);
				}
			
			}
		}
		
		if (loadsaved)
		{
			if (! shouldLoadSaved)
				loadsaved = false;
		}
		
		if (loadsaved && ! ignoreDataFile)
		{			
			try
			{
				Scanner in = new Scanner(data);
				
				int lines = 0;
				while (in.hasNextLine())
				{
					lines++;
					in.nextLine();
				}
				in.close();
				map = new String[lines];
				in = new Scanner(data);
				for (int i = 0; i < lines; i++)
				{
					map[i] = in.nextLine();
				}
				in.close();
				return;
			}
			catch (FileNotFoundException e)
			{
				Main.println("Save: " + mapFile.toString() + ".entitydata" + " not found", Color.RED);
				e.printStackTrace(System.out);
			}
		}
		
		try
		{
			Scanner in = new Scanner(mapFile);
			mapname = null;
			
			int lines = 0;
			while (in.hasNextLine())
			{
				lines++;
				in.nextLine();
			}
			map = new String[lines];
			in.close();
			in = new Scanner(mapFile);
			for (int i = 0; i < lines; i++)
			{
				map[i] = in.nextLine();
			}
			in.close();
		} 
		catch (FileNotFoundException e)
		{
			Main.println("Map: " + mapFile.toString() + " not found", Color.RED);
			e.printStackTrace(System.out);
		}
	}
	
	/**
	 * Loads a .prefab file into memory
	 * 
	 * @param prefabFile The .prefab file
	 */
	public void readPrefab(File prefabFile)
	{		
		try
		{
			this.prefabFile = prefabFile;
			
			Scanner in = new Scanner(prefabFile);
			
			int lines = 0;
			while (in.hasNextLine())
			{
				lines++;
				in.nextLine();
			}
			prefab = new String[lines];
			in.close();
			in = new Scanner(prefabFile);
			for (int i = 0; i < lines; i++)
			{
				prefab[i] = in.nextLine();
			}
			in.close();
		} 
		catch (FileNotFoundException e)
		{
			Main.println("Prefab: " + prefabFile.toString() + " not found", Color.RED);
			e.printStackTrace(System.out);
		}
	}
	
	int entities = 0;

	private long start;
	/**
	 * Loads the map
	 * 
	 * @param prefab Whether the map should load
	 * as a .prefab
	 * @param loadsaved Whether the map should load
	 * from a .entitydata file
	 * @return The return status
	 */
	@SuppressWarnings("unused")
	public int loadMap(boolean prefab, boolean loadsaved)
	{
		Main.getGameWindow().lighting = null;
		if (! prefab)
		{
			start = System.currentTimeMillis();
			Main.getClockTimerHandler().stopAll();
			Main.getReflectionHandler().removeAll();
			Main.getEntityHandler().removeAllEntities();
			Main.getTriggerHandler().clearAllTriggers();
			Main.getParticleEffectHandler().removeAll();
			Main.getMapEntityHandler().stopAll();
			Main.getMapEntityHandler().removeAll();
			Main.getNodeHandler().clearAll();
			
			Main.getGameWindow().cameraXBounds[0] = Integer.MIN_VALUE;
			Main.getGameWindow().cameraXBounds[1] = Integer.MAX_VALUE;
			Main.getGameWindow().cameraYBounds[0] = Integer.MIN_VALUE;
			Main.getGameWindow().cameraYBounds[1] = Integer.MAX_VALUE;
			
			staticEntityCache = new Entity[Main.getEntityHandler().getMaxEntities()];
			staticEntityIndex = 0;
			forcedSeperate = false;
			
			clearVariables();
			
			System.gc();
		}
			
		if (loadsaved && ! prefab)
		{
			Main.println("Loading from save...");
			int exit = loadSaved();
			if (exit != 1)
				return exit;
			readMap(mapFile, true);
		}
		
		int fileType = -1;
		if (map == null)
		{
			Main.println("No map loaded.", Color.RED);
			return 0;
		}
		
		if (! prefab)
			Main.println("Loading map: " + mapFile.toString());
		
		Main.getGameWindow().drawBackground = true;
		
		String map[];
		if (! prefab)
			map = this.map;
		else
			map = this.prefab;
		
		int i = 0;
		try 
		{
			if (map[0].equals("Map"))
			{
				fileType = 0;
				entities = 0;
			}
			else if (map[0].equals("Prefab"))
			{
				fileType = 1;
			}
			else
			{
				Main.println("Error: File does not have defined type at start", Color.RED);
				return -1;
			}
			
			if (map[1].equals("ForceSeperate"))
			{
				if (fileType == 0)
				{
					forcedSeperate = true;
				}
				else
				{
					Main.println("Error at " + prefabFile.toString() + ":" + (i + 1) + ": prefabs cannot set global map attributes", Color.RED);
					return -1;
				}
			}
			
			boolean versionFound = false;
			for (i = 0; i < map.length; i++)
			{
				String delims = " = ";
				String tokens[] = map[i].split(delims);
				if (tokens.length > 1)
				{					
					String args = tokens[1];
					delims = " ";
					if (tokens[0].equals("Name"))
					{
						if (fileType == 1)
						{
							Main.println("Error at " + prefabFile.toString() + ":" + (i + 1) + ": prefabs cannot set global map attributes", Color.RED);
							return -1;
						}
						if (mapname != null)
						{
							Main.println("Error at " + mapFile.toString() + ":" + (i + 1) + ": map name cannot be set twice", Color.RED);
							return -1;
						}
						mapname = tokens[1] + ".map";
					}
					else if (tokens[0].equals("Version"))
					{
						if (versionFound)
						{
							Main.println("Error at " + mapFile.toString() + ":" + (i + 1) + ": map version cannot be set twice", Color.RED);
						}
						tokens = args.split(delims);
						double version = Double.parseDouble(tokens[0]);
						if (version >= Main.ENGINE_VERSION)
						{ }
						else
						{
							Main.println("Incompatible map version " + version +
									". must be at least " + Main.ENGINE_VERSION, Color.RED);
							return -1;
						}
						versionFound = true;
					}
					else if (tokens[0].equals("Brush") && ! loadsaved)
					{
						tokens = args.split(delims);
					
						int type = -1;
						String texture;
						String name = "Brush Face";
						double x;
						double y;
						int width;
						int height;
						int z;
						boolean solid;
						int mass = 1;
						boolean kinetic = true;
					
					
						if (tokens[0].equals("STATIC"))
						{
							type = Entity.STATIC;
						} 
						else if (tokens[0].equals("DYNAMIC"))
						{
							type = Entity.DYNAMIC;
						}
					
						texture = tokens[1];
					
						x = getEntityReplacement(tokens[2]) + prefabOffsetX;
						y = getEntityReplacement(tokens[3]) + prefabOffsetY;
						z = Integer.parseInt(tokens[4]);
						width = (int) getEntityReplacement(tokens[5]);
						height = (int) getEntityReplacement(tokens[6]);
						solid = Boolean.parseBoolean(tokens[7]);
						if (type == Entity.DYNAMIC)
						{
							mass = Integer.parseInt(tokens[8]);
							kinetic = Boolean.parseBoolean(tokens[9]);
							name = tokens[10];
						}
						else
						{
							try
							{
								name = tokens[8];
							}
							catch (Exception e) {}
						}
					
						//(Entity.DYNAMIC, resourceHandler.getImage(resourceHandler.getIndexByName("player.png")), true, "Player Entity", 640, 360, 10, 64, 64, 100, false);
						Entity se = new Entity(type, Main.getResourceHandler().getByName(texture), width, height,
								solid, name, x, y, z, width, height, 100, false);
						se.setKinetic(kinetic);
						
						se.setMass(mass);
						se.setImageFile(Main.getResourceHandler().getByName(texture));
						se.setMaterial(Main.getMaterialHandler().getMaterialByTexture(texture));
						BufferedImage buffImg = ImageIO.read(Main.getResourceHandler().getByName(texture));
						if (buffImg.getColorModel().hasAlpha())
							se.setTransparancy(true);
						else
							se.setTransparancy(false);
						
						entities++;
						if (type == Entity.STATIC)
						{
							if (! forcedSeperate)
							{
								staticEntityCache[staticEntityIndex] = se;
								staticEntityIndex++;
								Entity se2 = new Entity(Entity.STATIC, solid, name, x, y,
										100);
								se2.setWidth(width);
								se2.setHeight(height);
								se2.setVisible(false);
								Main.getEntityHandler().addStaticEntity(se2);
							}
							else
							{
								Main.getEntityHandler().addStaticEntity(se);
							}
						}
						else
						{
							Main.getEntityHandler().addDynamicEntity(se);
						}
					}
					else if (tokens[0].equals("Clip") && ! loadsaved)
					{
						tokens = args.split(delims);
					
						String name = "Clip Face";
						double x;
						double y;
						int width;
						int height;
						boolean solid;
						int mass = 1;

						x = getEntityReplacement(tokens[0]) + prefabOffsetX;
						y = getEntityReplacement(tokens[1]) + prefabOffsetY;
						width = (int) getEntityReplacement(tokens[2]);
						height = (int) getEntityReplacement(tokens[3]);
						solid = Boolean.parseBoolean(tokens[4]);
						try
						{
							name = tokens[5];
						}
						catch (Exception e) {}
						
						//Entity se = new Entity(Entity.STATIC, Main.getResourceHandler().getByName(texture), width, height,
							//	solid, name, x, y, z, width, height, 100, false);
						Entity se = new Entity(Entity.STATIC, solid, x, y, width, height, 100);
						se.setName(name);
						se.setKinetic(false);				
						se.setTransparancy(true);
						
						entities++;
						Main.getEntityHandler().addStaticEntity(se);
					}
					else if (tokens[0].equals("BreakableBrush") && ! loadsaved)
					{
						tokens = args.split(delims);
					
						String texture;
						String name = "Breakable Brush Face";
						double x;
						double y;
						int width;
						int height;
						int z;
						boolean solid;
					
						texture = tokens[0];
					
						x = getEntityReplacement(tokens[1]) + prefabOffsetX;
						y = getEntityReplacement(tokens[2]) + prefabOffsetY;
						z = Integer.parseInt(tokens[3]);
						width = (int) getEntityReplacement(tokens[4]);
						height = (int) getEntityReplacement(tokens[5]);
						solid = Boolean.parseBoolean(tokens[6]);
						name = tokens[10];
					
						//(Entity.DYNAMIC, resourceHandler.getImage(resourceHandler.getIndexByName("player.png")), true, "Player Entity", 640, 360, 10, 64, 64, 100, false);
						Entity se = new Entity(Entity.STATIC, Main.getResourceHandler().getByName(texture), width, height,
								solid, name, x, y, z, width, height, 100, false);
						se.setKinetic(false);
						
						se.setMass(1);
						se.setImageFile(Main.getResourceHandler().getByName(texture));
						se.setMaterial(Main.getMaterialHandler().getMaterialByTexture(texture));
						BufferedImage buffImg = ImageIO.read(Main.getResourceHandler().getByName(texture));
						if (buffImg.getColorModel().hasAlpha())
							se.setTransparancy(true);
						else
							se.setTransparancy(false);
						
						entities++;
					}
					else if (tokens[0].equals("SeperateBrush") && ! loadsaved)
					{
						tokens = args.split(delims);
					
						int type = -1;
						String texture;
						String name = "Seperate Brush Face";
						double x;
						double y;
						int width;
						int height;
						int z;
						boolean solid;
						int mass = 1;
						boolean kinetic = true;
					
					
						if (tokens[0].equals("STATIC"))
						{
							type = Entity.STATIC;
						} 
						else if (tokens[0].equals("DYNAMIC"))
						{
							type = Entity.DYNAMIC;
						}
					
						texture = tokens[1];
					
						x = getEntityReplacement(tokens[2]) + prefabOffsetX;
						y = getEntityReplacement(tokens[3]) + prefabOffsetY;
						z = Integer.parseInt(tokens[4]);
						width = (int) getEntityReplacement(tokens[5]);
						height = (int) getEntityReplacement(tokens[6]);
						solid = Boolean.parseBoolean(tokens[7]);
						if (type == Entity.DYNAMIC)
						{
							mass = Integer.parseInt(tokens[8]);
							kinetic = Boolean.parseBoolean(tokens[9]);
							name = tokens[10];
						}
					
						//(Entity.DYNAMIC, resourceHandler.getImage(resourceHandler.getIndexByName("player.png")), true, "Player Entity", 640, 360, 10, 64, 64, 100, false);
						Entity se = new Entity(type, Main.getResourceHandler().getByName(texture), width, height,
								solid, name, x, y, z, width, height, 100, false);
						se.setKinetic(kinetic);
						se.setMaterial(Main.getMaterialHandler().getMaterialByTexture(texture));
						se.setMass(mass);
						se.setImageFile(Main.getResourceHandler().getByName(texture));
						
						BufferedImage buffImg = ImageIO.read(Main.getResourceHandler().getByName(texture));
						if (buffImg.getColorModel().hasAlpha())
							se.setTransparancy(true);
						else
							se.setTransparancy(false);
						
						entities++;
						if (type == Entity.STATIC)
							Main.getEntityHandler().addStaticEntity(se);
						else
							Main.getEntityHandler().addDynamicEntity(se);
					}
					else if (tokens[0].equals("LowDetailBrush") && ! loadsaved)
					{
						tokens = args.split(delims);
					
						int type = -1;
						String texture;
						String name = "Low Detail Brush Face";
						double x;
						double y;
						int width;
						int height;
						int z;
						boolean solid;
						int lod;
						int mass = 1;
						boolean kinetic = true;
					
					
						if (tokens[0].equals("STATIC"))
						{
							type = Entity.STATIC;
						} 
						else if (tokens[0].equals("DYNAMIC"))
						{
							type = Entity.DYNAMIC;
						}
					
						texture = tokens[1];
					
						x = getEntityReplacement(tokens[2]) + prefabOffsetX;
						y = getEntityReplacement(tokens[3]) + prefabOffsetY;
						z = Integer.parseInt(tokens[4]);
						width = (int) getEntityReplacement(tokens[5]);
						height = (int) getEntityReplacement(tokens[6]);
						solid = Boolean.parseBoolean(tokens[7]);
						lod = Integer.parseInt(tokens[8]);
						if (type == Entity.DYNAMIC)
						{
							mass = Integer.parseInt(tokens[9]);
							kinetic = Boolean.parseBoolean(tokens[10]);
							name = tokens[11];
						}
					
						//(Entity.DYNAMIC, resourceHandler.getImage(resourceHandler.getIndexByName("player.png")), true, "Player Entity", 640, 360, 10, 64, 64, 100, false);
						Entity se = new Entity(type, Main.getResourceHandler().getByName(texture), width, height,
								solid, name, x, y, z, width, height, 100, false);
						se.setKinetic(kinetic);
						se.setMaterial(Main.getMaterialHandler().getMaterialByTexture(texture));
						se.setMass(mass);
						se.setImageFile(Main.getResourceHandler().getByName(texture));
						
						BufferedImage buffImg = ImageIO.read(Main.getResourceHandler().getByName(texture));
						if (buffImg.getColorModel().hasAlpha())
							se.setTransparancy(true);
						else
							se.setTransparancy(false);
						
						se.setLOD(lod);
						se.renderInReflections(false);
						
						entities++;
						if (type == Entity.STATIC)
						{
							if (! forcedSeperate)
							{
								staticEntityCache[staticEntityIndex] = se;
								staticEntityIndex++;
								Entity se2 = new Entity(Entity.STATIC, solid, name, x, y,
										100);
								se2.setWidth(width);
								se2.setHeight(height);
								se2.setVisible(false);
								Main.getEntityHandler().addStaticEntity(se2);
							}
							else
							{
								Main.getEntityHandler().addStaticEntity(se);
							}
						}
						else
							Main.getEntityHandler().addDynamicEntity(se);
					}
					else if (tokens[0].equals("LowDetailTiledBrush") && ! loadsaved)
					{
						tokens = args.split(delims);
						
						int type = -1;
						String name = "Low Detail Tiled Brush Face";
						String texture;
						double x;
						double y;
						int width;
						int height;
						int tileW = 1;
						int tileH = 1;
						int z;
						boolean solid;
						int lod;
						if (tokens[0].equals("STATIC"))
						{
							type = Entity.STATIC;
						} 
						else if (tokens[0].equals("DYNAMIC"))
						{
							type = Entity.DYNAMIC;
						}
					
						texture = tokens[1];
					
						x = getEntityReplacement(tokens[2]);
						y = getEntityReplacement(tokens[3]) + prefabOffsetY;
						z = Integer.parseInt(tokens[4]);
						width = (int) getEntityReplacement(tokens[5]);
						height = (int) getEntityReplacement(tokens[6]);
						solid = Boolean.parseBoolean(tokens[7]);
						tileW = (int) getEntityReplacement(tokens[8]);
						tileH = (int) getEntityReplacement(tokens[9]);
						lod = (int) getEntityReplacement(tokens[10]);
						if (type == Entity.DYNAMIC)
						{
							Integer.parseInt(tokens[11]);
							name = tokens[12];
						}
						
						//(Entity.DYNAMIC, resourceHandler.getImage(resourceHandler.getIndexByName("player.png")), true, "Player Entity", 640, 360, 10, 64, 64, 100, false);
						boolean transparency;
						BufferedImage buffImg = ImageIO.read(Main.getResourceHandler().getByName(texture));
						if (buffImg.getColorModel().hasAlpha())
							transparency = true;
						else
							transparency = false;
						Image rawImg = buffImg.getScaledInstance((int) (width), 
								(int) (height), Image.SCALE_DEFAULT);
						
						BufferedImage img = new BufferedImage((int) (width * tileW),
								(int) (height * tileH), BufferedImage.TYPE_INT_ARGB);
						Graphics g = img.getGraphics();
						for (int j = 0; j < tileH; j++)
						{
							for (int k = 0; k < tileW; k++)
							{
								g.drawImage(rawImg, (int) (k * width), 
										(int) (j * height), null);
							}
						}
						
						
						Entity e = new Entity(type, img, width * tileW, 
								height * tileH, solid, x, y, z, 100, false);
						e.setLOD(lod);
						e.setVisible(true);
						e.setTransparancy(transparency);
						e.setMaterial(Main.getMaterialHandler().getMaterialByTexture(texture));
						if (type == Entity.STATIC)
						{
							if (! forcedSeperate)
							{
								staticEntityCache[staticEntityIndex] = e;
								staticEntityIndex++;
								Entity se2 = new Entity(Entity.STATIC, solid, name, x, y,
										100);
								se2.setWidth(img.getWidth());
								se2.setHeight(img.getHeight());
								se2.setVisible(false);
								Main.getEntityHandler().addStaticEntity(se2);
							}
							else
							{
								Main.getEntityHandler().addStaticEntity(e);
							}
						}
						else
							Main.getEntityHandler().addDynamicEntity(e);
						
					}
					else if (tokens[0].equals("ReflectiveBrush") && ! loadsaved)
					{
						tokens = args.split(delims);
					
						String texture;
						double x;
						double y;
						int width;
						int height;
						int z;
						boolean solid;
						double reflectivity;
						int tileX;
						int tileY;
					
						texture = tokens[0];
					
						x = getEntityReplacement(tokens[1]) + prefabOffsetX;
						y = getEntityReplacement(tokens[2]) + prefabOffsetY;
						z = Integer.parseInt(tokens[3]);
						width = (int) getEntityReplacement(tokens[4]);
						height = (int) getEntityReplacement(tokens[5]);
						solid = Boolean.parseBoolean(tokens[6]);
						reflectivity = getEntityReplacement(tokens[7]);
						tileX = Integer.parseInt(tokens[8]);
						tileY = Integer.parseInt(tokens[9]);
						
						//Main.println("" + x + " " + y  + " " + z + " " + width + " " + height + " " + solid + " " + reflectivity);
					
						//(Entity.DYNAMIC, resourceHandler.getImage(resourceHandler.getIndexByName("player.png")), true, "Player Entity", 640, 360, 10, 64, 64, 100, false);
						
						ReflectiveEntity se;
						Image img = Main.getResourceHandler().getImage(
								Main.getResourceHandler().getIndexByName(texture, true)).getScaledInstance(
										width, height, Image.SCALE_AREA_AVERAGING);
						if (false && tileX == 1 && tileY == 1)
						{
							se = new ReflectiveEntity(img, width, height,
									solid, x, y, z, 100, true, ReflectiveEntity.Plane.NEGATIVE_Y, reflectivity);
						}
						else
						{
							BufferedImage buffImg = new BufferedImage(width * tileX, height * tileY, BufferedImage.TYPE_INT_ARGB);
							Graphics g = buffImg.getGraphics();
							for (int j = 0; j < tileX; j++)
							{
								for (int k = 0; k < tileY; k++)
								{
									g.drawImage(img, j * width, k * height, null);
								}
							}
							se = new ReflectiveEntity(buffImg, width * tileX, height * tileY, solid, x, y, z, 100, true,
									ReflectiveEntity.Plane.NEGATIVE_Y, reflectivity);
						}
						
						BufferedImage buffImg = ImageIO.read(Main.getResourceHandler().getByName(texture));
						if (buffImg.getColorModel().hasAlpha())
							se.setTransparancy(true);
						else
							se.setTransparancy(false);
						
						se.setMaterial(Main.getMaterialHandler().getMaterialByTexture(texture));
						Main.getReflectionHandler().addReflectiveEntity(se);
						
						entities++;
					}
					else if (tokens[0].equals("AnimatedBrush") && ! loadsaved)
					{
						tokens = args.split(delims);
						
						int type = -1;
						String name = "Brush Face";
						double x;
						double y;
						int width;
						int height;
						int z;
						boolean solid;
						int mass = 1;
						boolean kinetic = false;
					
					
						if (tokens[0].equals("STATIC"))
						{
							type = Entity.STATIC;
						} 
						else if (tokens[0].equals("DYNAMIC"))
						{
							type = Entity.DYNAMIC;
						}
					
						String animation = tokens[1];
					
						x = getEntityReplacement(tokens[2]) + prefabOffsetX;
						y = getEntityReplacement(tokens[3]) + prefabOffsetY;
						z = Integer.parseInt(tokens[4]);
						width = (int) getEntityReplacement(tokens[5]);
						height = (int) getEntityReplacement(tokens[6]);
						solid = Boolean.parseBoolean(tokens[7]);
						if (type == Entity.DYNAMIC)
						{
							mass = Integer.parseInt(tokens[8]);
							kinetic = Boolean.parseBoolean(tokens[9]);
							name = tokens[10];
						}
					
						Animation anim = Main.getAnimationHandler().getByName(animation);
						//(Entity.DYNAMIC, resourceHandler.getImage(resourceHandler.getIndexByName("player.png")), true, "Player Entity", 640, 360, 10, 64, 64, 100, false);
						Entity se = new Entity(type, anim.getImageAtFrame(0), width, height,
								solid, x, y, z, 100, false);
						se.setName(name);
						se.setKinetic(kinetic);
						se.setAnimation(anim);
						se.setMass(mass);
						//se.setImageFile(Main.getResourceHandler().getByName(animation));
						
						/*
						BufferedImage buffImg = ImageIO.read(Main.getResourceHandler().getByName(texture));
						if (buffImg.getColorModel().hasAlpha())
							se.setTransparancy(true);
						else
							se.setTransparancy(false);
							*/
						se.setTransparancy(true);
						anim.setEntity(se);
						anim.shouldRun(true);
						
						entities++;
						if (type == Entity.STATIC)
							Main.getEntityHandler().addStaticEntity(se);
						else
							Main.getEntityHandler().addDynamicEntity(se);
						
						Main.println("added " + se);
					}
					else if (tokens[0].equals("TiledBrush") && ! loadsaved)
					{
						tokens = args.split(delims);
						
						int type = -1;
						String name = "Tiled Brush Face";
						String texture;
						double x;
						double y;
						int width;
						int height;
						int tileW = 1;
						int tileH = 1;
						int z;
						boolean solid;
						if (tokens[0].equals("STATIC"))
						{
							type = Entity.STATIC;
						} 
						else if (tokens[0].equals("DYNAMIC"))
						{
							type = Entity.DYNAMIC;
						}
					
						texture = tokens[1];
					
						x = getEntityReplacement(tokens[2]);
						y = getEntityReplacement(tokens[3]) + prefabOffsetY;
						z = Integer.parseInt(tokens[4]);
						width = (int) getEntityReplacement(tokens[5]);
						height = (int) getEntityReplacement(tokens[6]);
						solid = Boolean.parseBoolean(tokens[7]);
						tileW = (int) getEntityReplacement(tokens[8]);
						tileH = (int) getEntityReplacement(tokens[9]);
						if (type == Entity.DYNAMIC)
						{
							Integer.parseInt(tokens[10]);
							name = tokens[11];
						}
						
						//(Entity.DYNAMIC, resourceHandler.getImage(resourceHandler.getIndexByName("player.png")), true, "Player Entity", 640, 360, 10, 64, 64, 100, false);
						boolean transparency;
						BufferedImage buffImg = ImageIO.read(Main.getResourceHandler().getByName(texture));
						if (buffImg.getColorModel().hasAlpha())
							transparency = true;
						else
							transparency = false;
						Image rawImg = buffImg.getScaledInstance((int) (width), 
								(int) (height), Image.SCALE_DEFAULT);
						
						BufferedImage img = new BufferedImage((int) (width * tileW),
								(int) (height * tileH), BufferedImage.TYPE_INT_ARGB);
						Graphics g = img.getGraphics();
						for (int j = 0; j < tileH; j++)
						{
							for (int k = 0; k < tileW; k++)
							{
								g.drawImage(rawImg, (int) (k * width), 
										(int) (j * height), null);
							}
						}
						
						
						Entity e = new Entity(type, img, width * tileW, 
								height * tileH, solid, x, y, z, 100, false);
						e.setVisible(true);
						e.setTransparancy(transparency);
						e.setMaterial(Main.getMaterialHandler().getMaterialByTexture(texture));
						if (type == Entity.STATIC)
						{
							if (! forcedSeperate)
							{
								staticEntityCache[staticEntityIndex] = e;
								staticEntityIndex++;
								Entity se2 = new Entity(Entity.STATIC, solid, name, x, y,
										100);
								se2.setWidth(img.getWidth());
								se2.setHeight(img.getHeight());
								se2.setVisible(false);
								Main.getEntityHandler().addStaticEntity(se2);
							}
							else
							{
								Main.getEntityHandler().addStaticEntity(e);
							}
						}
						else
							Main.getEntityHandler().addDynamicEntity(e);
						
					}
					else if (tokens[0].equals("SeperateTiledBrush") && ! loadsaved)
					{
						tokens = args.split(delims);
						
						int type = -1;
						String texture;
						double x;
						double y;
						int width;
						int height;
						int tileW = 1;
						int tileH = 1;
						int z;
						boolean solid;
						if (tokens[0].equals("STATIC"))
						{
							type = Entity.STATIC;
						} 
						else if (tokens[0].equals("DYNAMIC"))
						{
							type = Entity.DYNAMIC;
						}
					
						texture = tokens[1];
					
						x = getEntityReplacement(tokens[2]);
						y = getEntityReplacement(tokens[3]) + prefabOffsetY;
						z = Integer.parseInt(tokens[4]);
						width = (int) getEntityReplacement(tokens[5]);
						height = (int) getEntityReplacement(tokens[6]);
						solid = Boolean.parseBoolean(tokens[7]);
						tileW = (int) getEntityReplacement(tokens[8]);
						tileH = (int) getEntityReplacement(tokens[9]);
						if (type == Entity.DYNAMIC)
						{
							Integer.parseInt(tokens[10]);
						}
						
						//(Entity.DYNAMIC, resourceHandler.getImage(resourceHandler.getIndexByName("player.png")), true, "Player Entity", 640, 360, 10, 64, 64, 100, false);
						boolean transparency;
						BufferedImage buffImg = ImageIO.read(Main.getResourceHandler().getByName(texture));
						if (buffImg.getColorModel().hasAlpha())
							transparency = true;
						else
							transparency = false;
						Image rawImg = buffImg.getScaledInstance(width, height, Image.SCALE_DEFAULT);
						
						BufferedImage img = new BufferedImage(width * tileW, height * tileH, BufferedImage.TYPE_INT_ARGB);
						Graphics g = img.getGraphics();
						for (int j = 0; j < tileH; j++)
						{
							for (int k = 0; k < tileW; k++)
							{
								g.drawImage(rawImg, (int) k * width, (int) j * height, null);
							}
						}
						

						Entity e = new Entity(type, img, img.getWidth(), img.getHeight(), solid, x, y, z, 100, false);
						e.setVisible(true);
						e.setTransparancy(transparency);
						e.setMaterial(Main.getMaterialHandler().getMaterialByTexture(texture));
						if (type == Entity.STATIC)
							Main.getEntityHandler().addStaticEntity(e);
						else
							Main.getEntityHandler().addDynamicEntity(e);
						
					}
					else if (tokens[0].equals("Logic"))
					{
						tokens = args.split(delims);
						
						String logicType = tokens[0];
						int type = -1;
						double time = 0;
						
						String delims2 = ":";
						String[] tokens2 = tokens[1].split(delims2);
						if (tokens2[0].equals("time"))
						{
							type = MapEntity.TIMER;
						}
						else if (tokens2[0].equals("onTrigger"))
						{
							type = MapEntity.ON_TRIGGER;
						}
						else if (tokens2[0].equals("null"))
						{
							type = -2;
						}
						else
						{
							if (fileType == 0)
								Main.println("Error at " + mapFile.toString() + ":" + (i + 1) + ": unkown logic argument: " + tokens2[0], Color.RED);
							else if (fileType == 1)
								Main.println("Error at " + prefabFile.toString() + ":" + (i + 1) + ": unkown logic argument: " + tokens2[0], Color.RED);		
						}
						
						Trigger recievedTrigger = null;
						if (type == MapEntity.TIMER)
						{
							time = Integer.parseInt(tokens2[2]);
						}
						else if (type == MapEntity.ON_TRIGGER)
						{
							recievedTrigger = Main.getTriggerHandler().getByName(tokens2[1]);
						}
						
						if (logicType.equals("InvokeTrigger"))
						{
							InvokeTrigger invkTrigger = null;
							Trigger triggerEntity = Main.getTriggerHandler().getByName(tokens[2]);
							if (type == MapEntity.TIMER)
								invkTrigger = new InvokeTrigger(type, "invokedtrigger", (long) (time * 1000), triggerEntity);
							else if (type == MapEntity.ON_TRIGGER)
								invkTrigger = new InvokeTrigger(type, "invokedtrigger", recievedTrigger, triggerEntity);
							
							Main.getMapEntityHandler().addEntity(invkTrigger);
						}
						else
						{
							if (fileType == 0)
								Main.println("Error at " + mapFile.toString() + ":" + (i + 1) + ": unkown logic entity: " + tokens[0], Color.RED);
							else if (fileType == 1)
								Main.println("Error at " + prefabFile.toString() + ":" + (i + 1) + ": unkown logic entity: " + tokens[0], Color.RED);		
						}
					}
					else if (tokens[0].equals("Trigger"))
					{
						tokens = args.split(delims);
						
						int type = -1;
						String triggerType;
						boolean once = false;
						boolean con = false;
						double x;
						double y;
						int w;
						int h;
						Entity triggerEntity;
						
						if (tokens[0].equals("STATIC"))
						{
							type = Entity.STATIC;
						} 
						else if (tokens[0].equals("DYNAMIC"))
						{
							type = Entity.DYNAMIC;
						}
						
						triggerType = tokens[1];
						if (triggerType.equals("TriggerMove"))
						{
							int mx;
							int my;
							if (tokens[2].equals("con"))
								con = true;
							else
								once = Boolean.parseBoolean(tokens[2]);
							x = getEntityReplacement(tokens[3]) + prefabOffsetX;
							y = getEntityReplacement(tokens[4]) + prefabOffsetY;
							w = (int) getEntityReplacement(tokens[5]);
							h = (int) getEntityReplacement(tokens[6]);
							triggerEntity = Main.getEntityHandler().getEntityByName(tokens[7]);
							mx = (int) getEntityReplacement(tokens[8]);
							my = (int) getEntityReplacement(tokens[9]);
							String name = "Trigger Brush " + Trigger.getNextID();
							try
							{
								name = tokens[11] + " Trigger";
							}
							catch (Exception e) {}
							Entity te = new Entity(type, false, name, x, y, w, h, 100);
							te.setTransparancy(true);
							
							TriggerMove triggerMove;
							if (! tokens[7].equals("this"))
							{
								triggerMove = new TriggerMove(te, name, triggerEntity);
								Main.getTriggerHandler().addTrigger(triggerMove);
							}
							else
							{
								triggerMove = new TriggerMove(te, name);
								Main.getTriggerHandler().addTrigger(triggerMove);
							}
							
							if (con)
								triggerMove.setContinue(true);
							
							if (! once)
								triggerMove.setDelay(Integer.parseInt(tokens[10]));
							
							triggerMove.setMoveX(mx);
							triggerMove.setMoveY(my);
							
							if (type == Entity.STATIC)
								Main.getEntityHandler().addStaticEntity(te);
							else if (type == Entity.DYNAMIC)
								Main.getEntityHandler().addDynamicEntity(te);
						}
						else if (triggerType.equals("TriggerLoad"))
						{
							String mapLoad;
							
							if (tokens[0].equals("STATIC"))
							{
								type = Entity.STATIC;
							} 
							else if (tokens[0].equals("DYNAMIC"))
							{
								type = Entity.DYNAMIC;
							}
							
							if (tokens[2].equals("con"))
							{
								con = true;
							}
							else
							{
								once = Boolean.parseBoolean(tokens[2]);
							}
							
							x = getEntityReplacement(tokens[3]) + prefabOffsetX;
							y = getEntityReplacement(tokens[4]) + prefabOffsetY;
							w = (int) getEntityReplacement(tokens[5]);
							h = (int) getEntityReplacement(tokens[6]);
							mapLoad = tokens[7];
							double xbias = getEntityReplacement(tokens[8]);
							double ybias = getEntityReplacement(tokens[9]);
							
							String name = "Trigger Brush " + Trigger.getNextID();
							try
							{
								name = tokens[10] + " Trigger";
							}
							catch (Exception e) {}
							Entity te = new Entity(type, false, name, x, y, w, h, 100);
							TriggerLoad triggerLoad = new TriggerLoad(te, name, mapLoad, xbias, ybias);
							
							if (con)
								triggerLoad.setContinue(con);
							
							Main.getTriggerHandler().addTrigger(triggerLoad);
							
							if (type == Entity.STATIC)
								Main.getEntityHandler().addStaticEntity(te);
							else if (type == Entity.DYNAMIC)
								Main.getEntityHandler().addDynamicEntity(te);
						}
						else if (triggerType.equals("TriggerGameEvent"))
						{
							String eventName = "";
							
							if (tokens[0].equals("STATIC"))
							{
								type = Entity.STATIC;
							} 
							else if (tokens[0].equals("DYNAMIC"))
							{
								type = Entity.DYNAMIC;
							}
							
							if (tokens[2].equals("con"))
							{
								con = true;
							}
							else
							{
								once = Boolean.parseBoolean(tokens[2]);
							}
							
							x = getEntityReplacement(tokens[3]) + prefabOffsetX;
							y = getEntityReplacement(tokens[4]) + prefabOffsetY;
							w = (int) getEntityReplacement(tokens[5]);
							h = (int) getEntityReplacement(tokens[6]);
							eventName = tokens[7];
							
							Entity te = new Entity(type, false, "Trigger Game Event", x, y, w, h, 100);
							TriggerGameEvent triggerEvent = new TriggerGameEvent(te, "Trigger Game Event", eventName);
							if (con)
								triggerEvent.setContinue(con);
							
							Main.getTriggerHandler().addTrigger(triggerEvent);
							
							if (type == Entity.STATIC)
								Main.getEntityHandler().addStaticEntity(te);
							else if (type == Entity.DYNAMIC)
								Main.getEntityHandler().addDynamicEntity(te);
							
						}
						else if (triggerType.equals("TriggerStopTrigger"))
						{
							if (tokens[2].equals("con"))
								con = true;
							else
								once = Boolean.parseBoolean(tokens[2]);
							x = getEntityReplacement(tokens[3]) + prefabOffsetX;
							y = getEntityReplacement(tokens[4]) + prefabOffsetY;
							w = (int) getEntityReplacement(tokens[5]);
							h = (int) getEntityReplacement(tokens[6]);
							Trigger stopTrigger = Main.getTriggerHandler().getByName(tokens[7]);
							String name = "Trigger Brush " + Trigger.getNextID();
							try
							{
								name = tokens[11] + " Trigger";
							}
							catch (Exception e) {}
							Entity te = new Entity(type, false, name, x, y, w, h, 100);
							te.setTransparancy(true);
							
							TriggerStopTrigger triggerStop;
							if (! tokens[7].equals("this"))
							{
								triggerStop = new TriggerStopTrigger(te, name, stopTrigger);
								Main.getTriggerHandler().addTrigger(triggerStop);
							}
							else
							{
								triggerStop = new TriggerStopTrigger(te, name, stopTrigger);
								Main.getTriggerHandler().addTrigger(triggerStop);
							}
							
							if (con)
								triggerStop.setContinue(true);							
							
							if (type == Entity.STATIC)
								Main.getEntityHandler().addStaticEntity(te);
							else if (type == Entity.DYNAMIC)
								Main.getEntityHandler().addDynamicEntity(te);
						}
						else if (triggerType.equals("TriggerStartTrigger"))
						{
							if (tokens[2].equals("con"))
								con = true;
							else
								once = Boolean.parseBoolean(tokens[2]);
							x = getEntityReplacement(tokens[3]) + prefabOffsetX;
							y = getEntityReplacement(tokens[4]) + prefabOffsetY;
							w = (int) getEntityReplacement(tokens[5]);
							h = (int) getEntityReplacement(tokens[6]);
							Trigger startTrigger = Main.getTriggerHandler().getByName(tokens[7]);
							String name = "Trigger Brush " + Trigger.getNextID();
							try
							{
								name = tokens[11] + " Trigger";
							}
							catch (Exception e) {}
							Entity te = new Entity(type, false, name, x, y, w, h, 100);
							te.setTransparancy(true);
							
							TriggerStartTrigger triggerStart;
							if (! tokens[7].equals("this"))
							{
								triggerStart = new TriggerStartTrigger(te, name, startTrigger);
								Main.getTriggerHandler().addTrigger(triggerStart);
							}
							else
							{
								triggerStart = new TriggerStartTrigger(te, name, startTrigger);
								Main.getTriggerHandler().addTrigger(triggerStart);
							}
							
							if (con)
								triggerStart.setContinue(true);							
							
							if (type == Entity.STATIC)
								Main.getEntityHandler().addStaticEntity(te);
							else if (type == Entity.DYNAMIC)
								Main.getEntityHandler().addDynamicEntity(te);
						}
						else if (triggerType.equals("TriggerGravityDecay"))
						{
							if (tokens[2].equals("con"))
								con = true;
							else
								once = Boolean.parseBoolean(tokens[2]);
							x = getEntityReplacement(tokens[3]) + prefabOffsetX;
							y = getEntityReplacement(tokens[4]) + prefabOffsetY;
							w = (int) getEntityReplacement(tokens[5]);
							h = (int) getEntityReplacement(tokens[6]);
							double gravity = getEntityReplacement(tokens[7]);
							double decFac = getEntityReplacement(tokens[8]);
							int rate = (int) getEntityReplacement(tokens[9]);
							String name = "Trigger Brush " + Trigger.getNextID();
							try
							{
								name = tokens[11] + " Trigger";
							}
							catch (Exception e) {}
							Entity te = new Entity(type, false, name, x, y, w, h, 100);
							te.setTransparancy(true);
							
							TriggerGravityDecay triggerDecay;
							if (! tokens[7].equals("this"))
							{
								triggerDecay = new TriggerGravityDecay(te, name, gravity, decFac, rate);
								Main.getTriggerHandler().addTrigger(triggerDecay);
							}
							else
							{
								triggerDecay = new TriggerGravityDecay(te, name, gravity, decFac, rate);
								Main.getTriggerHandler().addTrigger(triggerDecay);
							}
							
							if (con)
								triggerDecay.setContinue(true);							
							
							if (type == Entity.STATIC)
								Main.getEntityHandler().addStaticEntity(te);
							else if (type == Entity.DYNAMIC)
								Main.getEntityHandler().addDynamicEntity(te);
						}
						else if (triggerType.equals("TriggerBreak"))
						{
							if (tokens[2].equals("con"))
								con = true;
							else
								once = Boolean.parseBoolean(tokens[2]);
							x = getEntityReplacement(tokens[3]) + prefabOffsetX;
							y = getEntityReplacement(tokens[4]) + prefabOffsetY;
							w = (int) getEntityReplacement(tokens[5]);
							h = (int) getEntityReplacement(tokens[6]);
							Entity toStop = Main.getEntityHandler().getEntityByName(tokens[7]);
							String name = "Trigger Brush " + Trigger.getNextID();
							try
							{
								name = tokens[11] + " Trigger";
							}
							catch (Exception e) {}
							Entity te = new Entity(type, false, name, x, y, w, h, 100);
							te.setTransparancy(true);
							
							TriggerBreak triggerStop;
							if (! tokens[7].equals("this"))
							{
								triggerStop = new TriggerBreak(te, name, toStop);
								Main.getTriggerHandler().addTrigger(triggerStop);
							}
							else
							{
								triggerStop = new TriggerBreak(te, name, toStop);
								Main.getTriggerHandler().addTrigger(triggerStop);
							}
							
							if (con)
								triggerStop.setContinue(true);							
							
							if (type == Entity.STATIC)
								Main.getEntityHandler().addStaticEntity(te);
							else if (type == Entity.DYNAMIC)
								Main.getEntityHandler().addDynamicEntity(te);
						}
						else if (triggerType.equals("TriggerSound"))
						{
							if (tokens[2].equals("con"))
								con = true;
							else
								once = Boolean.parseBoolean(tokens[2]);
							x = getEntityReplacement(tokens[3]) + prefabOffsetX;
							y = getEntityReplacement(tokens[4]) + prefabOffsetY;
							w = (int) getEntityReplacement(tokens[5]);
							h = (int) getEntityReplacement(tokens[6]);
							String sound = tokens[7];
							int soundType = -1;
							String soundTypeStr = tokens[8];
							if (soundTypeStr.equals("GENERIC"))
								soundType = GameSound.UNSINGED;
							else if (soundTypeStr.equals("MUSIC"))
								soundType = GameSound.MUSIC;
							else if (soundTypeStr.equals("EFFECT"))
								soundType = GameSound.EFFECT;
							else if (soundTypeStr.equals("*")) {}
							else
								Main.println("WARNING: unkown sound type \"" + soundTypeStr + "\" ", Color.YELLOW);
							String name = "Trigger Brush " + Trigger.getNextID();
							try
							{
								name = tokens[9] + " Trigger";
							}
							catch (Exception e) {}
							Entity te = new Entity(type, false, name, x, y, w, h, 100);
							te.setTransparancy(true);
							
							TriggerSound soundTrigger;
							if (! tokens[7].equals("this"))
							{
								soundTrigger = new TriggerSound(te, sound, soundType, name);
								Main.getTriggerHandler().addTrigger(soundTrigger);
							}
							else
							{
								soundTrigger = new TriggerSound(te, sound, soundType, name);
								Main.getTriggerHandler().addTrigger(soundTrigger);
							}
							
							if (con)
								soundTrigger.setContinue(true);							
							
							if (type == Entity.STATIC)
								Main.getEntityHandler().addStaticEntity(te);
							else if (type == Entity.DYNAMIC)
								Main.getEntityHandler().addDynamicEntity(te);
						}
						else if (triggerType.equals("TriggerShader"))
						{
							if (tokens[2].equals("con"))
								con = true;
							else
								once = Boolean.parseBoolean(tokens[2]);
							x = getEntityReplacement(tokens[3]) + prefabOffsetX;
							y = getEntityReplacement(tokens[4]) + prefabOffsetY;
							w = (int) getEntityReplacement(tokens[5]);
							h = (int) getEntityReplacement(tokens[6]);
							String shaderName = tokens[7];
							String name = "Trigger Brush " + Trigger.getNextID();
							try
							{
								name = tokens[8] + " Trigger";
							}
							catch (Exception e) {}
							Entity te = new Entity(type, false, name, x, y, w, h, 100);
							te.setTransparancy(true);
							
							TriggerShader shaderTrigger;
							if (! tokens[7].equals("this"))
							{
								shaderTrigger = new TriggerShader(te, name, shaderName);
								Main.getTriggerHandler().addTrigger(shaderTrigger);
							}
							else
							{
								shaderTrigger = new TriggerShader(te, name, shaderName);
								Main.getTriggerHandler().addTrigger(shaderTrigger);
							}
							
							if (con)
								shaderTrigger.setContinue(true);							
							
							if (type == Entity.STATIC)
								Main.getEntityHandler().addStaticEntity(te);
							else if (type == Entity.DYNAMIC)
								Main.getEntityHandler().addDynamicEntity(te);
						}
						else if (triggerType.equals("TriggerGravity"))
						{
							if (tokens[2].equals("con"))
								con = true;
							else
								once = Boolean.parseBoolean(tokens[2]);
							x = getEntityReplacement(tokens[3]) + prefabOffsetX;
							y = getEntityReplacement(tokens[4]) + prefabOffsetY;
							w = (int) getEntityReplacement(tokens[5]);
							h = (int) getEntityReplacement(tokens[6]);
							double grav = getEntityReplacement(tokens[7]);
							String name = "Trigger Brush " + Trigger.getNextID();
							try
							{
								name = tokens[8] + " Trigger";
							}
							catch (Exception e) {}
							Entity te = new Entity(type, false, name, x, y, w, h, 100);
							te.setTransparancy(true);
							
							TriggerGravity gravityTrigger;
							if (! tokens[7].equals("this"))
							{
								gravityTrigger = new TriggerGravity(te, name, grav);
								Main.getTriggerHandler().addTrigger(gravityTrigger);
							}
							else
							{
								gravityTrigger = new TriggerGravity(te, name, grav);
								Main.getTriggerHandler().addTrigger(gravityTrigger);
							}
							
							if (con)
								gravityTrigger.setContinue(true);							
							
							if (type == Entity.STATIC)
								Main.getEntityHandler().addStaticEntity(te);
							else if (type == Entity.DYNAMIC)
								Main.getEntityHandler().addDynamicEntity(te);
							
						}
						else if (triggerType.equals("TriggerHurt"))
						{
							if (tokens[2].equals("con"))
								con = true;
							else
								once = Boolean.parseBoolean(tokens[2]);
							x = getEntityReplacement(tokens[3]) + prefabOffsetX;
							y = getEntityReplacement(tokens[4]) + prefabOffsetY;
							w = (int) getEntityReplacement(tokens[5]);
							h = (int) getEntityReplacement(tokens[6]);
							int damage = (int) getEntityReplacement(tokens[7]);
							String name = "Trigger Brush " + Trigger.getNextID();
							try
							{
								name = tokens[8] + " Trigger";
							}
							catch (Exception e) {}
							Entity te = new Entity(type, false, name, x, y, w, h, 100);
							te.setTransparancy(true);
							
							TriggerHurt hurtTrigger;
							if (! tokens[7].equals("this"))
							{
								hurtTrigger = new TriggerHurt(te, name, damage, TriggerHurt.GENERIC);
								Main.getTriggerHandler().addTrigger(hurtTrigger);
							}
							else
							{
								hurtTrigger = new TriggerHurt(te, name, damage, TriggerHurt.GENERIC);
								Main.getTriggerHandler().addTrigger(hurtTrigger);
							}
							
							if (con)
								hurtTrigger.setContinue(true);							
							
							if (type == Entity.STATIC)
								Main.getEntityHandler().addStaticEntity(te);
							else if (type == Entity.DYNAMIC)
								Main.getEntityHandler().addDynamicEntity(te);
							
						}
						else if (triggerType.equals("TriggerToggleEntity"))
						{
							if (tokens[2].equals("con"))
								con = true;
							else
								once = Boolean.parseBoolean(tokens[2]);
							x = getEntityReplacement(tokens[3]) + prefabOffsetX;
							y = getEntityReplacement(tokens[4]) + prefabOffsetY;
							w = (int) getEntityReplacement(tokens[5]);
							h = (int) getEntityReplacement(tokens[6]);
							String me = tokens[7];
							boolean state = Boolean.parseBoolean(tokens[8]);
							String name = "Trigger Brush " + Trigger.getNextID();
							try
							{
								name = tokens[9] + " Trigger";
							}
							catch (Exception e) {}
							Entity te = new Entity(type, false, name, x, y, w, h, 100);
							te.setTransparancy(true);
							
							TriggerToggleMapEntity toggleEntity;
							if (! tokens[7].equals("this"))
							{
								toggleEntity = new TriggerToggleMapEntity(te, name, me, state);
								Main.getTriggerHandler().addTrigger(toggleEntity);
							}
							else
							{
								toggleEntity = new TriggerToggleMapEntity(te, name, me, state);
								Main.getTriggerHandler().addTrigger(toggleEntity);
							}
							
							if (con)
								toggleEntity.setContinue(true);							
							
							if (type == Entity.STATIC)
								Main.getEntityHandler().addStaticEntity(te);
							else if (type == Entity.DYNAMIC)
								Main.getEntityHandler().addDynamicEntity(te);
						}
						else if (triggerType.equals("TriggerToggleSolid"))
						{
							String entityName = "";
							
							if (tokens[0].equals("STATIC"))
							{
								type = Entity.STATIC;
							} 
							else if (tokens[0].equals("DYNAMIC"))
							{
								type = Entity.DYNAMIC;
							}
							
							if (tokens[2].equals("con"))
							{
								con = true;
							}
							else
							{
								once = Boolean.parseBoolean(tokens[2]);
							}
							
							x = getEntityReplacement(tokens[3]) + prefabOffsetX;
							y = getEntityReplacement(tokens[4]) + prefabOffsetY;
							w = (int) getEntityReplacement(tokens[5]);
							h = (int) getEntityReplacement(tokens[6]);
							entityName = tokens[7];
							
							Entity te = new Entity(type, false, "Trigger Game Event", x, y, w, h, 100);
							TriggerToggleSolid triggerSolid = new TriggerToggleSolid(te, "Trigger Game Event", entityName);
							if (con)
								triggerSolid.setContinue(con);
							
							Main.getTriggerHandler().addTrigger(triggerSolid);
							
							if (type == Entity.STATIC)
								Main.getEntityHandler().addStaticEntity(te);
							else if (type == Entity.DYNAMIC)
								Main.getEntityHandler().addDynamicEntity(te);
							
						}
					}
					else if (tokens[0].equals("Background"))
					{
						if (fileType == 1)
						{
							Main.println("Error at " + prefabFile.toString() + ":" + (i + 1) + ": prefabs cannot set global map attributes", Color.RED);
							return -1;
						}
						
						tokens = args.split(delims);
						
						int r;
						int g;
						int b;
						
						r = (int) getEntityReplacement(tokens[0]);
						g = (int) getEntityReplacement(tokens[1]);
						b = (int) getEntityReplacement(tokens[2]);
						
						Main.getGameWindow().drawBackground = false;
						Main.getGameWindow().backgroundColor = new Color(r, g, b);
					}
					else if (tokens[0].equals("Decal") && ! loadsaved)
					{
						tokens = args.split(delims);
						String texture = tokens[0];
						String brushName = tokens[1];
						double x = Double.parseDouble(tokens[2]);
						double y = Double.parseDouble(tokens[3]); 
						
						Image img = Main.getResourceHandler().getImage(
								Main.getResourceHandler().getIndexByName(texture, true));
						BufferedImage buff = new BufferedImage(img.getWidth(null), img.getHeight(null), 
								BufferedImage.TYPE_INT_ARGB);
						Graphics g = buff.getGraphics();
						g.drawImage(buff, 0, 0, null);
						
						Decal dec = new Decal(buff, brushName, (int) x, (int) y);
						int res = dec.update();
						if (res == Decal.APPLY_FAIL)
						{
							System.out.println("searching entity cache...");
							int k = 0;
							for (k = 0; k < staticEntityCache.length; k++)
							{
								if (staticEntityCache[k] != null)
								{
									if (staticEntityCache[k].getName().equals(brushName))
									{
										System.out.println("found.");
										int res2 = dec.update(staticEntityCache[k]);
										if (res2 == Decal.APPLY_FAIL)
										{
											Main.println("Error at " + mapFile.toString() + ":" + (i + 1) + ": error applying decal", Color.YELLOW);
										}
										break;
									}
								}
							}
							if (k == staticEntityCache.length)
								System.out.println("not found");
						}
					}
					else if (tokens[0].equals("Entity"))
					{
						tokens = args.split(delims);
					
						String entityType;
					
						entityType = tokens[0];
					
						if (entityType.equals("PLAYER_START"))
						{
							if (fileType == 1)
							{
								Main.println("Error at " + prefabFile.toString() + ":" + (i + 1) + ": prefabs cannot set global map attributes", Color.RED);
								return -1;
							}
							
							xSpawn = Double.parseDouble(tokens[1]);
							ySpawn = Double.parseDouble(tokens[2]);
							Main.player_x = Double.parseDouble(tokens[1]);
							Main.player_y = Double.parseDouble(tokens[2]);
						}
						else if (entityType.equals("CAMERA"))
						{
							if (fileType == 1)
							{
								Main.println("Error at " + prefabFile.toString() + ":" + (i + 1) + ": prefabs cannot set global map attributes", Color.RED);
								return -1;
							}
							
							Main.getGameWindow().cameraXBounds[0] = (int) (Integer.parseInt(tokens[1]) * Main.resolutionScaleX);
							Main.getGameWindow().cameraXBounds[1] = (int) (Integer.parseInt(tokens[2]) * Main.resolutionScaleX);
							Main.getGameWindow().cameraYBounds[0] = (int) (Integer.parseInt(tokens[3]) * Main.resolutionScaleY);
							Main.getGameWindow().cameraYBounds[1] = (int) (Integer.parseInt(tokens[4]) * Main.resolutionScaleY);
						}
						else if (entityType.equals("VARIABLE"))
						{
							tokens = args.split(delims);
							
							String name;
							int value;
							
							name = tokens[1];
							value = (int) getEntityReplacement(tokens[2]);
							
							variables[numVariables] = value;
							variableNames[numVariables] = name;
							numVariables++;
						}
						else if (entityType.equals("GEM"))
						{
							tokens = args.split(delims);
							int x;
							int y;
							int type = Gem.GREEN_GEM;
							if (tokens[1].equals("BLUE"))
								type = Gem.BLUE_GEM;
							else if (tokens[1].equals("GREEN"))
								type = Gem.GREEN_GEM;
							else if (tokens[1].equals("YELLOW"))
								type = Gem.YELLOW_GEM;
							x = (int) getEntityReplacement(tokens[2]);
							y = (int) getEntityReplacement(tokens[3]);
							Gem gem = new Gem(type, x, y, tokens[4]);
							Main.getMapEntityHandler().addEntity(gem);
						}
						else if (entityType.equals("TRACK"))
						{
							tokens = args.split(delims);
							int type = -1;
							if (tokens[1].equals("YPLANE"))
								type = Track.Y_PLANE;
							else if (tokens[1].equals("XPLANE"))
								type = Track.X_PLANE;
							double dest = getEntityReplacement(tokens[2]);
							int delay = (int) getEntityReplacement(tokens[3]);
							Entity e = Main.getEntityHandler().getEntityByName(tokens[4]);
							boolean playerCollide = Boolean.parseBoolean(tokens[5]);
							String name = tokens[6];
							Track track = new Track(name, type, dest, delay, e, playerCollide);
							
							Main.getMapEntityHandler().addEntity(track);
							Main.println("added 1", Color.BLUE);
						}
						else if (entityType.equals("PARTICLE_EFFECT"))
						{
							tokens = args.split(delims);
							double x = getEntityReplacement(tokens[1]);
							double y = getEntityReplacement(tokens[2]);
							String partName = tokens[3];
							
							ParticleEffect part = new ParticleEffect(
									Main.getParticleEffectHandler().getFromBuffer(partName));
							part.getParticleArguments().setX(x);
							part.getParticleArguments().setY(y);
							Main.getParticleEffectHandler().addParticleEffect(part);
							part.shouldRun(true);
						}
						else if (entityType.equals("NODE"))
						{
							tokens = args.split(delims);
							double x = getEntityReplacement(tokens[1]);
							double y = getEntityReplacement(tokens[2]);
							String name = tokens[3];
							
							Node node = new Node(name, x, y);
							Main.getNodeHandler().addNode(node);
						}
						else if (entityType.equals("PARENT"))
						{
							tokens = args.split(delims);
							String ent1 = tokens[1];
							String ent2 = tokens[2];
							Entity e = Main.getEntityHandler().getEntityByName(ent1);
							Entity e2 = Main.getEntityHandler().getEntityByName(ent2);
							if (e == null || e2 == null)
							{
								if (e == null)
								{
									if (fileType == 0)
										Main.println("Error at " + mapFile.toString() + ":" + (i + 1) + ": unknown object: " + ent1, Color.RED);
									else if (fileType == 1)
										Main.println("Error at " + prefabFile.toString() + ":" + (i + 1) + ": unkown object: " + ent1, Color.RED);
								}
								if (e2 == null)
								{
									if (fileType == 0)
										Main.println("Error at " + mapFile.toString() + ":" + (i + 1) + ": unknown object: " + ent2, Color.RED);
									else if (fileType == 1)
										Main.println("Error at " + prefabFile.toString() + ":" + (i + 1) + ": unkown object: " + ent2, Color.RED);
								}
							}
							else
							{
								e.parentTo(e2);
								e.setObstinate(Boolean.parseBoolean(tokens[3]));
							}
						}
						else
						{
							if (fileType == 0)
								Main.println("Error at " + mapFile.toString() + ":" + (i + 1) + ": unkown entity: " + entityType, Color.RED);
							else if (fileType == 1)
								Main.println("Error at " + prefabFile.toString() + ":" + (i + 1) + ": unkown entity: " + entityType, Color.RED);
						}
					}
					else if (tokens[0].equals("Prefab"))
					{
						tokens = args.split(delims);
						prefabOffsetX = (int) getEntityReplacement(tokens[1]);
						prefabOffsetY = (int) getEntityReplacement(tokens[2]);
						readPrefab(new File("maps\\prefabs\\" + tokens[0]));
						int prefabReturn = loadMap(true, loadsaved);
						if (prefabReturn != 1)
							return prefabReturn;
						prefabOffsetX = 0;
						prefabOffsetY = 0;
					}
				}
			}
			if (fileType == 0)
			{
				if (! versionFound)
				{
					Main.println("WARNING: Map version not found!  Assuming full compatability.", Color.YELLOW);
				}
				
				File lightingFile = new File("maps\\lighting\\" + mapname + ".lighting");
				if (lightingFile.exists() && ! Main.useDynamicLighting)
				{
					Image lighting = ImageIO.read(lightingFile);
					Main.getGameWindow().lighting = lighting;
				}
				else if (! Main.useDynamicLighting && Main.BUILD_LIGHTING)
				{
					Main.getLightHandler().buildLighting();
					Image lighting = ImageIO.read(lightingFile);
					Main.getGameWindow().lighting = lighting;
				}
				
				if (! forcedSeperate)
				{
					BufferedImage buff = new BufferedImage(GameWindow.XRES_GL, GameWindow.YRES_GL, BufferedImage.TYPE_INT_ARGB);
					Graphics g = buff.getGraphics();
					Arrays.sort(staticEntityCache, Entity.EntityZComparator);
					for (int i2 = 0; i2 < staticEntityCache.length; i2++)
					{
						if (staticEntityCache[i2] != null)
						{
							if (staticEntityCache[i2].getBufferedImage() != null)
							{
								g.drawImage(staticEntityCache[i2].getBufferedImage(), 
									(int) staticEntityCache[i2].getX(), 
									(int) staticEntityCache[i2].getY(),
									null);
							}
						}
					}
					//Entity e2 = new Entity(type, img, img.getWidth(), img.getHeight(), solid, x, y, z, 100, false);
					//Entity e = new Entity(Entity.STATIC, buff, 1920, 1080, false, 0, 0,
							//-100, 100, false);
					//Main.getEntityHandler().addStaticEntity(e);
					long time = System.currentTimeMillis() - start;
					if (Main.getCurrentResourceMonitor() != null)
						Main.getCurrentResourceMonitor().increment(ResourceMonitor.Type.OTHER, time);
				}
				
				if (Main.DRAW_NODES)
				{
					for (int i1 = 0; i1 < Main.getNodeHandler().getNodes().length; i1++)
					{
						if (Main.getNodeHandler().getNodes()[i1] != null)
						{
							Entity se = new Entity(Entity.STATIC, new File("resources\\textures\\" + ResourceHandler.nodeTexture), 64, 64,
									false, "node ent", Main.getNodeHandler().getNodes()[i1].getX(), Main.getNodeHandler().getNodes()[i1].getY(), 1000, 64, 64, 100, false);
							se.setTransparancy(true);
							Main.getEntityHandler().addStaticEntity(se);
						}
					}
				}
				
				Main.getEntityHandler().forceSort();
				Main.getReflectionHandler().addAll();
				
				Main.println("Loaded map: " + mapname, Color.GREEN);
				Main.getPhysicsHandler().lastGravity = Main.getPhysicsHandler().getGravity();
				//Track track = new Track(Track.X_PLANE, 0.25, 1, Main.getEntityHandler().getEntityByName("PhysicsBlock"), 0);
				//Main.getMapEntityHandler().addEntity(track);
				Main.getMapEntityHandler().startAll();
				Main.getTriggerHandler().enableAll();
				Main.getReflectionHandler().update(true);	
			}	
			return 1;
		}
		catch (Exception e)
		{
			if (fileType == 0)
				Main.println("Error at " + mapFile.toString() + ":" + (i + 1) + ": " + e, Color.RED);
			else if (fileType == 1)
				Main.println("Error at " + prefabFile.toString() + ":" + (i + 1) + ": " + e, Color.RED);
			
			e.printStackTrace(System.out);
			return -1;
		}
	}

	private double getEntityReplacement(String str)
	{
		if (str.charAt(0) == '%')
		{
			int i = 1;
			String type = "";
			while (str.charAt(i) != ':')
			{
				type = type + str.substring(i, i + 1);
				i++;
			}

			if (type.equals("Random"))
			{
				int start;
				int end;
				String tokens[];
				String delims = ":";
				tokens = str.split(delims);
				start = Integer.parseInt(tokens[1]);
				end = Integer.parseInt(tokens[2]);
				return getNext(start, end);
			}
			else if (type.equals("Variable"))
			{
				String name;
				String tokens[];
				String delims = ":";
				tokens = str.split(delims);
				name = tokens[1];
				int value = getVariableByName(name);
				if (value != Integer.MAX_VALUE)
				{
					return value;
				}
			}
		}
		return Double.parseDouble(str);
	}
	
	/**
	 * Returns the current map name
	 * 
	 * @return The current map name
	 */
	public String getMapName()
	{
		return mapname;
	}
	
	private int getNext(int start, int end)
	{
		Random r = new Random();
		int num = r.nextInt(end - start) + start;
		return num;
	}
	
	/**
	 * Returns a map variable by its name
	 * 
	 * @param name The name of the variable
	 * @return The value of the variable, returns
	 * <code>Integer.MAX_VALUE</code> if it does
	 * not exist
	 */
	public int getVariableByName(String name)
	{
		for (int i = 0; i < variables.length; i++)
		{
			if (variableNames[i] != null)
			{
				if (variableNames[i].equals(name))
				{
					return variables[i];
				}
			}
		}
		return Integer.MAX_VALUE;
	}
	
	/**
	 * Clears the map variables
	 */
	public void clearVariables()
	{
		for (int i = 0; i < variables.length; i++)
		{
			variableNames[i] = null;
			variables[i] = 0;
		}
		numVariables = 0;
	}
	
	/**
	 * Loads from a save file
	 * 
	 * @return The return status
	 */
	public int loadSaved()
	{
		save = new File(mapFile.toString() + ".entitydata");
		int i = 0;
		try
		{
			
			Scanner in = new Scanner(save);
			int lines = 0;
			while (in.hasNextLine())
			{
				lines++;
				in.nextLine();
			}
			in.close();
			in = new Scanner(save);
			map = new String[lines];
			for (i = 0; i < lines; i++)
			{
				map[i] = in.nextLine();
			}
			
			for (i = 0; i < map.length; i++)
			{
				Entity e = Entity.newFromString(map[i]);
				if (e.getName().equals("Player")) {}
				else
				{
					if (e.isStatic())
					{
						Main.getEntityHandler().addStaticEntity(e);
					}
					else
					{
						Main.getEntityHandler().addDynamicEntity(e);
					}
					entities++;
				}
			}
			in.close();
			Main.println("Loaded: " + save.toString(), Color.GREEN);
			return 1;
		}
		catch (Exception e)
		{
			if (i < map.length)
			{
				Main.println("Error at " + save.toString() + ":" + (i + 1) + ": " + e, Color.RED);
				e.printStackTrace(System.out);
				return -1;
			}
			else
			{
				Main.println("Internal error at " + e.getStackTrace()[0].getClass() + 
						" : " + e, Color.RED);
				e.printStackTrace(System.out);
				return -1;
			}
		}
	}
	
	/**
	 * Updates the map's load state
	 * 
	 * @param loadsaved Whether the map loads
	 * from .entitydata file
	 */
	@SuppressWarnings("unused")
	@Deprecated
	public void updateState(boolean loadsaved)
	{
		if (true)
			return;
		data = new File(mapFile.toString() 	+ ".dat");
		try
		{
			timesloaded++;
			if (timesloaded > 1 && (new File(mapFile.toString() + ".entitydata")).exists())
			{
				loadsaved = true;
				this.loadsaved = true;
			}
			PrintStream out = new PrintStream(data);
			out.println("loadsaved = " + loadsaved);
			out.println("timesloaded = " + timesloaded);
			out.close();
		}
		catch (Exception e)
		{
			Main.println("error at: " + data.toString() + ": couldn't update map state", Color.RED);
			e.printStackTrace(System.out);
		}
	}
	
	/**
	 * Prints to a save file
	 */
	public void printToTemp()
	{
		File tempFile = new File("maps\\" + mapname + ".entitydata");
		try 
		{
			if (! tempFile.exists())
				tempFile.createNewFile();
			
			PrintStream out = new PrintStream(tempFile);
			EntityHandler entityHandler = Main.getEntityHandler();
			Main.println("Saving state...");
			for (int i = 0; i < entityHandler.getDynEntities(); i++)
			{
				if (entityHandler.getEntity(i) != null)
				{
					if (entityHandler.getEntity(i).getName().contains("Trigger")) {}
					else
					{
						out.println(entityHandler.getEntity(i).getAttributes());
					}
				}
			}
	Main.println("Saved.", Color.GREEN);
			out.close();
		}
		catch (Exception e) 
		{
			Main.println("error at: " + tempFile.toString() + ": couldn't save map", Color.RED);
			e.printStackTrace(System.out);
		}
	}
	
	/**
	 * Returns whether the loader will load from a save file
	 * 
	 * @return Whether the <code>MapLoader</code> will load
	 * from a save file
	 */
	public boolean getLoadSaved()
	{
		return loadsaved;
	}
}