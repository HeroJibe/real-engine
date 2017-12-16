package core;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.Closeable;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Random;
import java.util.Scanner;

import javax.imageio.ImageIO;

import core.MapEntities.Gem;
import core.MapEntities.InvokeTrigger;
import core.MapEntities.MapEntity;
import core.MapEntities.Track;
import core.Triggers.TriggerGameEvent;
import core.Triggers.TriggerGravity;
import core.Triggers.TriggerHurt;
import core.Triggers.TriggerLoad;
import core.Triggers.TriggerMove;
import core.Triggers.TriggerShader;
import core.Triggers.TriggerSound;
import core.Triggers.TriggerStartTrigger;
import core.Triggers.TriggerStopTrigger;
import gui.GameWindow;
import main.Main;

public class MapLoader
{
	public static final int MAX_VARIABLES = 16;
	public boolean shouldLoadSaved = false;
	
	private String[] map;
	private String[] prefab;
	private String mapname;
	private File mapFile;
	private File prefabFile;
	private Random r;
	private int prefabOffsetX = 0;
	private int prefabOffsetY = 0;
	private boolean loadsaved = false;
	private File data;
	private File save;
	private int timesloaded;

	private int variables[] = new int[MAX_VARIABLES];
	private String variableNames[] = new String[MAX_VARIABLES];
	private int numVariables = 0;
	
