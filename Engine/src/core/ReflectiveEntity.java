package core;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;

import main.Main;
import utilities.AdvancedFilters;
import utilities.ResourceMonitor;

public class ReflectiveEntity 
	extends Entity
{
	private Color c;
	private Entity tiedEntity;
	private double reflectivity;
	private Plane plane;
	
	private int locXs;
	private int locYs;
	private double locX;
	private double locY;
	
	private Material tiedMat;
	
	public enum Plane
	{
		POSITIVE_Y, NEGATIVE_Y, POSITIVE_X, NEGATIVE_X
	};
	
	public ReflectiveEntity(BufferedImage image, int xs, int ys, boolean solid, double x, double y, int z,
			int maxHealth, boolean isInvinsible, Plane plane, double reflectivity) 
	{
		super(STATIC, image, xs, ys, solid, x, y, z, maxHealth, isInvinsible);
		this.reflectivity = reflectivity;
		this.plane = plane;
		locXs = xs;
		locYs = ys;
		locX = x;
		locY = y;
		updateImage(ReflectiveEntityHandler.STATIC_REFLECTIONS, true);
		setTransparancy(true);
		setVisible(false);
	}
	
	public ReflectiveEntity(Image image, int xs, int ys, boolean solid, double x, double y, int z,
			int maxHealth, boolean isInvinsible, Plane plane, double reflectivity) 
	{
		super(STATIC, image, xs, ys, solid, x, y, z, maxHealth, isInvinsible);
		this.reflectivity = reflectivity;
		this.plane = plane;
		updateImage(ReflectiveEntityHandler.STATIC_REFLECTIONS, true);
		setTransparancy(true);
		setVisible(false);
	}
	
	public void setColor(Color c)
	{
		this.c = c;
	}
	
	public Color getColor()
	{
		return c;
	}
	
	public synchronized BufferedImage generateReflections(int reflectionType)
	{		
		if (start == 0)
			start = System.currentTimeMillis();
		if (reflectivity == 0)
		{
			if (Main.getCurrentResourceMonitor() != null)
				Main.getCurrentResourceMonitor().increment(ResourceMonitor.Type.POST_PROCESS, System.currentTimeMillis() - start);
			return null;
		}
		double nx = getX();
		double ny = getY();
		if (plane == Plane.NEGATIVE_Y)
			ny -= getScaleY();
		else if (plane == Plane.POSITIVE_Y)
			ny += getScaleY();
		else if (plane == Plane.NEGATIVE_X)
			nx -= getScaleX();
		else if (plane == Plane.POSITIVE_X)
			nx += getScaleX();
		
		Entity e = new Entity(Entity.STATIC, true, "reflection generator", nx, ny, 100, (int) getScaleX(), (int) getScaleY(), 
				100, false);
		e.setImage(ResourceHandler.defaultTextureImage);
		e.setTransparancy(true);
		e.setVisible(true);
		//Main.getEntityHandler().addStaticEntity(e);
		Entity[] entities = e.getCollisions(true);
		BufferedImage buffImg = new BufferedImage((int) getScaleX(), (int) getScaleY(), BufferedImage.TYPE_INT_ARGB);
		Graphics g = buffImg.getGraphics();
		if (entities == null)
		{
			if (Main.getCurrentResourceMonitor() != null)
				Main.getCurrentResourceMonitor().increment(ResourceMonitor.Type.POST_PROCESS, System.currentTimeMillis() - start);
			return buffImg;
		}
		if (entities.length == 0)
		{
			if (Main.getCurrentResourceMonitor() != null)
				Main.getCurrentResourceMonitor().increment(ResourceMonitor.Type.POST_PROCESS, System.currentTimeMillis() - start);
			return buffImg;
		}
		//g.fillRect(0, 0, (int) width, (int) height);
		for (int i = 0; i < entities.length; i++)
		{
			if (entities[i] != null)
			{
				if (entities[i].getBufferedImage() != null && entities[i].isVisible() && 
						(entities[i].isOnScreen() || reflectionType == ReflectiveEntityHandler.STATIC_REFLECTIONS))
				{
					if ((reflectionType == ReflectiveEntityHandler.FAST_DYNAMIC_REFLECTIONS &&
							entities[i].shouldRenderInReflections() && entities[i].getLOD() <= Main.getEntityHandler().currLod) || 
							(reflectionType == ReflectiveEntityHandler.FULL_DYNAMIC_REFLECTIONS && entities[i].getLOD() <= Main.getEntityHandler().currLod) || 
							(reflectionType == ReflectiveEntityHandler.STATIC_REFLECTIONS && entities[i].isStatic() && entities[i].getLOD() <= Main.getEntityHandler().currLod))
					{
						int reflX = /*(int) (x - entities[i].getX());*/ (int) (entities[i].getX() - getX());
						int reflY = /*(int) (y - entities[i].getY());*/ (int) (entities[i].getY() - getY() + getScaleY());	
						g.drawImage(entities[i].getBufferedImage().getScaledInstance((int) entities[i].getScaleX(), (int) entities[i].getScaleY(), Image.SCALE_DEFAULT), 
								reflX, reflY, null);
					}
				}
			}
		}
	//	if (debug2 != null)
	//		debug2.setImage(buffImg);
		BufferedImage newBuff = AdvancedFilters.flipHorizontal(buffImg);
		return newBuff;
	}
	
	private long start = 0;
	public synchronized void updateImage(int reflectionType, boolean forceReset)
	{
		start = System.currentTimeMillis();
		if (tiedEntity != null && reflectionType != ReflectiveEntityHandler.STATIC_REFLECTIONS)
		{
			if (! tiedEntity.isOnScreen())
				return;
		}
		
		BufferedImage create = new BufferedImage((int) getScaleX(), (int) getScaleY(), BufferedImage.TYPE_INT_ARGB);
		Graphics g = create.getGraphics();
		g.drawImage(getBufferedImage(), 0, 0, null);
		if (tiedEntity != null)
			tiedEntity.setBufferedImage(create);
		
		BufferedImage overlay = generateReflections(reflectionType);
		//if (debug != null)
		//	debug.setImage(overlay);
		if (overlay != null && tiedEntity != null)
		{
			overlay = AdvancedFilters.makeImageTranslucent(overlay, reflectivity);
			overlay = AdvancedFilters.blur(overlay, Main.getReflectionHandler().blurAmount);
			g = tiedEntity.getBufferedImage().getGraphics();
			g.drawImage(overlay, 0, 0, null);
		}
		
		if (forceReset)
		{
			Main.getEntityHandler().removeEntity(tiedEntity);
			tiedEntity = null;
		}
		if (tiedEntity == null)
		{
			tiedEntity = new Entity(STATIC, create, locXs, locYs, isSolid(), locX, locY, getZBuffer(), 100, false);
			tiedEntity.setName("Reflective entity");
			tiedEntity.setWidth(tiedEntity.getActualScaleX());
			tiedEntity.setHeight(tiedEntity.getActualScaleY());
			tiedEntity.setTransparancy(true);
			tiedEntity.setMaterial(tiedMat);
			Main.getEntityHandler().addStaticEntity(tiedEntity);
		//	debug = new Entity(STATIC, create, xs, ys, false, 500, 500, z, 100, false);
		//	Main.getEntityHandler().addStaticEntity(debug);
		//	debug2 = new Entity(STATIC, create, xs, ys, false, 500, 500 - ys, z, 100, false);
		//	Main.getEntityHandler().addStaticEntity(debug2);
		}
		tiedEntity.setImage(create);
		if (Main.getCurrentResourceMonitor() != null)
			Main.getCurrentResourceMonitor().increment(ResourceMonitor.Type.POST_PROCESS, System.currentTimeMillis() - start);
	}
	
	public void setReflectiveMaterial(Material mat)
	{
		tiedMat = mat;
	}
}
