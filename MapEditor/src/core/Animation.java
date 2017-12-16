package core;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;

import main.Main;

public class Animation
	implements Runnable
{
	private String name;
	private BufferedImage[] images;
	private Image[] rawImages;
	private int frame = 0;
	private int rate;
	private boolean runAnimation;
	private boolean autoAnimate;
	private boolean limitAnimation;
	private boolean flipHorz;
	private boolean flipVert;
	private long tick = 0;
	
	Entity entity;
	
	public Animation(String name, Entity entity, BufferedImage[] images, int rate)
	{
		this.name = name;
		this.images = images;
		this.rate = rate;
		runAnimation = false;
		this.entity = entity;
		this.rawImages = images;
	}
	
	public Animation(String name, BufferedImage[] images, int rate)
	{
		this.name = name;
		this.images = images;
		this.rate = rate;
		runAnimation = false;
		this.rawImages = images;
	}
	
	public Animation(Animation old)
	{
		name = old.getName() + " copy";
		images = old.getImages();
		rawImages = old.getImages();
		rate = old.getRate();
		entity = old.getEntity();
	}
	
	public void run()
	{
		return;
	}
	
	public BufferedImage nextFrame()
	{
		frame++;
		if (frame >= images.length)
		{
			if (! limitAnimation)
				frame = 0;
			else
				frame = images.length - 1;
		}
		
		BufferedImage img = images[frame];
		
		return img;
	}
	
	public Image nextFrameImage()
	{
		frame++;
		if (frame >= images.length)
		{
			if (! limitAnimation)
				frame = 0;
			else
				frame = images.length - 1;
		}
		
		Image img = images[frame];
		
		return img;
	}
	
	public BufferedImage frame(int frame)
	{
		if (frame >= images.length || frame < 0)
		{
			Main.println("Error: animation frame outside of animation bounds", Color.RED);
			return null;
		}
		else
			return images[frame];
	}
	
	public Image currFrame()
	{
		return images[frame];
	}
	
	public void setFrame(int frame)
	{
		if (frame > images.length || frame < 0)
			Main.println("Error: animation frame outside of animation bounds", Color.RED);
		else
			this.frame = frame;
	}
	
	public void update(Entity e)
	{
		e.setImage(images[frame]);
		Main.getEntityHandler().invokeUpdate();
	}
	
	public Entity getEntity()
	{
		return entity;
	}
	
	public int getFrame()
	{
		return frame;
	}
	
	public int getRate()
	{
		return rate;
	}
	
	public int getNumFrames()
	{
		return images.length;
	}
	
	public void setRate(int rate)
	{
		this.rate = rate;
	}
	
	public boolean isAnimating()
	{
		return runAnimation;
	}
	
	public void shouldRun(boolean runAnimation)
	{
		this.runAnimation = runAnimation;
	}
	
	public String getName()
	{
		return name;
	}
	
	public String toString()
	{
		return name + " frame: " + frame;	
	}
	
	public void setEntity(Entity e)
	{
		this.entity = e;
	}
	
	public void limitAnimation(boolean limitAnimation)
	{
		this.limitAnimation = limitAnimation;
	}
	
	public void setOrientation(boolean xOr, boolean yOr)
	{
		flipHorz = xOr;
		flipVert = yOr;
	}
	
	public boolean flipHorizontal()
	{
		return flipHorz;
	}
	
	public boolean flipVertical()
	{
		return flipVert;
	}
	
	public void updateImages()
	{
		AffineTransform tx = AffineTransform.getScaleInstance(1, 1);
		AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
		for (int i = 0; i < images.length; i++)
		{
			tx = AffineTransform.getScaleInstance(1, -1);
			tx.translate(-1 * images[i].getWidth(null), 0);
			op = new AffineTransformOp(tx, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
			images[i] = op.filter(images[i], null);

		}
	}
	
	public void tick()
	{
		tick++;
	}
	
	public void setTick(long tick)
	{
		this.tick = tick;
	}
	
	public long getTick()
	{
		return tick;
	}
	
	public boolean limitingAnimation()
	{
		return limitAnimation;
	}
	
	public BufferedImage[] getImages()
	{
		return images;
	}
	
	public BufferedImage getImageAtFrame(int frame)
	{
		return images[frame];
	}
	
	public static void animate(Entity e, Animation a)
	{
		Image img;
		img = a.nextFrame();
		e.setImage(img);
	}
}