	public MapLoader()
	{
		r = new Random();
	}
	
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
					e.printStackTrace();
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
					e.printStackTrace();
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
		}
	}
	
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
		}
	}
	
	int entities = 0;
	@SuppressWarnings("unused")
	public int loadMap(boolean prefab, boolean loadsaved)
	{
		Main.getGameWindow().lighting = null;
		if (! prefab)
		{
			Main.getEntityHandler().removeAllEntities();
			Main.getTriggerHandler().clearAllTriggers();
			clearVariables();
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
		
		Main.getGameWindow().drawBackground = false;
		
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
							Main.println("Error at " + prefabFile.toString() + ":" + (i + 1) + ": prefabs cannot set global map attributes");
							return -1;
						}
						if (mapname != null)
						{
							Main.println("Error at " + mapFile.toString() + ":" + (i + 1) + ": map name cannot be set twice");
							return -1;
						}
						mapname = tokens[1] + ".map";
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
					
						//(Entity.DYNAMIC, resourceHandler.getImage(resourceHandler.getIndexByName("player.png")), true, "Player Entity", 640, 360, 10, 64, 64, 100, false);
						Entity se = new Entity(type, Main.getResourceHandler().getByName(texture), width, height,
								solid, name, x, y, z, width, height, 100, false);
						se.setKinetic(kinetic);
						
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
					else if (tokens[0].equals("AnimatedBrush") && ! loadsaved)
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
						int mass = 1;
					
					
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
							mass = Integer.parseInt(tokens[10]);
							name = tokens[11];
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
						int l = 0;
						for (int j = 0; j < tileH; j++)
						{
							for (int k = 0; k < tileW; k++)
							{
								g.drawImage(rawImg, (int) k * width, (int) j * height, null);
								l++;
							}
						}
						

						Entity e = new Entity(type, img, img.getWidth(), img.getHeight(), solid, x, y, z, 100, false);
						e.setVisible(true);
						e.setTransparancy(transparency);
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
						
						String timerStart = null;
						Trigger recievedTrigger = null;
						if (type == MapEntity.TIMER)
						{
							timerStart = tokens2[1];
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
								invkTrigger = new InvokeTrigger(type, (long) (time * 1000), triggerEntity);
							else if (type == MapEntity.ON_TRIGGER)
								invkTrigger = new InvokeTrigger(type, recievedTrigger, triggerEntity);
							
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
							String name = "Trigger Brush " + Trigger.getNextID();
							try
							{
								name = tokens[8] + " Trigger";
							}
							catch (Exception e) {}
							Entity te = new Entity(type, false, name, x, y, w, h, 100);
							te.setTransparancy(true);
							
							TriggerSound soundTrigger;
							if (! tokens[7].equals("this"))
							{
								soundTrigger = new TriggerSound(te, sound, name);
								Main.getTriggerHandler().addTrigger(soundTrigger);
							}
							else
							{
								soundTrigger = new TriggerSound(te, sound, name);
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
								hurtTrigger = new TriggerHurt(te, name, damage);
								Main.getTriggerHandler().addTrigger(hurtTrigger);
							}
							else
							{
								hurtTrigger = new TriggerHurt(te, name, damage);
								Main.getTriggerHandler().addTrigger(hurtTrigger);
							}
							
							if (con)
								hurtTrigger.setContinue(true);							
							
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
						
						Main.getGameWindow().drawBackground = true;
						Main.getGameWindow().backgroundColor = new Color(r, g, b);
					}
					else if (tokens[0].equals("Decal") && ! loadsaved)
					{
						String name = "Brush Decal";
						tokens = args.split(delims);
						String type = tokens[0];
						String texture = tokens[1];
						double x = Double.parseDouble(tokens[2]);
						double y = Double.parseDouble(tokens[3]);
						int w = Integer.parseInt(tokens[4]);
						int h = Integer.parseInt(tokens[5]);
						try
						{
							name = tokens[6];
						}
						catch (ArrayIndexOutOfBoundsException e)
						{ }
						
						int numType = -1;
						if (type.equals("STATIC"))
							numType = Entity.STATIC;
						else if (type.equals("DYNAMIC"))
							numType = Entity.DYNAMIC;
						
						Entity de = new Entity(numType, Main.getResourceHandler().getByName(texture), 
								w, h, false, name, x, y, 0, w, h, 100, false);
						de.setDecal(true);
						
						if (type.equals("STATIC"))
						{
							Main.getEntityHandler().addStaticEntity(de);
						}
						else if (type.equals("DYNAMIC"))
						{
							Main.getEntityHandler().addDynamicEntity(de);
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
							Track track = new Track(type, dest, delay, e, playerCollide);
							Main.getMapEntityHandler().addEntity(track);
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
				
				
				Main.println("Loaded map: " + mapname, Color.GREEN);
				Main.getPhysicsHandler().lastGravity = Main.getPhysicsHandler().gravity;
				//Track track = new Track(Track.X_PLANE, 0.25, 1, Main.getEntityHandler().getEntityByName("PhysicsBlock"), 0);
				//Main.getMapEntityHandler().addEntity(track);
				Main.getMapEntityHandler().startAll();
				Main.getTriggerHandler().enableAll();
				
				
			}
			else
			{
				//Main.println("Loaded: " + prefabFile.toString());
			}
			return 1;
		}
		catch (Exception e)
		{
			if (fileType == 0)
				Main.println("Error at " + mapFile.toString() + ":" + (i + 1) + ": " + e, Color.RED);
			else if (fileType == 1)
				Main.println("Error at " + prefabFile.toString() + ":" + (i + 1) + ": " + e, Color.RED);
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
				String temp = "";
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
	
	public void clearVariables()
	{
		for (int i = 0; i < variables.length; i++)
		{
			variableNames[i] = null;
			variables[i] = 0;
		}
		numVariables = 0;
	}
	
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
			
			int entities = 0;
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
			Main.println("Error at " + save.toString() + ":" + (i + 1) + ": " + e, Color.RED);
			return -1;
		}
	}
	
	public void updateState(boolean loadsaved)
	{
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
		}
	}
	
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
		}
	}
	
	public boolean getLoadSaved()
	{
		return loadsaved;
	}
	
	public static int getNumBrushes(String map)
	{
		File mapFile = Main.getResourceHandler().getByName(map);
		Scanner s;
		int brushes = 0;
		try 
		{
			s = new Scanner(mapFile);
			while (s.hasNextLine())
			{
				String delims = " = ";
				String[] tokens = s.next().split(delims);
				if (tokens.length > 0)
				{
					if (tokens[0].equals("Brush") || tokens[0].equals("TiledBrush") || tokens[0].equals("AnimatedBrush"))
						brushes++;
				}
				s.nextLine();
			}
			s.close();
		} 
		catch (Exception e) 
		{
			return -1;
		}
		return brushes;
	}
	
	public static int getNumTriggers(String map)
	{
		File mapFile = Main.getResourceHandler().getByName(map);
		Scanner s;
		int triggers = 0;
		try 
		{
			s = new Scanner(mapFile);
			while (s.hasNextLine())
			{
				String delims = " = ";
				String[] tokens = s.next().split(delims);
				if (tokens.length > 0)
				{
					if (tokens[0].equals("Trigger"))
						triggers++;
				}
				s.nextLine();
			}
			s.close();
		} 
		catch (Exception e) 
		{
			return -1;
		}
		return triggers;
	}
	
	public static int getNumEntities(String map)
	{
		File mapFile = Main.getResourceHandler().getByName(map);
		Scanner s;
		int entities = 0;
		try 
		{
			s = new Scanner(mapFile);
			while (s.hasNextLine())
			{
				String delims = " = ";
				String[] tokens = s.next().split(delims);
				if (tokens.length > 0)
				{
					if (tokens[0].equals("Entity"))
						entities++;
				}
				s.nextLine();
			}
			s.close();
		} 
		catch (Exception e) 
		{
			return -1;
		}
		return entities;
	}
	
	public static String getName(String map)
	{
		File mapFile = Main.getResourceHandler().getByName(map);
		Scanner s;
		String name = "";
		try 
		{
			s = new Scanner(mapFile);
			while (s.hasNextLine())
			{
				String delims = " = ";
				String[] tokens = s.nextLine().split(delims);
				if (tokens.length > 0)
				{
					if (tokens[0].equals("Name"))
					{
						name = tokens[1];
						s.close();
						return name;
					}
				}
			}
			s.close();
		} 
		catch (Exception e) 
		{
			return null;
		}
		return "Name Not Found";
	}
}